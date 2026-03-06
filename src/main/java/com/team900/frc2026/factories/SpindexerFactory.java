package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.spindexer.SpindexerConstants;
import edu.wpi.first.wpilibj2.command.Command;

public class SpindexerFactory {

    private static RobotContainer getContainer() {
        return RobotContainer.getInstance();
    }

    public static Command runSpindexer() {
        return getContainer()
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycle);
    }

    public static Command exhaustSpindexer() {
        return getContainer()
                .getSpindexerSubsystem()
                .dutyCycleCommand(() -> SpindexerConstants.kSpindexerDutyCycleExhaust);
    }
}
