package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class RollerFactory {
    static RobotContainer container = RobotContainer.getInstance();

    public static Command runIntake() {
        return container
                .getIntakeRollerSubsystem()
                .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycle);
    }

    public static Command exhaustIntake(RobotContainer container) {
        return container
                .getIntakeRollerSubsystem()
                .dutyCycleCommand(() -> IntakeRollerConstants.kIntakeDutyCycleExhaust);
    }
}
