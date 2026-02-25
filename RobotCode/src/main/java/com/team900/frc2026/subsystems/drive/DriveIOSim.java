package com.team900.frc2026.subsystems.drive;

import edu.wpi.first.wpilibj.RobotController;
import org.littletonrobotics.junction.Logger;

/**
 * Sim implementation of DriveIO. Exposes simulationPeriodic() to be called from the robot's
 * simulationPeriodic(), which runs CTRE's built-in physics simulation.
 */
public class DriveIOSim extends DriveIOHardware {

    public DriveIOSim(
            com.ctre.phoenix6.swerve.SwerveDrivetrainConstants driveTrainConstants,
            com.ctre.phoenix6.swerve.SwerveModuleConstants<?, ?, ?>... modules) {
        super(driveTrainConstants, modules);

        // Lower odometry thread priority in sim to avoid thread contention
        this.getOdometryThread().setThreadPriority(1);
    }

    /** Must be called from Robot.simulationPeriodic() to drive the CTRE physics sim. */
    public void simulationPeriodic() {
        updateSimState(0.020, RobotController.getBatteryVoltage());
    }

    @Override
    public void readInputs(DriveIOInputs inputs) {
        super.readInputs(inputs);
        Logger.recordOutput("Drive/SimPose", inputs.Pose);
    }
}
