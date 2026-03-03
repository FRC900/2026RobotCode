package com.team900.frc2026.factories;

import edu.wpi.first.wpilibj2.command.Command;
import com.team900.frc2026.subsystems.intake.IntakePivotSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeRollerSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeConstants;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class IntakeFactory{

    private static final RobotContainer container = RobotContainer.getInstance();

    public static Command runIntake(RobotContainer container) {
        return container
            .getIntakeRollerSubsystem()
            .dutyCycleCommand(() -> IntakeConstants.kIntakeDutyCycleIntake)
            .withName("Duty Cycle Intake");
    }

    public static Command exhaustIntake(RobotContainer container) {
        return container
            .getIntakeRollerSubsystem()
            .dutyCycleCommand(() -> IntakeConstants.kIntakeDutyCycleExhaust)
            .withName("Duty Cycle Outtake");
    }

    public static Command extendIntake(RobotContainer container) {
        return container
            .getIntakePivotSubsystem()
            .motionMagicSetpointCommandBlocking(() -> 
                IntakeConstants.kIntakePivotDeployPositionRadians,
                IntakeConstants.kIntakePivotToleranceRadians)
            .withName("Extending Slapdown");
    }

    public static Command retractIntake(RobotContainer container) {
        return container
            .getIntakePivotSubsystem()
            .motionMagicSetpointCommandBlocking(() -> 
                IntakeConstants.kIntakePivotStowPositionRadians,
                IntakeConstants.kIntakePivotToleranceRadians)
            .withName("Retracting Slapdown");
    }
}