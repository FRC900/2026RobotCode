package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class SuperstructureFactory {

    public static Command aim(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return Commands.parallel(
                HoodFactory.aimHoodToPose(setPointSupplier, container),
                new InstantCommand(() -> container.getDriveCommand().setKAiming(true)));
        /*Command to aim at target
         * Aim turret
         * Aim hood */
    }

    public static Command aimWTurret(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        return Commands.parallel(
                HoodFactory.aimHoodToPose(setPointSupplier, container),
                TurretFactory.aimTurretToPose(setPointSupplier));
        /*Command to aim at target
         * Aim turret
         * Aim hood */
    }

    public static Command stow(RobotContainer container) {
        return new ParallelCommandGroup(HoodFactory.stow(container));
    }

    public static Command trench(RobotContainer container) {
        return new ParallelCommandGroup(
                HoodFactory.stow(container), IntakeFactory.deploySlapdown(container));
    }

    public static Command intakeDeploy(RobotContainer container) {

        return IntakeFactory.deploySlapdown(container);
    }
}
