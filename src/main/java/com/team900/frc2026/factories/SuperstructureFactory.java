package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class SuperstructureFactory {

    public static Command aim(
            Supplier<ShooterSetpoint> setPointSupplier, RobotContainer container) {
        // return null;
        return new ParallelCommandGroup(
                new InstantCommand(() -> container.getDriveCommand().setKAiming(true)),
                HoodFactory.aimHoodToPose(setPointSupplier, container));
        /*Command to aim at target
         * Aim turret
         * Aim hood */
    }

    public static Command stow(RobotContainer container) {
        return new ParallelCommandGroup(
                HoodFactory.stow(container));
    }

    public static Command trench(RobotContainer container) {
        return new ParallelCommandGroup(
                HoodFactory.stow(container), IntakeFactory.deploySlapdown(container));
    }

    public static Command intakeDeploy(RobotContainer container) {

        return IntakeFactory.deploySlapdown(container);
    }
}
