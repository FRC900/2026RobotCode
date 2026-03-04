package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.intake.IntakePivotConstants;

import edu.wpi.first.wpilibj2.command.Command;

public class PivotFactory {
        static RobotContainer container = RobotContainer.getInstance();


     public static Command deploySlapdown(RobotContainer container) {
        return container
                .getIntakePivotSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> IntakePivotConstants.kIntakePivotDeployRadians,
                        IntakePivotConstants.kIntakePivotToleranceRadians);
    }

    public static Command retractSlapdown(RobotContainer container) {
        return container
                .getIntakePivotSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> IntakePivotConstants.kIntakePivotStowRadians,
                        IntakePivotConstants.kIntakePivotToleranceRadians);
    }
    
}
