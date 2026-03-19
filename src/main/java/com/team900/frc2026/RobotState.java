package com.team900.frc2026;

import com.team900.frc2026.subsystems.turret.TurretConstants;
import com.team900.frc2026.subsystems.vision.VisionConstants;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservation;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservationType;
import com.team900.frc2026.subsystems.vision.VisionSubsystem.VisionConsumer;
import com.team900.lib.util.ConcurrentTimeInterpolatableBuffer;
import com.team900.lib.util.MathHelpers;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.Timer;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntSupplier;
import org.littletonrobotics.junction.Logger;

/** Tracks robot state including pose, velocities, and mechanism positions. */
public class RobotState implements VisionConsumer {

    private static volatile RobotState instance;

    public static final double LOOKBACK_TIME = 1.0;

    private RobotState() {
        fieldToRobot.addSample(0.0, MathHelpers.kPose2dZero);
        robotToTurret.addSample(0.0, MathHelpers.kRotation2dZero);
        turretAngularVelocity.addSample(0.0, 0.0);
        driveYawAngularVelocity.addSample(0.0, 0.0);
        turretPositionRadians.addSample(0.0, 0.0);

        // Initialize mechanism positions
        intakePivotRotations.set(0.0);
        hoodRotations.set(0.0);
    }

    // State of robot.

    // Kinematic Frames
    // Robot's pose in field coordinates over time
    private final ConcurrentTimeInterpolatableBuffer<Pose2d> fieldToRobot =
            ConcurrentTimeInterpolatableBuffer.createBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Rotation2d> robotToTurret =
            ConcurrentTimeInterpolatableBuffer.createBuffer(LOOKBACK_TIME);
    private static final Transform2d TURRET_TO_CAMERA =
            new Transform2d(
                    VisionConstants.kTurretToCameraXMeters,
                    VisionConstants.kTurretToCameraYMeters,
                    MathHelpers.kRotation2dZero);
    // Current robot-relative chassis speeds (measured from encoders)
    private final AtomicReference<ChassisSpeeds> measuredRobotRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Current field-relative chassis speeds (measured from encoders)
    private final AtomicReference<ChassisSpeeds> measuredFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Desired robot-relative chassis speeds (set by control systems)
    private final AtomicReference<ChassisSpeeds> desiredRobotRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Desired field-relative chassis speeds (set by control systems)
    private final AtomicReference<ChassisSpeeds> desiredFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    private final AtomicReference<ChassisSpeeds> fusedFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());

    private final AtomicInteger iteration = new AtomicInteger(0);

    private double lastUsedMultiTagTimestamp = 0;
    private Pose2d lastUsedMultiTagPose = Pose2d.kZero;
    private ConcurrentTimeInterpolatableBuffer<Double> turretAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private ConcurrentTimeInterpolatableBuffer<Double> turretPositionRadians =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> driveYawAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> driveRollAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> drivePitchAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);

    private final ConcurrentTimeInterpolatableBuffer<Double> drivePitchRads =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> driveRollRads =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> accelX =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> accelY =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);

    private final AtomicBoolean hasHoodZero = new AtomicBoolean(false);

    private double autoStartTime;

    private Optional<Pose2d> trajectoryTargetPose = Optional.empty();
    private Optional<Pose2d> trajectoryCurrentPose = Optional.empty();

    public void setAutoStartTime(double timestamp) {
        autoStartTime = timestamp;
    }

    public double getAutoStartTime() {
        return autoStartTime;
    }

    public void updateHoodHasZero(boolean hoodZereod) {
        hasHoodZero.set(hoodZereod);
    }

    public boolean getHoodHasZeroed() {
        return hasHoodZero.get();
    }

    public void addOdometryMeasurement(double timestamp, Pose2d pose) {
        fieldToRobot.addSample(timestamp, pose);
    }

    public void incrementIterationCount() {
        iteration.incrementAndGet();
    }

    public int getIteration() {
        return iteration.get();
    }

    public IntSupplier getIterationSupplier() {
        return () -> getIteration();
    }

    public void addDriveMotionMeasurements(
            double timestamp,
            double angularRollRadsPerS,
            double angularPitchRadsPerS,
            double angularYawRadsPerS,
            double pitchRads,
            double rollRads,
            double accelX,
            double accelY,
            ChassisSpeeds desiredRobotRelativeChassisSpeeds,
            ChassisSpeeds desiredFieldRelativeSpeeds,
            ChassisSpeeds measuredSpeeds,
            ChassisSpeeds measuredFieldRelativeSpeeds,
            ChassisSpeeds fusedFieldRelativeSpeeds) {
        this.driveRollAngularVelocity.addSample(timestamp, angularRollRadsPerS);
        this.drivePitchAngularVelocity.addSample(timestamp, angularPitchRadsPerS);
        this.driveYawAngularVelocity.addSample(timestamp, angularYawRadsPerS);
        this.drivePitchRads.addSample(timestamp, pitchRads);
        this.driveRollRads.addSample(timestamp, rollRads);
        this.accelY.addSample(timestamp, accelY);
        this.accelX.addSample(timestamp, accelX);
        this.desiredRobotRelativeChassisSpeeds.set(desiredRobotRelativeChassisSpeeds);
        this.desiredFieldRelativeChassisSpeeds.set(desiredFieldRelativeSpeeds);
        this.measuredRobotRelativeChassisSpeeds.set(measuredSpeeds);
        this.measuredFieldRelativeChassisSpeeds.set(measuredFieldRelativeSpeeds);
        this.fusedFieldRelativeChassisSpeeds.set(fusedFieldRelativeSpeeds);
    }

    public Map.Entry<Double, Pose2d> getLatestFieldToRobot() {
        return fieldToRobot.getLatest();
    }

    /**
     * Predicts robot's future pose based on current velocity.
     *
     * @param lookaheadTimeS How far ahead to predict (seconds)
     * @return Predicted pose
     */
    public Pose2d getPredictedFieldToRobot(double lookaheadTimeS) {
        var maybeFieldToRobot = getLatestFieldToRobot();
        Pose2d fieldToRobot =
                maybeFieldToRobot == null ? MathHelpers.kPose2dZero : maybeFieldToRobot.getValue();
        var delta = getLatestRobotRelativeChassisSpeed();
        delta = delta.times(lookaheadTimeS);
        return fieldToRobot.exp(
                new Twist2d(
                        delta.vxMetersPerSecond,
                        delta.vyMetersPerSecond,
                        delta.omegaRadiansPerSecond));
    }

    // This has rotation and radians to allow for wrapping tracking.
    public void addTurretUpdates(
            double timestamp,
            Rotation2d turretRotation,
            double turretRadians,
            double angularYawRadsPerS) {
        // turret frame 180 degrees off from robot frame
        robotToTurret.addSample(timestamp, turretRotation.rotateBy(MathHelpers.kRotation2dPi));
        this.turretAngularVelocity.addSample(timestamp, angularYawRadsPerS);
        this.turretPositionRadians.addSample(timestamp, turretRadians);
    }

    public double getLatestTurretPositionRadians() {
        return this.turretPositionRadians.getInternalBuffer().lastEntry().getValue();
    }

    public double getLatestTurretAngularVelocity() {
        return this.turretAngularVelocity.getInternalBuffer().lastEntry().getValue();
    }

    public Optional<Pose2d> getFieldToRobot(double timestamp) {
        return fieldToRobot.getSample(timestamp);
    }

    public Transform2d getTurretToCamera() {
        return TURRET_TO_CAMERA;
    }

    // Internal helper — apply turret offset to any robot pose
    private Pose2d fieldToTurret(Pose2d fieldToRobot) {
        return fieldToRobot.transformBy(
                new Transform2d(TurretConstants.turretOffsetFromCenter, Rotation2d.kZero));
    }

    // Latest
    public Pose2d getLatestFieldToTurret() {
        return fieldToTurret(getLatestFieldToRobot().getValue());
    }

    // Past (by timestamp)
    public Optional<Pose2d> getFieldToTurret(double timestamp) {
        return getFieldToRobot(timestamp).map(this::fieldToTurret);
    }

    // Future (by lookahead)
    public Pose2d getPredictedFieldToTurret(double lookaheadTimeS) {
        return fieldToTurret(getPredictedFieldToRobot(lookaheadTimeS));
    }

    /** Returns the current robot-to-turret-camera Transform3d, composing the live turret angle. */
public Transform3d getRobotToTurretCamera() {
    var entry = getLatestRobotToTurret();
    Transform3d robotToTurret =
            new Transform3d(
                    TurretConstants.turretOffsetFromCenter.getX(),
                    TurretConstants.turretOffsetFromCenter.getY(),
                    0.0,
                    new Rotation3d(0, 0, turretAngle.getRadians()));
    return robotToTurret.plus(VisionConstants.turretToCamera0);
}


    public Map.Entry<Double, Rotation2d> getLatestRobotToTurret() {
        return robotToTurret.getLatest();
    }

    public ChassisSpeeds getLatestMeasuredFieldRelativeChassisSpeeds() {
        return measuredFieldRelativeChassisSpeeds.get();
    }

    public ChassisSpeeds getLatestRobotRelativeChassisSpeed() {
        return measuredRobotRelativeChassisSpeeds.get();
    }

    public ChassisSpeeds getLatestDesiredRobotRelativeChassisSpeeds() {
        return desiredRobotRelativeChassisSpeeds.get();
    }

    public ChassisSpeeds getLatestDesiredFieldRelativeChassisSpeed() {
        return desiredFieldRelativeChassisSpeeds.get();
    }

    public ChassisSpeeds getLatestFusedFieldRelativeChassisSpeed() {
        return fusedFieldRelativeChassisSpeeds.get();
    }

    public ChassisSpeeds getLatestFusedRobotRelativeChassisSpeed() {
        var speeds = getLatestRobotRelativeChassisSpeed();
        speeds.omegaRadiansPerSecond =
                getLatestFusedFieldRelativeChassisSpeed().omegaRadiansPerSecond;
        return speeds;
    }

    private Optional<Double> getMaxAbsValueInRange(
            ConcurrentTimeInterpolatableBuffer<Double> buffer, double minTime, double maxTime) {
        var submap = buffer.getInternalBuffer().subMap(minTime, maxTime).values();
        var max = submap.stream().max(Double::compare);
        var min = submap.stream().min(Double::compare);
        if (max.isEmpty() || min.isEmpty()) return Optional.empty();
        if (Math.abs(max.get()) >= Math.abs(min.get())) return max;
        else return min;
    }

    public Optional<Double> getMaxAbsDriveYawAngularVelocityInRange(
            double minTime, double maxTime) {
        // Gyro yaw rate not set in sim.
        if (Robot.isReal()) return getMaxAbsValueInRange(driveYawAngularVelocity, minTime, maxTime);
        return Optional.of(measuredRobotRelativeChassisSpeeds.get().omegaRadiansPerSecond);
    }

    public Optional<Double> getMaxAbsDrivePitchAngularVelocityInRange(
            double minTime, double maxTime) {
        return getMaxAbsValueInRange(drivePitchAngularVelocity, minTime, maxTime);
    }

    public Optional<Double> getMaxAbsDriveRollAngularVelocityInRange(
            double minTime, double maxTime) {
        return getMaxAbsValueInRange(driveRollAngularVelocity, minTime, maxTime);
    }

    public Optional<Double> getMaxAbsTurretAngularVelocityInRange(
        double minTime, double maxTime) {
    return getMaxAbsValueInRange(turretAngularVelocity, minTime, maxTime);
}

    public double lastUsedMultiTagTimestamp() {
        return lastUsedMultiTagTimestamp;
    }

    public Pose2d lastUsedMultiTagPose() {
        return lastUsedMultiTagPose;
    }

    public void updateLogger() {
        if (this.driveYawAngularVelocity.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/YawAngularVelocity",
                    this.driveYawAngularVelocity.getInternalBuffer().lastEntry().getValue());
        }
        if (this.driveRollAngularVelocity.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/RollAngularVelocity",
                    this.driveRollAngularVelocity.getInternalBuffer().lastEntry().getValue());
        }
        if (this.drivePitchAngularVelocity.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/PitchAngularVelocity",
                    this.drivePitchAngularVelocity.getInternalBuffer().lastEntry().getValue());
        }
        if (this.drivePitchRads.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/PitchRads",
                    this.drivePitchRads.getInternalBuffer().lastEntry().getValue());
        }
        if (this.driveRollRads.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/RollRads",
                    this.driveRollRads.getInternalBuffer().lastEntry().getValue());
        }
        if (this.accelX.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/AccelX", this.accelX.getInternalBuffer().lastEntry().getValue());
        }
        if (this.accelY.getInternalBuffer().lastEntry() != null) {
            Logger.recordOutput(
                    "RobotState/AccelY", this.accelY.getInternalBuffer().lastEntry().getValue());
        }
        Logger.recordOutput(
                "RobotState/DesiredChassisSpeedFieldFrame",
                getLatestDesiredFieldRelativeChassisSpeed());
        Logger.recordOutput(
                "RobotState/DesiredChassisSpeedRobotFrame",
                getLatestDesiredRobotRelativeChassisSpeeds());
        Logger.recordOutput(
                "RobotState/MeasuredChassisSpeedFieldFrame",
                getLatestMeasuredFieldRelativeChassisSpeeds());
        Logger.recordOutput(
                "RobotState/FusedChassisSpeedFieldFrame",
                getLatestFusedFieldRelativeChassisSpeed());

        Logger.recordOutput("RobotState/HasHoodZero", getHoodHasZeroed());

        // Add mechanism logging
        Logger.recordOutput("RobotState/TurretRotations", getLatestTurretPositionRadians());
        Logger.recordOutput("RobotState/HoodRotations", getHoodRotations());
        Logger.recordOutput("RobotState/IntakePivotRotations", getIntakePivotRotations());
    }

    private final AtomicReference<Optional<Integer>> exclusiveTag =
            new AtomicReference<>(Optional.empty());

    private final AtomicReference<Double> hoodRotations = new AtomicReference<>(0.0);
    private final AtomicReference<Double> hoodRPS = new AtomicReference<>(0.0);

    private final AtomicReference<Double> intakeRollerRotations = new AtomicReference<>(0.0);
    private final AtomicReference<Double> intakeRollerRPS = new AtomicReference<>(0.0);

    private final AtomicReference<Double> intakePivotRotations = new AtomicReference<>(0.0);
    private final AtomicReference<Double> intakePivotRPS = new AtomicReference<>(0.0);

    private final AtomicReference<Double> spindexerRotations = new AtomicReference<>(0.0);
    private final AtomicReference<Double> spindexerRPS = new AtomicReference<>(0.0);

    private final AtomicReference<Double> handoffRotations = new AtomicReference<>(0.0);
    private final AtomicReference<Double> handoffRPS = new AtomicReference<>(0.0);

    private final AtomicReference<Double> shooterRPS = new AtomicReference<>(0.0);

    public void setShooterRPS(double rps) {
        shooterRPS.set(rps);
    }

    public double getShooterRPS() {
        return shooterRPS.get();
    }

    public void setSpindexerRotations(double rotations) {
        spindexerRotations.set(rotations);
    }

    public void setSpindexerRPS(double rps) {
        spindexerRPS.set(rps);
    }

    public double getSpindexerRotations() {
        return spindexerRotations.get();
    }

    public double getSpindexerRPS() {
        return spindexerRPS.get();
    }

    public void setHandoffRotations(double rotations) {
        handoffRotations.set(rotations);
    }

    public void setHandoffRPS(double rps) {
        handoffRPS.set(rps);
    }

    public double getHandoffRotations() {
        return handoffRotations.get();
    }

    public double getHandoffRPS() {
        return handoffRPS.get();
    }

    public void setIntakePivotRotations(double rotations) {
        intakePivotRotations.set(rotations);
    }

    public void setIntakePivotRPS(double rps) {
        intakePivotRPS.set(0.0);
    }

    public double getIntakePivotRotations() {
        return intakePivotRotations.get();
    }

    public double getIntakePivotRPS() {
        return intakePivotRPS.get();
    }

    public void setHoodRotations(double rotations) {
        hoodRotations.set(rotations);
    }

    public void setHoodRPS(double rps) {
        hoodRPS.set(rps);
    }

    public void setIntakeRollerRotations(double rotations) {
        intakeRollerRotations.set(rotations);
    }

    public void setIntakeRollerRPS(double rps) {
        intakeRollerRPS.set(rps);
    }

    public double getHoodRotations() {
        return hoodRotations.get();
    }

    public double getIntakeRollerRotations() {
        return intakeRollerRotations.get();
    }

    public double getIntakeRollerRPS() {
        return intakeRollerRPS.get();
    }

    // not helpful for right now since we can't check tag ids with our estimates
    public void setExclusiveTag(int id) {
        exclusiveTag.set(Optional.of(id));
    }

    public void clearExclusiveTag() {
        exclusiveTag.set(Optional.empty());
    }

    public Optional<Integer> getExclusiveTag() {
        return exclusiveTag.get();
    }

    public double getDrivePitchRadians() {
        if (this.drivePitchRads.getInternalBuffer().lastEntry() != null) {
            return drivePitchRads.getInternalBuffer().lastEntry().getValue();
        }
        return 0.0;
    }

    public double getDriveRollRadians() {
        if (this.driveRollRads.getInternalBuffer().lastEntry() != null) {
            return driveRollRads.getInternalBuffer().lastEntry().getValue();
        }
        return 0.0;
    }

    //     public void logControllerMode() {
    //         Logger.recordOutput("Controller Mode",
    // ModalControls.getInstance().getMode().toString());
    //     }

    public static RobotState getInstance() {
        if (instance == null) {
            synchronized (RobotState.class) {
                if (instance == null) {

                    instance = new RobotState();
                }
            }
        }
        return instance;
    }

    /** Adds a new timestamped vision measurement. */
    @Override
    public void accept(PoseObservation observation, Matrix<N3, N1> visionMeasurementStdDevs) {
        updatePoseObservation(observation, visionMeasurementStdDevs);
    }

    public void updatePoseObservation(
            PoseObservation poseObservation, Matrix<N3, N1> visionMeasurementStdDevs) {

        if (poseObservation.type() == PoseObservationType.SOLVE_PNP)
            lastUsedMultiTagTimestamp = Timer.getFPGATimestamp();
        RobotContainer.getInstance()
                .getDriveSubsystem()
                .getPoseEstimator()
                .addVisionMeasurement(
                        poseObservation.pose().toPose2d(),
                        poseObservation.timestamp(),
                        visionMeasurementStdDevs);
    }
}
