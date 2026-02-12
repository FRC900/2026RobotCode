package com.team900.frc2026.factories;

import java.util.function.Supplier;

import com.team900.frc2026.RobotContainer;

import edu.wpi.first.wpilibj2.command.Command;

public class TurretFactory {
    public static Command aimTurretToPose(RobotContainer container, Supplier<ShooterSupplier> setpointSupplier) {
        Object turret = container.getTurret();
        return turret.positionSetpointCommand(() -> setpointSupplier.get().getTurretRadiansFromCenter(), 
                () -> setpointSupplier.get().getTurretFF().withName("Align Turret to Pose"));
    }
}
