package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.Hood.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public class HoodFactory {
    public static Command pointHoodToPose(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        HoodSubsystem hood = container.getHood();
        return hood.angleCommand(() -> setpointSupplier.get().hoodAngleRadians);
    }
}
