package com.team900.frc2026.factories;

import edu.wpi.first.wpilibj2.command.Command;
import com.team900.frc2026.subsystems.intake.IntakePivotSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeRollerSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import com.team900.frc2026.subsystems.intake.IntakePivotConstants;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class IntakeFactory{

    private static final RobotContainer container = RobotContainer.getInstance();

    public static Command runIntake(RobotContainer container) {
        return container
            .getIntakeRollerSubsystem()
            .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycle)
            .withName("Duty Cycle Intake");
    }

    public static Command exhaustIntake(RobotContainer container) {
        return container
            .getIntakeRollerSubsystem()
            .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycleExhaust)
            .withName("Duty Cycle Exhaust");
    }

    public static Command extendIntake(RobotContainer container) {
        return container
            .getIntakePivotSubsystem()
            .motionMagicSetpointCommandBlocking(() -> 
                IntakePivotConstants.kIntakePivotDeployRadians,
                IntakePivotConstants.kIntakePivotToleranceRadians)
            .withName("Extending Slapdown");
    }

    public static Command retractIntake(RobotContainer container) {
        return container
            .getIntakePivotSubsystem()
            .motionMagicSetpointCommandBlocking(() -> 
                IntakePivotConstants.kIntakePivotStowRadians,
                IntakePivotConstants.kIntakePivotToleranceRadians)
            .withName("Retracting Slapdown");
    }
}