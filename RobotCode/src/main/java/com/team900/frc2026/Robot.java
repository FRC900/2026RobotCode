// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import com.team254.lib.time.RobotTime;

public class Robot extends LoggedRobot {
    private Command m_autonomousCommand;

    private final RobotContainer m_robotContainer;

    public Robot() {
        m_robotContainer = new RobotContainer();
        DataLogManager.start();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        if (Robot.isSimulation()) {
            m_robotContainer.getSimulatedRobotState().updateSim();
        }
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        if (m_autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(m_autonomousCommand);
        }

        if (isReal()) {
            Logger.addDataReceiver(new WPILOGWriter("/U/logs"));
            Logger.addDataReceiver(new NT4Publisher());
        } else {
            Logger.addDataReceiver(new WPILOGWriter("logs"));
            Logger.addDataReceiver(new NT4Publisher());

            Logger.recordMetadata("ProjectName", "Robot");
            Logger.recordMetadata("Build", BuildConstants.GIT_SHA);
        }



        Logger.start();
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {

        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }

        if (isReal()) {
            Logger.addDataReceiver(new WPILOGWriter("/U/logs"));
            Logger.addDataReceiver(new NT4Publisher());
        } else {
            Logger.addDataReceiver(new WPILOGWriter("logs"));
            Logger.addDataReceiver(new NT4Publisher());

            Logger.recordMetadata("ProjectName", "Robot");
            Logger.recordMetadata("Build", BuildConstants.GIT_SHA);
        }

        Logger.start();
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}
}
