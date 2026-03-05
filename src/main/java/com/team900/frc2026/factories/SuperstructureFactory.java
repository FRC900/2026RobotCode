package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class SuperstructureFactory {

    public static Command aimAndShoot(
            RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        return new ParallelCommandGroup(
                TurretFactory.aimTurretToPose(setPointSupplier),
                HoodFactory.aimHoodToPose(setPointSupplier),
                SpindexerFactory.runSpindexer(),
                HandoffFactory.runHandoff(),
                ShooterFactory.setShooterRPS(setPointSupplier));
        /*Command to shoot all balls in robot (should auto aim)
         * Aim turret*
         * Aim hood*
         * Run handoff, shooter, and spindexer as appropriate**/
    }

    public static Command aim(
            RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        // return null;
        return new ParallelCommandGroup(
                TurretFactory.aimTurretToPose(setPointSupplier),
                HoodFactory.aimHoodToPose(setPointSupplier));
        /*Command to aim at target
         * Aim turret
         * Aim hood */
    }
}
