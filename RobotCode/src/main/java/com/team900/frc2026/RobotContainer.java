package com.team900.frc2026;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.team900.frc2026.subsystems.drive.DriveIOHardware;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
    // Subsystems
    private final DriveSubsystem drive;

    // Controllers
    private final CommandXboxController driverController =
            new CommandXboxController(Constants.kDriverControllerPort);

    // Swerve requests
    private final SwerveRequest.FieldCentric fieldCentricDrive =
            new SwerveRequest.FieldCentric()
                    .withDriveRequestType(DriveRequestType.OpenLoopVoltage)
                    .withSteerRequestType(SteerRequestType.MotionMagicExpo)
                    .withDeadband(Constants.DriveConstants.kDriveMaxSpeed * Constants.kJoystickDeadband)
                    .withRotationalDeadband(
                            Constants.DriveConstants.kDriveMaxAngularRate
                                    * Constants.kJoystickDeadband);

    private final SwerveRequest.SwerveDriveBrake brakeRequest = new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt pointRequest = new SwerveRequest.PointWheelsAt();

    public RobotContainer() {
        drive = buildDriveSubsystem();
        configureDefaultCommands();
        configureBindings();
    }

    private DriveSubsystem buildDriveSubsystem() {
        return new DriveSubsystem(
                new DriveIOHardware(
                        Constants.DriveConstants.kDrivetrain.getDriveTrainConstants(),
                        Constants.DriveConstants.kDrivetrain.getModuleConstants()));
    }

    private void configureDefaultCommands() {
        // Field-centric: left stick - translation, right stick X - rotation
        drive.setDefaultCommand(drive.applyRequest(() -> fieldCentricDrive
                                        .withVelocityX(-driverController.getLeftY() * Constants.DriveConstants.kDriveMaxSpeed)
                                        .withVelocityY(-driverController.getLeftX() * Constants.DriveConstants.kDriveMaxSpeed)
                                        .withRotationalRate(-driverController.getRightX() * Constants.DriveConstants.kDriveMaxAngularRate)));
    }

    private void configureBindings() {
        // // A button: brake (X-pattern)
        // driverController.a().whileTrue(drive.applyRequest(() -> brakeRequest));

        // // B button: point wheels at current right stick direction
        // driverController.b().whileTrue(drive.applyRequest(() -> pointRequest.withModuleDirection(
        //         new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))));

        // Start button: reset gyro heading (zero the field-centric forward direction)
        driverController.start().onTrue(Commands.runOnce(
                                        () ->drive.resetOdometry(
                                                new edu.wpi.first.math.geometry.Pose2d(
                                                        drive.getPose().getTranslation(),
                                                        new Rotation2d())))
                                                .ignoringDisable(true));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
