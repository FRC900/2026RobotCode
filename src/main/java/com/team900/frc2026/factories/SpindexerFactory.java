package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.spindexer.SpindexerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerFactory {

    public static Command runSpindexer(RobotContainer container) {
        return container
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycle);
    }

    public static Command stopSpindexer(RobotContainer container) {
        return container.getSpindexerSubsystem().voltageCommand(() -> 0);
    }

    public static Command exhaustSpindexer(RobotContainer container) {
        return container
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycleExhaust);
    }
}
