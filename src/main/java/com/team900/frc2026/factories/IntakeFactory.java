package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.intake.IntakePivotConstants;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeFactory {

    public static Command runIntake(RobotContainer container) {
        return container
                .getIntakeRollerSubsystem()
                .voltageCommand(() -> IntakeRollerConstants.kIntakeDutyCycle);
    }

    public static Command exhaustIntake(RobotContainer container) {
        return container
                .getIntakeRollerSubsystem()
                .voltageCommand(() -> IntakeRollerConstants.kIntakeDutyCycleExhaust);
    }

    public static Command stopIntake(RobotContainer container) {
        return container.getIntakeRollerSubsystem().voltageCommand(() -> 0);
    }

    public static Command deploySlapdown(RobotContainer container) {
        return container
                .getIntakePivotSubsystem()
                // .motionMagicSetpointCommandBlocking(
                //         () -> IntakePivotConstants.kIntakePivotDeploy, 1, 1)
                // .withTimeout(2);
                .positionSetpointCommand(() -> IntakePivotConstants.kIntakePivotDeploy);
    }

    public static Command retractSlapdown(RobotContainer container) {
        return container
                .getIntakePivotSubsystem()
                // .motionMagicSetpointCommandBlocking(
                //         () -> IntakePivotConstants.kIntakePivotStow, 1, 1)
                // .withTimeout(3);
                .positionSetpointCommand(() -> IntakePivotConstants.kIntakePivotStow);
    }
}
