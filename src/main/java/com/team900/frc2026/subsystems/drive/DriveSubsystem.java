// Copyright (c) 2025 FRC 1533
// http://github.com/triplestrange
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package com.team900.frc2026.subsystems.drive;

import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.team900.frc2026.Constants;
import com.team900.frc2026.Constants.Mode;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.vision.VisionFieldPoseEstimate;
import com.team900.lib.util.FullSubsystem;
import com.team900.lib.util.Util;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Getter;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class DriveSubsystem extends FullSubsystem {

    static final Lock odometryLock = new ReentrantLock();

    DriveViz telemtry = new DriveViz(getMaxLinearSpeedMetersPerSec());

    private final GyroIO gyroIO;
    private final GyroIOInputsAutoLogged gyroInputs = new GyroIOInputsAutoLogged();
    private final Module[] modules = new Module[4]; // FL, FR, BL, BR

    private ChassisSpeeds prePoofed = new ChassisSpeeds();

    private final SysIdRoutine sysId;

    private final Alert gyroDisconnectedAlert =
            new Alert("Disconnected gyro, using kinematics as fallback.", AlertType.kError);

    private SwerveDriveKinematics kinematics =
            new SwerveDriveKinematics(DriveConstants.getModuleTranslations());

    private Rotation2d rawYawRotation = new Rotation2d();

    private double rawYawVelocity = 0.0;

    private SwerveModulePosition[] lastModulePositions = // For delta tracking
            new SwerveModulePosition[] {
                new SwerveModulePosition(),
                new SwerveModulePosition(),
                new SwerveModulePosition(),
                new SwerveModulePosition()
            };

    @Getter
    private SwerveDrivePoseEstimator poseEstimator =
            new SwerveDrivePoseEstimator(
                    kinematics, rawYawRotation, lastModulePositions, new Pose2d());

    public DriveSubsystem(
            GyroIO gyroIO,
            ModuleIO flModuleIO,
            ModuleIO frModuleIO,
            ModuleIO blModuleIO,
            ModuleIO brModuleIO) {
        this.gyroIO = gyroIO;

        modules[0] = new Module(flModuleIO, 0, CompTunerConstants.FrontLeft);
        modules[1] = new Module(frModuleIO, 1, CompTunerConstants.FrontRight);
        modules[2] = new Module(blModuleIO, 2, CompTunerConstants.BackLeft);
        modules[3] = new Module(brModuleIO, 3, CompTunerConstants.BackRight);

        // Usage reporting for swerve template
        HAL.report(
                tResourceType.kResourceType_RobotDrive, tInstances.kRobotDriveSwerve_AdvantageKit);

        // Start the Odometry Thread
        PhoenixOdometryThread.getInstance().start();

        // Configure AutoBuilder for PathPlanner
        AutoBuilder.configure(
                this::getPose,
                this::resetPose,
                this::getChassisSpeeds,
                this::runVelocity,
                // TODO: tune pathfollowing constants
                new PPHolonomicDriveController(
                        new PIDConstants(7, 0.0, 0), new PIDConstants(5.0, 0.0, 0.0)),
                DriveConstants.PP_CONFIG,
                () -> DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red,
                this);
        // Pathplanner Logging
        PathPlannerLogging.setLogActivePathCallback(
                (activePath) -> {
                    Logger.recordOutput(
                            "Odometry/Trajectory",
                            activePath.toArray(new Pose2d[activePath.size()]));
                });

        PathPlannerLogging.setLogTargetPoseCallback(
                (targetPose) -> {
                    Logger.recordOutput("Odometry/TrajectorySetpoint", targetPose);
                });

        // TODO: Configure SysId
        sysId =
                new SysIdRoutine(
                        new SysIdRoutine.Config(
                                null,
                                null,
                                null,
                                (state) ->
                                        Logger.recordOutput("Drive/SysIdState", state.toString())),
                        new SysIdRoutine.Mechanism(
                                (voltage) -> runCharacterization(voltage.in(Volts)), null, this));
    }

    @Override
    public void periodic() {
        odometryLock.lock(); // Prevents odometry updates while reading data
        gyroIO.readInputs(gyroInputs);
        Logger.processInputs("Drive/Gyro", gyroInputs);

        for (var module : modules) {
            module.periodic();
        }
        odometryLock.unlock();
        RobotState.getInstance().incrementIterationCount();

        // Stop moving when disabled
        if (DriverStation.isDisabled()) {
            for (var module : modules) {
                module.stop();
            }
        }

        // Log empty setpoint states when disabled
        if (DriverStation.isDisabled()) {
            Logger.recordOutput("SwerveStates/Setpoints", new SwerveModuleState[] {});
            Logger.recordOutput("SwerveStates/SetpointsOptimized", new SwerveModuleState[] {});
        }
        // instantiate fields for drive measurements
        double rawAccelX = 0.0;
        double rawAccelY = 0.0;

        double rawRollVelocity = 0.0;
        double rawPitchVelocity = 0.0;

        double rawRoll = 0.0;
        double rawPitch = 0.0;

        // Update odometry
        double[] sampleTimestamps =
                modules[0].getOdometryTimestamps(); // All signals are sampled together
        int sampleCount = sampleTimestamps.length;
        for (int i = 0; i < sampleCount; i++) {
            // Read wheel positions and deltas from each module
            SwerveModulePosition[] modulePositions = new SwerveModulePosition[4];
            SwerveModulePosition[] moduleDeltas = new SwerveModulePosition[4];
            for (int moduleIndex = 0; moduleIndex < 4; moduleIndex++) {
                modulePositions[moduleIndex] = modules[moduleIndex].getOdometryPositions()[i];
                moduleDeltas[moduleIndex] =
                        new SwerveModulePosition(
                                modulePositions[moduleIndex].distanceMeters
                                        - lastModulePositions[moduleIndex].distanceMeters,
                                modulePositions[moduleIndex].angle);
                lastModulePositions[moduleIndex] = modulePositions[moduleIndex];
            }

            // Update gyro angle
            if (gyroInputs.connected) {
                // Use the real gyro angle
                rawYawRotation = gyroInputs.odometryYawPositions[i];

                // too lazy to update gyro sim so here's the solution
                if (Constants.currentMode == Mode.REAL) {
                    rawYawVelocity = gyroInputs.odometryYawVelocitys[i];
                    rawRollVelocity = gyroInputs.odometryRollVelocitys[i];
                    rawPitchVelocity = gyroInputs.odometryPitchVelocitys[i];

                    rawRoll = gyroInputs.odometryRollPositions[i].getRadians();
                    rawPitch = gyroInputs.odometryPitchPositions[i].getRadians();

                    rawAccelX = gyroInputs.odometryAccelXs[i];
                    rawAccelY = gyroInputs.odometryAccelYs[i];
                }
            } else {
                // Use the angle delta from the kinematics and module deltas
                Twist2d twist = kinematics.toTwist2d(moduleDeltas);
                rawYawRotation = rawYawRotation.plus(new Rotation2d(twist.dtheta));
            }
            // Apply update
            RobotState.getInstance()
                    .addOdometryMeasurement(
                            sampleTimestamps[i],
                            poseEstimator.updateWithTime(
                                    sampleTimestamps[i], rawYawRotation, modulePositions));

            ChassisSpeeds measuredRobotRelativeChassisSpeeds =
                    kinematics.toChassisSpeeds(swerveModulePositionToState(modulePositions));
            ChassisSpeeds measuredFieldRelativeChassisSpeeds =
                    ChassisSpeeds.fromRobotRelativeSpeeds(
                            measuredRobotRelativeChassisSpeeds, rawYawRotation);
            ChassisSpeeds desiredFieldRelativeChassisSpeeds =
                    ChassisSpeeds.fromRobotRelativeSpeeds(prePoofed, rawYawRotation);

            ChassisSpeeds fusedFieldRelativeChassisSpeeds =
                    new ChassisSpeeds(
                            measuredFieldRelativeChassisSpeeds.vxMetersPerSecond,
                            measuredFieldRelativeChassisSpeeds.vyMetersPerSecond,
                            rawYawVelocity);

            RobotState.getInstance()
                    .addDriveMotionMeasurements(
                            sampleTimestamps[i],
                            rawRollVelocity,
                            rawPitchVelocity,
                            rawYawVelocity,
                            rawPitch,
                            rawRoll,
                            rawAccelX,
                            rawAccelY,
                            prePoofed,
                            desiredFieldRelativeChassisSpeeds,
                            measuredRobotRelativeChassisSpeeds,
                            measuredFieldRelativeChassisSpeeds,
                            fusedFieldRelativeChassisSpeeds);
        }

        // Update gyro alert
        gyroDisconnectedAlert.set(!gyroInputs.connected && Constants.currentMode != Mode.SIM);

        telemtry.telemeterize(
                poseEstimator.getEstimatedPosition(),
                getModuleStates(),
                1. / DriveConstants.ODOMETRY_FREQUENCY);
    }

    @Override
    public void periodicAfterScheduler() {

        Logger.recordOutput(
                "Drive/currentCommand",
                (getCurrentCommand() == null) ? "Default" : getCurrentCommand().getName());
    }

    /**
     * Runs the drive at the desired velocity.
     *
     * @param speeds Speeds in meters/sec
     */
    public void runVelocity(ChassisSpeeds speeds) {
        Logger.recordOutput("SwerveStates/AutoSpeeds", speeds);
        // Calculate module setpoints
        speeds = ChassisSpeeds.discretize(speeds, Constants.kRealDt);
        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(
                setpointStates, CompTunerConstants.kSpeedAt12Volts);

        // Log unoptimized setpoints and setpoint speeds
        Logger.recordOutput("SwerveStates/Setpoints", setpointStates);
        Logger.recordOutput("SwerveChassisSpeeds/Setpoints", speeds);

        // Send setpoints to modules
        // for (int i = 0; i < 4; i++) {
        //     modules[i].runSetpoint(setpointStates[i]);
        // }

        // Log optimized setpoints (runSetpoint mutates each state)
        Logger.recordOutput("SwerveStates/SetpointsOptimized", setpointStates);
    }

    /** Runs the drive in a straight line with the specified drive output. */
    public void runCharacterization(double output) {
        for (int i = 0; i < 4; i++) {
            modules[i].runCharacterization(output);
        }
    }

    /** Stops the drive. */
    public void stop() {
        runVelocity(new ChassisSpeeds());
    }

    /**
     * Stops the drive and turns the modules to an X arrangement to resist movement. The modules
     * will return to their normal orientations the next time a nonzero velocity is requested.
     */
    public void stopWithX() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
            headings[i] = DriveConstants.getModuleTranslations()[i].getAngle();
        }
        kinematics.resetHeadings(headings);
        stop();
    }

    /** Returns a command to run a quasistatic test in the specified direction. */
    public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0))
                .withTimeout(1.0)
                .andThen(sysId.quasistatic(direction));
    }

    /** Returns a command to run a dynamic test in the specified direction. */
    public Command sysIdDynamic(SysIdRoutine.Direction direction) {
        return run(() -> runCharacterization(0.0))
                .withTimeout(1.0)
                .andThen(sysId.dynamic(direction));
    }

    /** Returns the module states (turn angles and drive velocities) for all of the modules. */
    @AutoLogOutput(key = "SwerveStates/Measured")
    private SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
            states[i] = modules[i].getState();
        }
        return states;
    }

    /** Returns the module positions (turn angles and drive positions) for all of the modules. */
    private SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] states = new SwerveModulePosition[4];
        for (int i = 0; i < 4; i++) {
            states[i] = modules[i].getPosition();
        }
        return states;
    }

    /** Returns the measured chassis speeds of the robot. */
    @AutoLogOutput(key = "SwerveChassisSpeeds/Measured")
    private ChassisSpeeds getChassisSpeeds() {
        return kinematics.toChassisSpeeds(getModuleStates());
    }

    /** Returns the position of each module in radians. */
    public double[] getWheelRadiusCharacterizationPositions() {
        double[] values = new double[4];
        for (int i = 0; i < 4; i++) {
            values[i] = modules[i].getWheelRadiusCharacterizationPosition();
        }
        return values;
    }

    /** Returns the average velocity of the modules in rotations/sec (Phoenix native units). */
    public double getFFCharacterizationVelocity() {
        double output = 0.0;
        for (int i = 0; i < 4; i++) {
            output += modules[i].getFFCharacterizationVelocity() / 4.0;
        }
        return output;
    }

    /** Returns the current odometry pose. */
    @AutoLogOutput(key = "Odometry/Robot")
    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    /** Returns the current odometry rotation. */
    public Rotation2d getRotation() {
        return getPose().getRotation();
    }

    /** Resets the current odometry pose. */
    public void resetPose(Pose2d pose) {
        poseEstimator.resetPosition(rawYawRotation, getModulePositions(), pose);
        if (Constants.currentMode == Mode.SIM) {
            RobotContainer.getInstance()
                    .getSimulatedRobotState()
                    .getSimDrive()
                    .setSimulationWorldPose(pose);
        }
    }

    /** Resets the current odometry pose. */
    public void resetPose() {
        resetPose(Pose2d.kZero);
    }

    /** Returns the maximum linear speed in meters per sec. */
    public double getMaxLinearSpeedMetersPerSec() {
        return CompTunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
    }

    /** Returns the maximum angular speed in radians per sec. */
    public double getMaxAngularSpeedRadPerSec() {
        return getMaxLinearSpeedMetersPerSec() / DriveConstants.DRIVE_BASE_RADIUS;
    }

    public void teleopResetRotation() {
        poseEstimator.resetPosition(
                rawYawRotation,
                getModulePositions(),
                new Pose2d(
                        getPose().getX(), getPose().getY(), Util.apply(Rotation2d.fromDegrees(0))));
    }

    public static SwerveModuleState[] swerveModulePositionToState(
            SwerveModulePosition... positions) {
        SwerveModuleState[] states = new SwerveModuleState[positions.length];
        for (int i = 0; i < positions.length; i++) {
            states[i] = new SwerveModuleState(positions[i].distanceMeters, positions[i].angle);
        }

        return states;
    }

    public void addVisionMeasurement(VisionFieldPoseEstimate estimate) {
        // TODO: Implement when vision is enabled
    }
}
