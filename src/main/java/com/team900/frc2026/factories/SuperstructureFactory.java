package com.team900.frc2026.factories;

import java.util.function.Supplier;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class SuperstructureFactory {

    public static Command aimAndShoot(RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        return null;
        /*Command to shoot all balls in robot (should auto aim)
         * Aim turret
         * Aim hood
         * Maintain shooter and handoff speed
         * Run spindexer
         */

    }

    public static Command aim(RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
       // return null;
        return new ParallelCommandGroup(
            TurretFactory.aimTurretToPose(setPointSupplier),
            HoodFactory.aimHoodToPose(container,setPointSupplier));        
        /*Command to aim at target
         * Aim turret
         * Aim hood
         */
    }
}
