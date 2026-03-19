package com.team900.frc2026.subsystems.vision;

import static com.team900.frc2026.subsystems.vision.VisionConstants.*;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservation;
import com.team900.frc2026.subsystems.vision.VisionIO.PoseObservationType;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.littletonrobotics.junction.Logger;

public class VisionSubsystem extends SubsystemBase {
    private final VisionConsumer consumer;
    private final RobotState state;
    private final VisionIO[] io;
    private final CameraConfig[] configs;
    private final VisionIOInputsAutoLogged[] inputs;
    private final Alert[] disconnectedAlerts;

    // Duplicate-timestamp rejection: skip observations we've already processed.
    private final double[] lastProcessedTimestamps;

    @Getter private Rotation2d rotation2dToHubTx = new Rotation2d();
    @Getter private Rotation2d rotation2dToHubTy = new Rotation2d();

    private static final AprilTagFieldLayout fieldLayout =
            AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark);

    /**
     * Creates a new VisionSubsystem.
     *
     * @param consumer Callback that receives accepted observations and std devs
     *     (typically RobotState, which implements VisionConsumer).
     * @param state RobotState instance for pose-difference checks and angular
     *     velocity rejection.
     * @param configs Per-camera configuration (fixed vs turret-mounted). Must be
     *     the same length as io.
     * @param io One or more VisionIO implementations (one per camera).
     */
    public VisionSubsystem(
            VisionConsumer consumer,
            RobotState state,
            CameraConfig[] configs,
            VisionIO... io) {
        this.consumer = consumer;
        this.state = state;
        this.io = io;
        this.configs = configs;

        if (configs.length != io.length) {
            throw new IllegalArgumentException(
                    "CameraConfig array length ("
                            + configs.length
                            + ") must match VisionIO array length ("
                            + io.length
                            + ")");
        }

        // Initialize inputs
        this.inputs = new VisionIOInputsAutoLogged[io.length];
        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = new VisionIOInputsAutoLogged();
        }

        // Initialize disconnected alerts
        this.disconnectedAlerts = new Alert[io.length];
        for (int i = 0; i < io.length; i++) {
            disconnectedAlerts[i] =
                    new Alert(
                            "Vision camera " + i + " is disconnected.",
                            AlertType.kWarning);
        }

        this.lastProcessedTimestamps = new double[io.length];
    }

    /**
     * Returns the X angle to the best target for simple target servoing.
     *
     * @param cameraIndex The index of the camera to use.
     */
    public Rotation2d getTargetX(int cameraIndex) {
        return inputs[cameraIndex].latestTargetObservation.tx();
    }

    @Override
    public void periodic() {
        // --- 1. Read inputs from all cameras ---
        for (int i = 0; i < io.length; i++) {
            io[i].updateInputs(inputs[i]);
            Logger.processInputs("Vision/Camera" + i, inputs[i]);
        }

        // --- 2. Process observations ---
        List<Pose3d> allTagPoses = new LinkedList<>();
        List<Pose3d> allRobotPoses = new LinkedList<>();
        List<Pose3d> allRobotPosesAccepted = new LinkedList<>();
        List<Pose3d> allRobotPosesRejected = new LinkedList<>();

        for (int cameraIndex = 0; cameraIndex < io.length; cameraIndex++) {
            disconnectedAlerts[cameraIndex].set(!inputs[cameraIndex].connected);

            List<Pose3d> tagPoses = new LinkedList<>();
            List<Pose3d> robotPoses = new LinkedList<>();
            List<Pose3d> robotPosesAccepted = new LinkedList<>();
            List<Pose3d> robotPosesRejected = new LinkedList<>();

            // Log seen tag poses
            for (int tagId : inputs[cameraIndex].tagIds) {
                fieldLayout.getTagPose(tagId).ifPresent(tagPoses::add);
            }

            // Prefer SolvePNP; fall back to pinhole only if no SolvePNP was accepted.
            boolean usedSolvePnp = false;

            for (var observation : inputs[cameraIndex].poseObservations) {
                if (observation.type() == PoseObservationType.SOLVE_PNP) {
                    boolean accepted =
                            processObservation(
                                    observation,
                                    cameraIndex,
                                    robotPoses,
                                    robotPosesAccepted,
                                    robotPosesRejected);
                    if (accepted) usedSolvePnp = true;
                }
            }

            if (!usedSolvePnp) {
                for (var observation : inputs[cameraIndex].poseObservations) {
                    if (observation.type() == PoseObservationType.PINHOLE) {
                        processObservation(
                                observation,
                                cameraIndex,
                                robotPoses,
                                robotPosesAccepted,
                                robotPosesRejected);
                    }
                }
            }

            // Update target observation getters (for servoing)
            if (cameraIndex == 0) {
                rotation2dToHubTx = inputs[0].latestTargetObservation.tx();
                rotation2dToHubTy = inputs[0].latestTargetObservation.ty();
            }

            // Log per-camera data
            Logger.recordOutput(
                    "Vision/Camera" + cameraIndex + "/TagPoses",
                    tagPoses.toArray(new Pose3d[0]));
            Logger.recordOutput(
                    "Vision/Camera" + cameraIndex + "/RobotPoses",
                    robotPoses.toArray(new Pose3d[0]));
            Logger.recordOutput(
                    "Vision/Camera" + cameraIndex + "/RobotPosesAccepted",
                    robotPosesAccepted.toArray(new Pose3d[0]));
            Logger.recordOutput(
                    "Vision/Camera" + cameraIndex + "/RobotPosesRejected",
                    robotPosesRejected.toArray(new Pose3d[0]));

            allTagPoses.addAll(tagPoses);
            allRobotPoses.addAll(robotPoses);
            allRobotPosesAccepted.addAll(robotPosesAccepted);
            allRobotPosesRejected.addAll(robotPosesRejected);
        }

        // Log summary data
        Logger.recordOutput(
                "Vision/Summary/TagPoses", allTagPoses.toArray(new Pose3d[0]));
        Logger.recordOutput(
                "Vision/Summary/RobotPoses", allRobotPoses.toArray(new Pose3d[0]));
        Logger.recordOutput(
                "Vision/Summary/RobotPosesAccepted",
                allRobotPosesAccepted.toArray(new Pose3d[0]));
        Logger.recordOutput(
                "Vision/Summary/RobotPosesRejected",
                allRobotPosesRejected.toArray(new Pose3d[0]));
    }

    /**
     * Processes a single PoseObservation: reject, calculate std devs, or accept.
     *
     * @return true if the observation was accepted.
     */
    private boolean processObservation(
            PoseObservation observation,
            int cameraIndex,
            List<Pose3d> robotPoses,
            List<Pose3d> robotPosesAccepted,
            List<Pose3d> robotPosesRejected) {

        robotPoses.add(observation.pose());

        // Reject: duplicate timestamp
        if (observation.timestamp() == lastProcessedTimestamps[cameraIndex]) {
            robotPosesRejected.add(observation.pose());
            return false;
        }

        // Reject: basic sanity checks
        if (shouldRejectPose(observation)) {
            robotPosesRejected.add(observation.pose());
            return false;
        }

        // Reject: angular velocity too high
        if (shouldRejectOnAngularVelocity(observation, cameraIndex)) {
            Logger.recordOutput(
                    "Vision/Camera" + cameraIndex + "/RejectedAngularVelocity", true);
            robotPosesRejected.add(observation.pose());
            return false;
        }

        // Calculate std devs and accept
        Matrix<N3, N1> stdDevs = calculateStdDevs(observation, cameraIndex);
        consumer.accept(observation, stdDevs);
        lastProcessedTimestamps[cameraIndex] = observation.timestamp();
        robotPosesAccepted.add(observation.pose());
        return true;
    }

    /**
     * Field-boundary, Z-error, and ambiguity rejection.
     */
    private boolean shouldRejectPose(PoseObservation observation) {
        if (observation.tagCount() == 0) return true;

        if (observation.tagCount() == 1 && observation.ambiguity() > maxAmbiguity) {
            return true;
        }

        if (Math.abs(observation.pose().getZ()) > maxZError) return true;

        double x = observation.pose().getX();
        double y = observation.pose().getY();
        if (x < 0.0 || x > fieldLayout.getFieldLength()) return true;
        if (y < 0.0 || y > fieldLayout.getFieldWidth()) return true;

        return false;
    }

    /**
     * Rejects observations taken during fast rotation.
     *
     * <p>For turret-mounted cameras, the effective yaw rate in the field frame is
     * the SUM of the chassis yaw rate and the turret yaw rate. A turret spinning
     * at +100 deg/s on a chassis spinning at +100 deg/s means the camera is
     * sweeping at 200 deg/s in the field frame, which destroys pose accuracy.
     *
     * <p>For fixed cameras, only chassis angular velocities matter.
     *
     * <p>Adapted from 254's shouldUsePinhole / shouldUseMegatag logic.
     */
    private boolean shouldRejectOnAngularVelocity(
            PoseObservation observation, int cameraIndex) {
        double timestamp = observation.timestamp();
        double windowStart = timestamp - angularVelocityTimeWindowSec;
        boolean isTurret = configs[cameraIndex].isTurretCamera();

        if (observation.type() == PoseObservationType.SOLVE_PNP) {
            // SolvePNP is more tolerant of rotation but still degrades under
            // very fast effective yaw
            double effectiveYawRate = getEffectiveYawRate(windowStart, timestamp, isTurret);
            if (effectiveYawRate > maxYawAngularVelocityForSolvePNP) {
                Logger.recordOutput(
                        "Vision/Rejection/SolvePNPYaw", effectiveYawRate);
                return true;
            }
        } else {
            // PINHOLE: sensitive to all three axes
            double effectiveYawRate = getEffectiveYawRate(windowStart, timestamp, isTurret);
            if (effectiveYawRate > maxYawAngularVelocityForPinhole) {
                Logger.recordOutput(
                        "Vision/Rejection/PinholeYaw", effectiveYawRate);
                return true;
            }

            // Pitch and roll are chassis-only (turret doesn't add pitch/roll)
            Optional<Double> maxPitch =
                    state.getMaxAbsDrivePitchAngularVelocityInRange(
                            windowStart, timestamp);
            if (maxPitch.isPresent()
                    && Math.abs(maxPitch.get()) > maxPitchAngularVelocityForPinhole) {
                Logger.recordOutput(
                        "Vision/Rejection/PinholePitch", maxPitch.get());
                return true;
            }

            Optional<Double> maxRoll =
                    state.getMaxAbsDriveRollAngularVelocityInRange(
                            windowStart, timestamp);
            if (maxRoll.isPresent()
                    && Math.abs(maxRoll.get()) > maxRollAngularVelocityForPinhole) {
                Logger.recordOutput(
                        "Vision/Rejection/PinholeRoll", maxRoll.get());
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the effective yaw rate for a camera in the given time window.
     *
     * <p>For turret cameras: chassis yaw rate + turret yaw rate (their sum is
     * the camera's yaw rate in field frame). We take the max of the absolute
     * combined value over the window.
     *
     * <p>For fixed cameras: just the chassis yaw rate.
     */
    private double getEffectiveYawRate(
            double windowStart, double windowEnd, boolean isTurret) {
        Optional<Double> maxChassisYaw =
                state.getMaxAbsDriveYawAngularVelocityInRange(windowStart, windowEnd);

        if (!isTurret) {
            return maxChassisYaw.map(Math::abs).orElse(0.0);
        }

        // For turret camera: combine turret + chassis yaw velocities
        Optional<Double> maxTurretYaw =
                state.getMaxAbsTurretAngularVelocityInRange(windowStart, windowEnd);

        if (maxTurretYaw.isPresent() && maxChassisYaw.isPresent()) {
            // The camera's field-frame yaw rate is turretVel + chassisVel.
            // We check the absolute combined value.
            return Math.abs(maxTurretYaw.get() + maxChassisYaw.get());
        } else if (maxChassisYaw.isPresent()) {
            return Math.abs(maxChassisYaw.get());
        } else if (maxTurretYaw.isPresent()) {
            return Math.abs(maxTurretYaw.get());
        }
        return 0.0;
    }

    /**
     * Tiered std dev calculation based on observation type, tag count, tag area,
     * and pose difference from odometry.
     */
    private Matrix<N3, N1> calculateStdDevs(
            PoseObservation observation, int cameraIndex) {

        double poseDifference = Double.MAX_VALUE;
        Optional<Pose2d> odomPose = state.getFieldToRobot(observation.timestamp());
        if (odomPose.isPresent()) {
            poseDifference =
                    observation
                            .pose()
                            .toPose2d()
                            .getTranslation()
                            .getDistance(odomPose.get().getTranslation());
        }

        double linearStdDev;
        double angularStdDev;

        if (observation.type() == PoseObservationType.SOLVE_PNP) {
            linearStdDev = calculateSolvePnpLinearStdDev(observation, poseDifference);
            angularStdDev = calculateSolvePnpAngularStdDev(observation);
        } else {
            linearStdDev = calculatePinholeLinearStdDev(observation, poseDifference);
            angularStdDev = Units.degreesToRadians(pinholeRotStdDevDeg);
        }

        if (cameraIndex < cameraStdDevFactors.length) {
            linearStdDev *= cameraStdDevFactors[cameraIndex];
            angularStdDev *= cameraStdDevFactors[cameraIndex];
        }

        return VecBuilder.fill(linearStdDev, linearStdDev, angularStdDev);
    }

    private double calculateSolvePnpLinearStdDev(
            PoseObservation observation, double poseDifference) {
        int tagCount = observation.tagCount();
        double avgArea = observation.averageTagArea();

        if (tagCount >= 2 && avgArea > tagAreaThresholdMedium) {
            return solvePnpMultiTagTightXY;
        }
        if (tagCount >= 2) {
            return solvePnpMultiTagLooseXY;
        }
        if (avgArea > tagAreaThresholdLarge
                && poseDifference < maxPoseDifferenceForMediumStdDev) {
            return solvePnpSingleTagCloseXY;
        }
        if (avgArea > tagAreaThresholdMedium
                && poseDifference < maxPoseDifferenceForTightStdDev) {
            return solvePnpSingleTagFarXY;
        }
        return solvePnpFallbackXY;
    }

    private double calculateSolvePnpAngularStdDev(PoseObservation observation) {
        if (observation.tagCount() >= 2) {
            return Units.degreesToRadians(solvePnpMultiTagDegStdDev);
        }
        if (observation.averageTagArea() > tagAreaThresholdLarge) {
            return Units.degreesToRadians(solvePnpSingleTagDegStdDev);
        }
        return Units.degreesToRadians(solvePnpFallbackDegStdDev);
    }

    private double calculatePinholeLinearStdDev(
            PoseObservation observation, double poseDifference) {
        int tagCount = observation.tagCount();
        double avgDistance = observation.averageTagDistance();

        if (tagCount >= 2 && avgDistance < 3.0) {
            return pinholeMultiTagTightXY;
        }
        if (avgDistance < 5.0 && poseDifference < maxPoseDifferenceForMediumStdDev) {
            return pinholeSingleTagCloseXY;
        }
        if (avgDistance < 3.0 && poseDifference < maxPoseDifferenceForTightStdDev) {
            return pinholeSingleTagFarXY;
        }
        if (tagCount > 1) {
            return 1.2;
        }
        return pinholeFallbackXY;
    }

    @FunctionalInterface
    public interface VisionConsumer {
        void accept(
                PoseObservation observation,
                Matrix<N3, N1> visionMeasurementStdDevs);
    }
}
