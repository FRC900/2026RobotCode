package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.turret.TurretSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Supplier;

public class TurretFactory {

    // Continuously aims the turret radians
    public static Command aimTurretToPoseRadians(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(
                        () ->
                                turret.setPositionRadians(
                                        setpointSupplier.get().getTurretRadiansFromCenter(),
                                        setpointSupplier.get().getTurretFF()),
                        turret)
                .withName("Aim Turret to Pose (rad)");
    }

    // Continuously aims the turret degrees
    public static Command aimTurretToPoseDegrees(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(
                        () ->
                                turret.setPositionDegrees(
                                        setpointSupplier.get().getTurretDegreesFromCenter(),
                                        setpointSupplier.get().getTurretFFDegrees()),
                        turret)
                .withName("Aim Turret to Pose (deg)");
    }

    // Goes to a fixed position in radians, then finishes
    public static Command setPositionRadians(RobotContainer container, double radians) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(() -> turret.setPositionRadians(radians), turret)
                .until(turret::atSetpoint)
                .withName("Turret Set Position (rad)");
    }

    // Goes to a fixed position in degrees, then finishes
    public static Command setPositionDegrees(RobotContainer container, double degrees) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(() -> turret.setPositionDegrees(degrees), turret)
                .until(turret::atSetpoint)
                .withName("Turret Set Position (deg)");
    }

   // Holds the turret at a fixed position in radians until it's interrupted
    public static Command holdPositionRadians(RobotContainer container, double radians) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(() -> turret.setPositionRadians(radians), turret)
                .withName("Turret Hold Position (rad)");
    }

    // Holds the turret at a fixed position in degrees until it's interrupted
    public static Command holdPositionDegrees(RobotContainer container, double degrees) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(() -> turret.setPositionDegrees(degrees), turret)
                .withName("Turret Hold Position (deg)");
    }

    public static Command moveTurret(RobotContainer container, double dutyCycle) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.run(() -> turret.setOpenLoop(dutyCycle), turret)
                .withName("Move Turret");
    }

    public static Command stop(RobotContainer container) {
        TurretSubsystem turret = container.getTurretSubsystem();
        return Commands.runOnce(turret::stop, turret).withName("Turret Stop");
    }
}
