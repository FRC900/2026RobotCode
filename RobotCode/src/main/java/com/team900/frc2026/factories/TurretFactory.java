package com.team900.frc2026.factories;

import java.util.function.Supplier;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.Turret.TurretSubsystem;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class TurretFactory {
    public static Command aimTurretToPose(RobotContainer container, Supplier<ShooterSupplier> setpointSupplier) {
        TurretSubsystem turret = container.getTurret();
        return Commands.run(
            () -> turret.setPositionRadians(
                setpointSupplier.get().getTurretRadiansFromCenter(), 
                setpointSupplier.get().getTurretFF().withName("Align Turret to Pose")
                ),
                turret
            ).withName("Align Turret to Pose");;
    } 
}
