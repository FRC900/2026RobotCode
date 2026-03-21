package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import java.util.function.Supplier;

import org.littletonrobotics.junction.AutoLogOutput;

public class ShootingFactory {
@AutoLogOutput
    public static boolean canShoot(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return MathUtil.isNear(
                        setPointSupplier.get().getShooterRPS(),
                        container.getShooterSubsystem().getCurrentVelocity(),
                        2)
                && MathUtil.isNear(
                        setPointSupplier.get().getHoodRadians() / (2.0 * Math.PI),
                        container.getHoodSubsystem().getCurrentPosition(),
                        0.003);
                // && container.getDriveCommand().isNearTarget();
    }

    public static Command shoot(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return Commands.sequence(
                Commands.parallel(
                                ShooterFactory.setShooterRPS(setPointSupplier, container),
                                SuperstructureFactory.aim(setPointSupplier, container)) .withTimeout(0.25)
                       ,
                Commands.parallel(
                        HandoffFactory.runHandoff(container),
                        SpindexerFactory.runSpindexer(container)));
    }

    public static Command manualShoot(RobotContainer container) {
        return Commands.sequence(
                Commands.parallel(
                                HoodFactory.setPosition(() -> HoodConstants.kHoodRotorMaxPosition - 0.01, container),
                                new InstantCommand(() -> container.getDriveCommand().setKAiming(true)),
                                ShooterFactory.setShooterRPS(ShooterConstants.kCloseShotRPS, container)).withTimeout(0.25)
                                        // .until(
                                        //         () ->
                                        //                 MathUtil.isNear(
                                        //                         ShooterConstants.kCloseShotRPS,
                                        //                         container.getShooterSubsystem()
                                        //                                 .getCurrentVelocity(),
                                        //                         5))
                                ,
                                        Commands.parallel(
                                                HandoffFactory.runHandoff(container),
                                                SpindexerFactory.runSpindexer(container)));
    }
}
