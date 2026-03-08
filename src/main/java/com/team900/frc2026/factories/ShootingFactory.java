package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class ShootingFactory {

    public static Command shoot(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return (new ParallelCommandGroup(
                                ShooterFactory.setShooterRPS(setPointSupplier, container),
                                SuperstructureFactory.aim(setPointSupplier, container),
                                IntakeFactory.deploySlapdown(container))
                        .until(
                                () ->
                                        MathUtil.isNear(
                                                setPointSupplier.get().getShooterRPS(),
                                                container
                                                        .getShooterSubsystem()
                                                        .getCurrentVelocity(),
                                                1)))
                .andThen(
                        new ParallelCommandGroup(
                                        IntakeFactory.runIntake(container),
                                        HandoffFactory.runHandoff(container),
                                        SpindexerFactory.runSpindexer(container))
                                .onlyWhile(container.getDriveCommand()::isNearTarget));
    }
}
