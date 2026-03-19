package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.turret.TurretConstants;
import com.team900.lib.util.ShooterSetpoint;
import com.team900.lib.util.Util;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;

public class AimFactory {
    public static Command alignSuperstructure(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        return Commands.parallel(
                alignHoodAndTurret(container, setpointSupplier),
                ShooterFactory.setShooterRPS(container,
                        setpointSupplier))
                .withName("Align Superstructure");
    }

    public static Command alignHoodAndTurret(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        return Commands.parallel(
                TurretFactory.aimTurretToPose(container, setpointSupplier),
                HoodFactory.aimHoodToPose(container, setpointSupplier))
                .withName("Align Hood and Turret");
    }

    public static Command alignUntilOnTarget(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        return alignSuperstructure(container, setpointSupplier)
                .until(() -> onTarget(container,
                        setpointSupplier))
                .withName("Align Until On Target");
    }

    public static boolean onTarget(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        var turret = container.getTurretSubsystem();
        var hood = container.getHoodSubsystem();
        var shooterStage2 = container.getShooterStage2Subsystem();
        double shooterSetpoint = setpointSupplier.get().getShooterRPS();
        boolean shooterOnTarget = Util.epsilonEquals(shooterStage2.getCurrentVelocity(), shooterSetpoint,
                shooterSetpoint * 0.04);
        boolean turretOnTarget = Math.abs(new Rotation2d(turret.getCurrentPosition()).rotateBy(
                new Rotation2d(setpointSupplier.get().getTurretRadiansFromCenter()).unaryMinus())
                .getRadians()) < TurretConstants.kTurretShootingEpsilon;
        boolean hoodOnTarget = Util.epsilonEquals(hood.getCurrentPosition(), setpointSupplier.get().getHoodRadians(),
                HoodConstants.kHoodShootingEpsilon);
        boolean isValid = setpointSupplier.get().getIsValid();
        Logger.recordOutput("Shooter/IsValid", isValid);
        Logger.recordOutput("Shooter/OnTarget", shooterOnTarget);
        Logger.recordOutput("Turret/OnTarget", turretOnTarget);
        Logger.recordOutput("Hood/OnTarget", hoodOnTarget);
        return hoodOnTarget && shooterOnTarget && turretOnTarget && isValid;
    }

}