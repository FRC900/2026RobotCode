// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026.factories;

import com.team254.lib.util.ShooterSetpoint;
import com.team900.frc2026.RobotContainer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class ShootingFactory {

    /* Commands for shooting */

    public static Command spinBoth(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {

        var topShooter = container.getTopShooter();
        var handoff = container.getHandoff();
        return new ParallelCommandGroup(
                        topShooter.velocitySetpointCommand(
                                () -> setpointSupplier.get().getTopShooterRPS()),
                        handoff.velocitySetpointCommand(
                                () -> setpointSupplier.get().getShooterRPS()))
                .withName("Spin Both Shooter Stages");
    }


    public static Command spinTest(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {

        var topShooter = container.getTopShooter();
        return topShooter
                .dutyCycleCommand(()-> 0.5);
    }

    public static Command spinBottom(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {

        var handoff = container.getHandoff();
        return handoff
                .velocitySetpointCommand(() -> setpointSupplier.get().getShooterRPS())
                .withName("Spin Up Bottom Shooter");
    }

    public static Command spinTop(
            RobotContainer container, Supplier<ShooterSetpoint> setpointSupplier) {

        var topShooter = container.getTopShooter();
        return topShooter
                .velocitySetpointCommand(() -> setpointSupplier.get().getTopShooterRPS())
                .withName("Spin Up Top Shooter");
    }
}
