// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

public class RobotContainer {
    private static volatile RobotContainer instance;
    public SwerveDriveSimulation driveSimulation = null;

    private RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {}

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

    public static RobotContainer getInstance() {
        if (instance == null) {
            synchronized (RobotContainer.class) {
                if (instance == null) {
                    instance = new RobotContainer();
                }
            }
        }
        return instance;
    }
}
