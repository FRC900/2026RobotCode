// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.frc2026.subsystems.vision.VisionFieldPoseEstimate;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Consumer;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

public class RobotContainer {
    private static volatile RobotContainer instance;
    private final RobotState robotState;
    public SwerveDriveSimulation driveSimulation = null;
    public DriveSubsystem driveSubsystem;

    private final Consumer<VisionFieldPoseEstimate> visionEstimateConsumer =
            new Consumer<VisionFieldPoseEstimate>() {
                @Override
                public void accept(VisionFieldPoseEstimate estimate) {
                    driveSubsystem.addVisionMeasurement(estimate);
                }
            };

    private RobotContainer() {
        robotState = RobotState.getInstance(visionEstimateConsumer);

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
