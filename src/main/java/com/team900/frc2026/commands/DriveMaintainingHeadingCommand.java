package com.team900.frc2026.commands;

import com.team900.frc2026.Constants;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.drive.DriveConstants;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.lib.util.AllianceFlipUtil;
import com.team900.lib.util.FieldConstants;
import com.team900.lib.util.TurretAlignUtil;
import com.team900.lib.util.Util;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import lombok.Getter;
import lombok.Setter;
import org.littletonrobotics.junction.Logger;

public class DriveMaintainingHeadingCommand extends Command {
    public DriveMaintainingHeadingCommand(
            RobotContainer container,
            DoubleSupplier throttle,
            DoubleSupplier strafe,
            DoubleSupplier turn) {
        mRobotContainer = container;
        mDrivetrain = container.getDriveSubsystem();
        mThrottleSupplier = throttle;
        mStrafeSupplier = strafe;
        mTurnSupplier = turn;

        addRequirements(mDrivetrain);
        setName("Swerve Drive Maintain Heading");
    }

    private final RobotState mRobotState = RobotState.getInstance();
    private final RobotContainer mRobotContainer;
    protected DriveSubsystem mDrivetrain;

    private final DoubleSupplier mThrottleSupplier;
    private final DoubleSupplier mStrafeSupplier;
    private final DoubleSupplier mTurnSupplier;
    private Optional<Rotation2d> mHeadingSetpoint = Optional.empty();
    @Getter @Setter private boolean kAiming = false;
    private double mJoystickLastTouched = -1;

    private final PIDController thetaController =
            new PIDController(
                    DriveConstants.kHeadingControllerP,
                    DriveConstants.kHeadingControllerI,
                    DriveConstants.kHeadingControllerD);

    @Override
    public void initialize() {
        mHeadingSetpoint = Optional.empty();
        thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    @Override
    public void execute() {
        double throttle = mThrottleSupplier.getAsDouble() * DriveConstants.kDriveMaxSpeed * 0.8;
        double strafe = mStrafeSupplier.getAsDouble() * DriveConstants.kDriveMaxSpeed * 0.8;
        double turnFieldFrame =
                Util.handleDeadband(
                        mTurnSupplier.getAsDouble(),
                        DriveConstants.kDriveMaxAngularRate * Constants.kSteerJoystickDeadband);
        double throttleFieldFrame =
                Util.handleDeadband(
                        Util.shouldFlip() ? -throttle : throttle,
                        DriveConstants.kDriveMaxSpeed * 0.05);
        double strafeFieldFrame =
                Util.handleDeadband(
                        Util.shouldFlip() ? -strafe : strafe, DriveConstants.kDriveMaxSpeed * 0.05);
        if (Math.abs(turnFieldFrame) > Constants.kSteerJoystickDeadband) {
            mJoystickLastTouched = Timer.getFPGATimestamp();
        }
        if (Math.abs(turnFieldFrame) > Constants.kSteerJoystickDeadband
                || (Util.epsilonEquals(mJoystickLastTouched, Timer.getFPGATimestamp(), 0.25)
                        && Math.abs(
                                        mRobotState.getLatestRobotRelativeChassisSpeed()
                                                .omegaRadiansPerSecond)
                                > Math.toRadians(10))) {
            mDrivetrain.runVelocity(
                    ChassisSpeeds.fromFieldRelativeSpeeds(
                            throttleFieldFrame,
                            strafeFieldFrame,
                            turnFieldFrame * DriveConstants.kDriveMaxAngularRate,
                            mDrivetrain.getRotation()));
            mHeadingSetpoint = Optional.empty();
            Logger.recordOutput("DriveMaintainHeading/Mode", "NoHeading");
        } else {
            if (mHeadingSetpoint.isEmpty()) {
                mHeadingSetpoint =
                        Optional.of(mRobotState.getLatestFieldToRobot().getValue().getRotation());
            }

            Logger.recordOutput("DriveMaintainHeading/throttleFieldFrame", throttleFieldFrame);
            Logger.recordOutput("DriveMaintainHeading/strafeFieldFrame", strafeFieldFrame);
            Logger.recordOutput("DriveMaintainHeading/mHeadingSetpoint", mHeadingSetpoint.get());

            if (kAiming) {
                Pose2d robotPose = mRobotState.getLatestFieldToRobot().getValue();

                TurretAlignUtil aligner = new TurretAlignUtil(robotPose);
                Pose2d turretPose = aligner.getTurretPositionFromRobotPose();
                double turretX = turretPose.getX();
                double turretY = turretPose.getY();

                Translation2d hub =
                        AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();
                double angleRad = Math.atan2(hub.getY() - turretY, hub.getX() - turretX);

                mHeadingSetpoint = Optional.of(new Rotation2d(angleRad));

                mDrivetrain.runVelocity(
                        ChassisSpeeds.fromFieldRelativeSpeeds(
                                throttleFieldFrame,
                                strafeFieldFrame,
                                thetaController.calculate(
                                                mDrivetrain.getRotation().getRadians(),
                                                mHeadingSetpoint.get().getRadians())
                                        * DriveConstants.kDriveMaxAngularRate,
                                mDrivetrain.getRotation()));

                Logger.recordOutput("DriveMaintainHeading/Mode", "Heading");
                Logger.recordOutput(
                        "DriveMaintainHeading/HeadingSetpoint",
                        mHeadingSetpoint.get().getDegrees());
            } else {
                mDrivetrain.runVelocity(
                        ChassisSpeeds.fromFieldRelativeSpeeds(
                                throttleFieldFrame,
                                strafeFieldFrame,
                                thetaController.calculate(
                                                mDrivetrain.getRotation().getRadians(),
                                                mHeadingSetpoint.get().getRadians())
                                        * DriveConstants.kDriveMaxAngularRate,
                                mDrivetrain.getRotation()));

                Logger.recordOutput("DriveMaintainHeading/Mode", "Heading");
                Logger.recordOutput(
                        "DriveMaintainHeading/HeadingSetpoint",
                        mHeadingSetpoint.get().getDegrees());
            }
        }
    }

    // @Override
    // public boolean isFinished() {
    //     if (isNearTarget()) {
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }

    public boolean isNearTarget() {
        if (mHeadingSetpoint.isEmpty()) {
            return false;
        }

        boolean isNearHubTarget =
                MathUtil.isNear(
                        mHeadingSetpoint.get().getDegrees(),
                        mRobotContainer.getDriveSubsystem().getRotation().getDegrees(),
                        2.5);

        return isNearHubTarget;
    }
}
