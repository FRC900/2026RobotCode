package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.hood.HoodSubsystem;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Supplier;

public class HoodFactory {

    private static RobotContainer getContainer() {
        return RobotContainer.getInstance();
    }

    // Sets the hood to a fixed position in radians
    public static Command setPositionMotionMagicCommand(double radians) {
        HoodSubsystem hood = getContainer().getHoodSubsystem();
        return hood.motionMagicSetpointCommand(() -> radians).withName("Hood Set Position");
    }

    public static Command aimHoodToPose(Supplier<ShooterSetpoint> setPointSupplier) {
        HoodSubsystem hood = getContainer().getHoodSubsystem();
        return Commands.run(
                        () ->
                                hood.setPositionRadians(
                                        setPointSupplier.get().getHoodRadians(),
                                        setPointSupplier.get().getHoodFF()),
                        hood)
                .withName("Aim Hood to Pose (rad)");
    }

    // Sets the hood to a fixed position and finishes when it arrives within the tolerance
    public static Command setPositionBlocking(double radians, double tolerance) {
        HoodSubsystem hood = getContainer().getHoodSubsystem();
        return hood.motionMagicSetpointCommandBlocking(() -> radians, tolerance)
                .withName("Hood Set Position Blocking");
    }

    // Stows the hood
    public static Command stow() {
        return setPositionMotionMagicCommand(HoodConstants.kHoodStowTrenchPositionRadians)
                .withName("Hood Stow");
    }
}
