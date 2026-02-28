// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.subsystems.drive.CompTunerConstants;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.frc2026.subsystems.drive.GyroIOPigeon2;
import com.team900.frc2026.subsystems.drive.ModuleIOTalonFXReal;
import com.team900.frc2026.subsystems.spindexer.SpindexerSubsystem;
import com.team900.frc2026.subsystems.vision.VisionFieldPoseEstimate;
import com.team900.lib.subsystems.TalonFXIO;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.function.Consumer;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

public class RobotContainer {
    private DriveSubsystem buildDriveSystem() {
        if (RobotBase.isSimulation()) {
            return new DriveSubsystem(
                    new GyroIOPigeon2(),
                    new ModuleIOTalonFXReal(CompTunerConstants.FrontLeft),
                    new ModuleIOTalonFXReal(CompTunerConstants.FrontRight),
                    new ModuleIOTalonFXReal(CompTunerConstants.BackLeft),
                    new ModuleIOTalonFXReal(CompTunerConstants.BackRight));
        } else {
            return new DriveSubsystem(null, null, null, null, null);
        }
    private SpindexerSubsystem buildSpindexerSubsystem()    {
          if (RobotBase.isSimulation()) {
            return new SpindexerSubsystem(SpindexerConstants.config, new TalonFXIO(SpindexerConstants.config));
    }
    }

    private static volatile RobotContainer instance;
    private final Consumer<VisionFieldPoseEstimate> visionEstimateConsumer =
            new Consumer<VisionFieldPoseEstimate>() {
                @Override
                public void accept(VisionFieldPoseEstimate estimate) {
                    driveSubsystem.addVisionMeasurement(estimate);
                }
            };
    private final RobotState robotState = RobotState.getInstance(visionEstimateConsumer);
    public SwerveDriveSimulation driveSimulation = null;
    public DriveSubsystem driveSubsystem;

    private RobotContainer() {
        if (Robot.isSimulation()) {
            // assert this.simulatedRobotState != null;
            // this.simulatedRobotState.init();
        }
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
