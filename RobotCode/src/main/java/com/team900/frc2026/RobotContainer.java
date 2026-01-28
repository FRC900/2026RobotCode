// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.team254.lib.loops.StatusSignalLoop;
import com.team254.lib.pathplanner.auto.NamedCommands;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team254.lib.subsystems.TalonFXIO;
import com.team254.lib.util.ShooterSetpoint;
import com.team900.frc2026.controlboard.ControlBoard;
import com.team900.frc2026.controlboard.ModalControls;
import com.team900.frc2026.factories.ShootingFactory;
import com.team900.frc2026.simulation.SimulatedRobotState;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottom;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottomSensorIOHardware;
import com.team900.frc2026.subsystems.ShooterBottom.ShooterBottomSensorIOSim;
import com.team900.frc2026.subsystems.TopShooter.TopShooter;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class RobotContainer {
    private RobotState robotState;
    private ShooterBottom shooterBottom;
    private TopShooter topShooter;

    private SimulatedRobotState simulatedRobotState;
    private ShooterBottomSensorIOSim simulatedBottomShooterSensors;


    public RobotContainer() {

        this.robotState = new RobotState();


        this.simulatedRobotState =
                RobotBase.isSimulation() ? new SimulatedRobotState(this) : null;
        this.simulatedBottomShooterSensors =
                RobotBase.isSimulation()
                        ? new ShooterBottomSensorIOSim(
                                Constants.SensorConstants.kShooterBottomBannerSensorPort)
                        : null;

        this.shooterBottom = buildShooterBottom();
        this.topShooter = buildTopShooter();


        configureBindings();
        
        Supplier shoooterSetpoint = ShooterSetpoint.speakerSetpointSupplier(robotState);
        NamedCommands.registerCommand("Shoot", new
            SequentialCommandGroup(ShootingFactory.spinBoth(this, shoooterSetpoint)));

        configureBindings();
        statusSignalLoop.register(getBottomShooter());
    }

    public RobotState getRobotState() {
        return robotState;
    }

    private void configureBindings() {
        modalControls.configureBindings();

        modalControls.shoot().whileTrue(ShootingFactory.spinBoth(this, ShooterSetpoint.speakerSetpointSupplier(robotState))
                );
    }

    public Command getAutonomousCommand() {
        return ShootingFactory.spinBoth(this, ShooterSetpoint.speakerSetpointSupplier(robotState));
    }

    private final ControlBoard controlBoard = ControlBoard.getInstance();
    private final ModalControls modalControls = ModalControls.getInstance();

    private final StatusSignalLoop statusSignalLoop = new StatusSignalLoop(250.0, "TurretThread");

    public SimulatedRobotState getSimulatedRobotState() {
        return simulatedRobotState;
    }

    public TopShooter getTopShooter() {
        return topShooter;
    }

    private ShooterBottom buildShooterBottom() {
        if (RobotBase.isSimulation()) {
            return new ShooterBottom(
                    Constants.kShooterBottomConfig,
                    new SimTalonFXIO(Constants.kShooterBottomConfig),
                    simulatedBottomShooterSensors,
                    robotState);
        } else {
            return new ShooterBottom(
                    Constants.kShooterBottomConfig,
                    new TalonFXIO(Constants.kShooterBottomConfig),
                    new ShooterBottomSensorIOHardware(
                            Constants.SensorConstants.kShooterBottomBannerSensorPort),
                    robotState);
        }
    }

    private TopShooter buildTopShooter() {
        if (RobotBase.isSimulation()) {
            return new TopShooter(
                    Constants.kShooterTopConfig,
                    new SimTalonFXIO(Constants.kShooterTopTopConfig),
                    new SimTalonFXIO[] {new SimTalonFXIO(Constants.kShooterTopBottomConfig)},
                    robotState);
        } else {
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
}
