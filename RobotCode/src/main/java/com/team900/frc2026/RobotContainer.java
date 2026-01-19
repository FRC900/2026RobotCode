// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team254.lib.subsystems.TalonFXIO;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottom;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottomSensorIOHardware;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottomSensorIOSim;
import com.team900.frc2026.subsystems.TopShooter.TopShooter;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {
    public RobotContainer() {
        configureBindings();
    }

    public RobotState getRobotState() {
        return robotState;
    }

    private void configureBindings() {}

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

    public TopShooter getTopShooter() {
        return topShooter;
    }

    private ShooterBottom buildShooterBottom() {
        if (RobotBase.isSimulation()){
            return new ShooterBottom(Constants.kShooterBottomConfig, 
            new SimTalonFXIO(Constants.kShooterBottomConfig), 
            simulatedBottomShooterSensors, robotState);
        }
        else{
        return new ShooterBottom(
                Constants.kShooterBottomConfig,
                new TalonFXIO(Constants.kShooterBottomConfig),
                new ShooterBottomSensorIOHardware(
                        Constants.SensorConstants.kShooterBottomBannerSensorPort),
                robotState);
        }
    }

    private TopShooter buildTopShooter() {
        if (RobotBase.isSimulation()){
            return new TopShooter(
                Constants.kShooterTopConfig,
                new SimTalonFXIO(Constants.kShooterTopTopConfig),
                new SimTalonFXIO[] {new SimTalonFXIO(Constants.kShooterTopBottomConfig)},
                robotState);
        }
        else{
        return new TopShooter(
                Constants.kShooterTopConfig,
                new TalonFXIO(Constants.kShooterTopTopConfig),
                new TalonFXIO[] {new TalonFXIO(Constants.kShooterTopBottomConfig)},
                robotState);
        }
    }

    public ShooterBottom getBottomShooter() {
        return shooterBottom;
    }

    private final RobotState robotState = new RobotState();
    private final ShooterBottom shooterBottom = buildShooterBottom();
    private final TopShooter topShooter = buildTopShooter();

    private final ShooterBottomSensorIOSim simulatedBottomShooterSensors = Robot.isSimulation() 
    ? new ShooterBottomSensorIOSim(Constants.SensorConstants.kShooterBottomBannerSensorPort)
    : null;
}
