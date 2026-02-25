package com.team900.frc2026.subsystems.drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import org.littletonrobotics.junction.Logger;

/**
 * Sim implementation of DriveIO. Extends DriveIOHardware and adds a Notifier that calls CTRE's
 * built-in updateSimState() at 200 Hz to run the physics simulation.
 */
public class DriveIOSim extends DriveIOHardware {

    private static final double kSimUpdatePeriodSec = 0.005; // 200 Hz
    private final Notifier simNotifier;

    public DriveIOSim(
            com.ctre.phoenix6.swerve.SwerveDrivetrainConstants driveTrainConstants,
            com.ctre.phoenix6.swerve.SwerveModuleConstants<?, ?, ?>... modules) {
        super(driveTrainConstants, modules);

        simNotifier =
                new Notifier(
                        () -> {
                            updateSimState(
                                    kSimUpdatePeriodSec, RobotController.getBatteryVoltage());
                        });
        simNotifier.startPeriodic(kSimUpdatePeriodSec);
    }

    @Override
    public void readInputs(DriveIOInputs inputs) {
        super.readInputs(inputs);
        Logger.recordOutput("Drive/SimPose", inputs.Pose);
    }
}
