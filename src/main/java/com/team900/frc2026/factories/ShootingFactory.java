package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.turret.TurretSubsystem;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class ShootingFactory {

    public static Command shoot(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return Commands.sequence(Commands.parallel(
                                ShooterFactory.setShooterRPS(setPointSupplier, container),
                                SuperstructureFactory.aim(setPointSupplier, container))
                        .until(
                                () ->
                                        MathUtil.isNear(
                                                        setPointSupplier.get().getShooterRPS(),
                                                        container
                                                                .getShooterSubsystem()
                                                                .getCurrentVelocity(),
                                                        1)
                                                && MathUtil.isNear(
                                                        setPointSupplier.get().getHoodRadians(),
                                                        container
                                                                .getHoodSubsystem()
                                                                .getCurrentPosition(),
                                                        1)
                                                && MathUtil.isNear(
                                                        0,
                                                        RobotState.getInstance()
                                                                .getLatestRotationRobotToHub()
                                                                .getDegrees(),
                                                        3))
                                                //&& container.getTurretSubsystem().atSetpoint()))
                ,
                        Commands.parallel(
                                        IntakeFactory.runIntake(container),
                                        HandoffFactory.runHandoff(container),
                                        SpindexerFactory.runSpindexer(container))
                                .onlyWhile(container.getDriveCommand()::isNearTarget));
    }
}
