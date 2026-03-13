package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.hood.HoodSubsystem;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import java.util.function.Supplier;

public class HoodFactory {

    // Sets the hood to a fixed position in radians
    public static Command setPositionMotionMagicCommand(double radians, RobotContainer container) {
        HoodSubsystem hood = container.getHoodSubsystem();
        return hood.motionMagicSetpointCommand(() -> radians).withName("Hood Set Position");
    }

    public static Command aimHoodToPose(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        HoodSubsystem hood = container.getHoodSubsystem();
        return Commands.run(
                        () ->
                                hood.setPositionRadians(
                                        setPointSupplier.get().getHoodRadians(),
                                        setPointSupplier.get().getHoodFF()),
                        hood)
                .withName("Aim Hood to Pose (rad)");
    }

    // Sets the hood to a fixed position and finishes when it arrives within the tolerance
    public static Command setPositionBlocking(
            double radians, double tolerance, RobotContainer container) {
        return container
                .getHoodSubsystem()
                .motionMagicSetpointCommandBlocking(() -> radians, tolerance)
                .withName("Hood Set Position Blocking");
    }

    // Stows the hood
    public static Command stow(RobotContainer container) {
        return setPositionMotionMagicCommand(
                        HoodConstants.kHoodStowTrenchPositionRadians, container)
                .withName("Hood Stow");
    }

    public static Command zero(RobotContainer container) {
        return new InstantCommand(container.getHoodSubsystem()::disableSoftLimits)
                .andThen(
                        container
                                .getHoodSubsystem()
                                .dutyCycleCommand(() -> -0.05)
                                .until(RobotState.getInstance()::getHoodHasZeroed)
                                .andThen(container.getHoodSubsystem()::enableSoftLimits));
    }

    // TODO: tune these positions on the real robot
    public static Command pass(RobotContainer container, double tolerance) {
        return container
                .getHoodSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> HoodConstants.kHoodRotorMaxPosition, tolerance)
                .withName("Hood pass Position Blocking");
    }

    public static Command shoot(RobotContainer container, double tolerance) {
        return container
                .getHoodSubsystem()
                .motionMagicSetpointCommandBlocking(
                        () -> HoodConstants.kHoodRotorMaxPosition * 0.5, tolerance)
                .withName("Hood Shoot Position Blocking");
    }
}
