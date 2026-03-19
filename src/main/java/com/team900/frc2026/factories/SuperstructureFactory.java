package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class SuperstructureFactory {

    public static Command shoot(
            RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
            return Commands.parallel(TurretFactory.aimTurretToPose(container, setPointSupplier),
            
            HoodFactory.aimHoodToPose(container, setPointSupplier)
            , SpindexerFactory.runSpindexer(container), 
            ShooterFactory.runHandoff(container),
            ShooterFactory.setShooterRPS(container, setPointSupplier)
            ).withName("Shoot");
    }

     public static Command spinAndShoot(RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        return AimFactory.alignUntilOnTarget(container, setpointSupplier)
                .andThen(shoot(container, setpointSupplier))
                .withName("Align then Shoot");
    }

    public static Command stowHoodandIntake(RobotContainer container) {
        return new ParallelCommandGroup(HoodFactory.stow(container), IntakeFactory.retractSlapdown(container));
    }

    public static Command trench(RobotContainer container) {
        return new ParallelCommandGroup(
                HoodFactory.stow(container), IntakeFactory.deploySlapdown(container));
    }
}
