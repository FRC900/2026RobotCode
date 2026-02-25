package com.team900.frc2026.subsystems.drive;

import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;

/** Simple container for holding CTRE swerve drivetrain creation constants. */
public class CommandSwerveDrivetrain {
    SwerveDrivetrainConstants driveTrainConstants;
    SwerveModuleConstants<?, ?, ?>[] moduleConstants;

    public CommandSwerveDrivetrain(
            SwerveDrivetrainConstants driveTrainConstants,
            SwerveModuleConstants<?, ?, ?>... modules) {
        this.driveTrainConstants = driveTrainConstants;
        this.moduleConstants = modules;
    }

    public SwerveDrivetrainConstants getDriveTrainConstants() {
        return driveTrainConstants;
    }

    public SwerveModuleConstants<?, ?, ?>[] getModuleConstants() {
        return moduleConstants;
    }
}
