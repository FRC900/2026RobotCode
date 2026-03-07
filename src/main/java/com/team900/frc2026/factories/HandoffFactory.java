package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.handoff.HandoffConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class HandoffFactory {

    public static Command runHandoff(RobotContainer container) {
        return container
                .getHandoffSubsystem()
                .dutyCycleCommand(() -> HandoffConstants.kHandoffDutyCycle);
    }

    public static Command stopHandoff(RobotContainer container) {
        return container.getHandoffSubsystem().dutyCycleCommand(() -> 0);
    }

    public static Command exhaustHandoff(RobotContainer container) {
        return container
                .getHandoffSubsystem()
                .dutyCycleCommand(() -> HandoffConstants.kHandoffDutyCycleExhaust);
    }
}
