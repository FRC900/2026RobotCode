package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.spindexer.SpindexerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerFactory {
    static RobotContainer container = RobotContainer.getInstance();

    public static Command runSpindexer() {
        return container
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycle);
    }

    public static Command exhaustSpindexer() {
        return container
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycleExhaust);
    }
}
