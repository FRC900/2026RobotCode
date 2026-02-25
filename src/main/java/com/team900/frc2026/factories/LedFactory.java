package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.Led.LedConstants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.DoubleSupplier;

public class LedFactory {
    private static final double kLowBattery = LedConstants.kLowBatteryThresholdVolts;

    public static Command updateLEDs(RobotContainer container) {
        return Commands.none();
    }

    //Ppl preferred to have status lights so all commands are for prematch use
    //ie imu zeroed or battery

    public static Command battery(RobotContainer container, DoubleSupplier voltageSupplier) {
        return container.getLeds().commandSolidColor(
            () -> {
                if(voltageSupplier.getAsDouble()<kLowBattery){
                    return LedState.kBad;
                } else {
                    return LedState.kGood;
                }
            }
        );
    }

}
