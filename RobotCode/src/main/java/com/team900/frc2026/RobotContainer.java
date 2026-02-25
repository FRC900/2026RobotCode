package com.team900.frc2026;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.team900.frc2026.subsystems.drive.DriveIO;
import com.team900.frc2026.subsystems.drive.DriveIOHardware;
import com.team900.frc2026.subsystems.drive.DriveIOSim;
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
                    .withDeadband(
                            Constants.DriveConstants.kDriveMaxSpeed * Constants.kJoystickDeadband)
                    .withRotationalDeadband(
                            Constants.DriveConstants.kDriveMaxAngularRate
                                    * Constants.kJoystickDeadband);

    private final SwerveRequest.SwerveDriveBrake brakeRequest =
            new SwerveRequest.SwerveDriveBrake();

    private final SwerveRequest.PointWheelsAt pointRequest = new SwerveRequest.PointWheelsAt();

    public RobotContainer() {
        drive = buildDriveSubsystem();
        configureDefaultCommands();
        configureBindings();
    }

    private DriveSubsystem buildDriveSubsystem() {
        DriveIO io =
                switch (Constants.currentMode) {
                    case REAL -> new DriveIOHardware(
                            Constants.DriveConstants.kDrivetrain.getDriveTrainConstants(),
                            Constants.DriveConstants.kDrivetrain.getModuleConstants());
                    case SIM -> new DriveIOSim(
                            Constants.DriveConstants.kDrivetrain.getDriveTrainConstants(),
                            Constants.DriveConstants.kDrivetrain.getModuleConstants());
                    case REPLAY -> throw new UnsupportedOperationException(
                            "REPLAY mode not yet implemented");
                };
        return new DriveSubsystem(io);
    }

    /** Deadband & linear rescaling */
    private static double deadbandRescale(double value, double deadband) {
        if (Math.abs(value) < deadband) {
            return 0.0;
        }
        return Math.signum(value) * (Math.abs(value) - deadband) / (1.0 - deadband);
    }

    /**
     * Shape translation inputs using circular magnitude processing. Treats X/Y as a vector,
     * prevents diagonal speed from being sqrt2× faster. Allows uniform speed in all directions
     */
    private static double[] shapeTranslation(double rawX, double rawY) {
        double magnitude = Math.hypot(rawX, rawY);
        if (magnitude < Constants.kJoystickDeadband) {
            return new double[] {0.0, 0.0};
        }

        double rescaled = deadbandRescale(Math.min(magnitude, 1.0), Constants.kJoystickDeadband);
        double shaped = Math.pow(rescaled, Constants.kTranslationExponent);

        double scale = shaped / magnitude;
        return new double[] {rawX * scale, rawY * scale};
    }

    private static double shapeRotation(double raw) {
        double rescaled = deadbandRescale(raw, Constants.kJoystickDeadband);
        return Math.signum(rescaled) * Math.pow(Math.abs(rescaled), Constants.kRotationExponent);
    }

    private void configureDefaultCommands() {
        // Field-centric: left stick - translation, right stick X - rotation
        drive.setDefaultCommand(
                drive.applyRequest(
                        () -> {
                            double[] translation =
                                    shapeTranslation(
                                            -driverController.getLeftY(),
                                            -driverController.getLeftX());

                            double rotation = shapeRotation(-driverController.getRightX());

                            // // Slow mode: left trigger proportionally reduces speed
                            // double slowMultiplier =
                            //         1.0 - (driverController.getLeftTriggerAxis() *
                            // Constants.kSlowModeScalar);

                            return fieldCentricDrive
                                    .withVelocityX(
                                            translation[0]
                                                    * Constants.DriveConstants.kDriveMaxSpeed)
                                    .withVelocityY(
                                            translation[1]
                                                    * Constants.DriveConstants.kDriveMaxSpeed)
                                    .withRotationalRate(
                                            rotation
                                                    * Constants.DriveConstants
                                                            .kDriveMaxAngularRate);
                        }));
    }

    private void configureBindings() {
        // // A button: brake (X-pattern)
        // driverController.a().whileTrue(drive.applyRequest(() -> brakeRequest));

        // // B button: point wheels at current right stick direction
        // driverController.b().whileTrue(drive.applyRequest(() -> pointRequest.withModuleDirection(
        //         new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))));

        // Start button: reset gyro heading (zero the field-centric forward direction)
        driverController
                .start()
                .onTrue(
                        Commands.runOnce(
                                        () ->
                                                drive.resetOdometry(
                                                        new edu.wpi.first.math.geometry.Pose2d(
                                                                drive.getPose().getTranslation(),
                                                                new Rotation2d())))
                                .ignoringDisable(true));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
