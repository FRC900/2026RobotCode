package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.Hood.HoodSubsystem;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public class HoodFactory {
    public static Command pointHoodToPose(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        HoodSubsystem hood = container.getHood();

        Supplier<Double> radSupplier = new Supplier<>() {
            @Override
            public Double get() {
                return setpointSupplier.get().hoodAngleRadians; // double → Double
            }
        };

        return hood.angleCommand(radSupplier)
                .withName("Point Hood to Pose (rad)");
    }

    public static Command pointHoodToPoseDegrees(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        HoodSubsystem hood = container.getHood();

        Supplier<Double> degSupplier = new Supplier<>() {
            @Override
            public Double get() {
                return setpointSupplier.get().hoodAngleDegrees; // double → Double
            }
        };

        return hood.angleDegreesCommand(degSupplier)
                .withName("Point Hood to Pose (deg)");
    }
}
