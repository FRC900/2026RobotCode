package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.hood.HoodSubsystem;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class HoodFactory {

    public static Command aimHoodToPose(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        HoodSubsystem hood = container.getHoodSubsystem();
        return Commands.run(
                        () ->
                                hood.setPositionRadians(
                                        setPointSupplier.get().getHoodRadians() / (2. * Math.PI),
                                        setPointSupplier.get().getHoodFF()),
                        hood)
                .withName("Aim Hood to Pose (rad)");
    }

    // Sets the hood to a fixed position and continuously holds it
    public static Command setPosition(DoubleSupplier radians, RobotContainer container) {
        return container
                .getHoodSubsystem()
                .positionSetpointCommand(radians)
                .withName("Hood Set Position Blocking");
    }

    // Stows the hood
    public static Command stow(RobotContainer container) {
        return container
                .getHoodSubsystem()
                .positionSetpointUntilOnTargetCommand(() -> 0.0756, () -> 0.01)
                .withName("Hood Stow");
    }

    public static Command zero(RobotContainer container) {
        return new InstantCommand(container.getHoodSubsystem()::disableSoftLimits)
                .andThen(
                        container
                                .getHoodSubsystem()
                                .dutyCycleCommand(() -> -0.05)
                                .until(RobotState.getInstance()::getHoodHasZeroed)
                                .andThen(container.getHoodSubsystem()::enableSoftLimits))
                .andThen(
                        Commands.runOnce(
                                () ->
                                        container
                                                .getHoodSubsystem()
                                                .setCurrentPosition(
                                                        HoodConstants.kHoodMinPositionRadians),
                                container.getHoodSubsystem()));
    }
}
