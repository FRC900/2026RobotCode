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

    public static Command stopIntake(RobotContainer container) {
        return container.getIntakeRollerSubsystem().dutyCycleCommand(() -> 0);
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
                .motionMagicSetpointCommand(
                        () -> IntakePivotConstants.kIntakePivotDeploy,
                        () -> IntakePivotConstants.kIntakePivotMotionMagicConfigs);
    }

    public static Command retractSlapdown(RobotContainer container) {
        return container
                .getIntakePivotSubsystem()
                .motionMagicSetpointCommand(
                        () -> IntakePivotConstants.kIntakePivotStow,
                        () -> IntakePivotConstants.kIntakePivotMotionMagicConfigs);
    }
}
