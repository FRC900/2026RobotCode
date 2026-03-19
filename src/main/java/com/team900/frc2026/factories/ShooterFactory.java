// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.Supplier;

public class ShooterFactory {

    /* Commands for shooting */

    public static Command idle(RobotContainer container) {
        return container
                .getShooterStage2Subsystem()
                .velocitySetpointCommand(() -> ShooterConstants.ShooterStage2.kIdleRPS);
    }

    public static Command setShooterRPS(double RPS, RobotContainer container) {
        return container.getShooterStage2Subsystem().velocitySetpointCommand(() -> RPS);
    }

    public static Command setShooterRPS(
           RobotContainer container,   Supplier<ShooterSetpoint> setpointSupplier) {
        return container
                .getShooterStage2Subsystem()
                .velocitySetpointCommand(setpointSupplier.get()::getShooterRPS);
    }

    public static Command runHandoff(RobotContainer container) {
        return container
                .getShooterStage1Subsystem()
                .dutyCycleCommand(() -> ShooterConstants.ShooterStage1.kHandoffDutyCycle);
    }

    public static Command stopHandoff(RobotContainer container) {
        return container.getShooterStage1Subsystem().voltageCommand(() -> 0);
    }

    public static Command exhaustHandoff(RobotContainer container) {
        return container
                .getShooterStage1Subsystem()
                .dutyCycleCommand(() -> ShooterConstants.ShooterStage1.kHandoffDutyCycleExhaust);
    }
}
