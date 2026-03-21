package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.hood.HoodSubsystem;
import com.team900.lib.util.ShooterSetpoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class HoodFactory {

    public static Command aimHoodToPose(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        HoodSubsystem hood = container.getHoodSubsystem();
        return Commands.run(
                        () -> {
                            hood.setPositionRadians(
                                    Units.radiansToRotations(
                                            setPointSupplier.get().getHoodRadians()),
                                    setPointSupplier.get().getHoodFF());
                            Logger.recordOutput(
                                    "setpointfeafeahfewa", setPointSupplier.get().getHoodRadians());
                        },
                        hood)
                .withName("Aim Hood to Pose (rad)");
    }

    // Sets the hood to a fixed position and finishes when it arrives within the tolerance
    public static Command setPosition(DoubleSupplier value, RobotContainer container) {
           return Commands.run(
                        () -> {
                            container.getHoodSubsystem().setPositionRadians(value.getAsDouble(), 0);
                        },
                        container.getHoodSubsystem()).until(() -> MathUtil.isNear(value.getAsDouble(), RobotState.getInstance().getHoodRotations(), 0.003))
                .withName("Aim Hood to Pose (rad)");
    }

    // Stows the hood
    public static Command stow(RobotContainer container) {
        return container
                .getHoodSubsystem()
                .positionSetpointUntilOnTargetCommand(() -> 0.2, () -> 0.01)
                .withName("Hood Stow");
    }

    //     public static Command zero(RobotContainer container) {
    //         return Commands.sequence(
    //
    // container.getHoodSubsystem().runOnce(container.getHoodSubsystem()::disableSoftLimits),
    //                 container
    //                         .getHoodSubsystem()
    //                         .dutyCycleCommand(() -> 0.05)
    //                         .until(RobotState.getInstance()::getHoodHasZeroed),
    //                container.getHoodSubsystem().runOnce(
    //                        () -> container.getHoodSubsystem()
    //                         .setCurrentPosition(HoodConstants.kHoodRotorMinPosition)),

    //
    // container.getHoodSubsystem().run(container.getHoodSubsystem()::enableSoftLimits)
    //         ).withName("Zero Hood");
    //     }

    public static Command zero(RobotContainer container) {
        return Commands.sequence(
                        container
                                .getHoodSubsystem()
                                .runOnce(
                                        () -> {
                                            RobotState.getInstance().resetHoodHasZero();
                                            container.getHoodSubsystem().disableSoftLimits();
                                        }),
                        container
                                .getHoodSubsystem()
                                .dutyCycleCommand(() -> 0.05)
                                .until(RobotState.getInstance()::getHoodHasZeroed),
                        container
                                .getHoodSubsystem()
                                .runOnce(
                                        () ->
                                                container
                                                        .getHoodSubsystem()
                                                        .setCurrentPosition(
                                                                Units.degreesToRotations(75))),
                        container
                                .getHoodSubsystem()
                                .runOnce(container.getHoodSubsystem()::enableSoftLimits))
                .withName("Zero Hood");
    }

    public static Command shoot(RobotContainer container, double tolerance) {
        return container
                .getHoodSubsystem()
                .positionSetpointUntilOnTargetCommand(
                        () -> HoodConstants.kHoodRotorMaxPosition * 0.5, () -> tolerance)
                .withName("Hood Shoot Position Blocking");
    }

    public static boolean isHoodStowed(RobotContainer container) {
        return container.getHoodSubsystem().isStowed();
    }
}
