package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.intake.IntakePivotConstants;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class IntakeFactory {

    private static RobotContainer getContainer() {
        return RobotContainer.getInstance();
    }

    public static Command runIntake() {
        return getContainer()
                .getIntakeRollerSubsystem()
                .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycle);
    }

    public static Command exhaustIntake() {
        return getContainer()
                .getIntakeRollerSubsystem()
                .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycleExhaust);
    }

    public static Command deploySlapdown() {
        return getContainer()
                .getIntakePivotSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> IntakePivotConstants.kIntakePivotDeployRadians,
                        IntakePivotConstants.kIntakePivotToleranceRadians);
    }

    public static Command retractSlapdown() {
        return getContainer()
                .getIntakePivotSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> IntakePivotConstants.kIntakePivotStowRadians,
                        IntakePivotConstants.kIntakePivotToleranceRadians);
    }
}
