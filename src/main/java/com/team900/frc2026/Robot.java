// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.ctre.phoenix6.SignalLogger;
import com.pathplanner.lib.pathfinding.Pathfinding;
import com.team900.lib.util.CANBusStatusLogger;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Threads;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.Optional;
import org.ironmaple.simulation.SimulatedArena;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {

    static final int kRTPriority = 2;
    static final int kNonRTPriority = 1;

    private Command disabledCommand = Commands.none();
    private boolean hasEnabled = false;

    private final RobotContainer robotContainer;
    private int mIter = 0;
    private Command autonomousCommand = Commands.none();
    private Optional<Pose2d> startingPose = Optional.empty();

    private double lastTimestampNotValid = 0;

    private double timeOfLastSync = 0.0;

    private CANBusStatusLogger driverCAN = new CANBusStatusLogger(Constants.kCanBusCanivoreDrive);
    private CANBusStatusLogger mechanismCAN = new CANBusStatusLogger(Constants.kCanBusCanivoreMech);

    private RobotContainer container;

    public Robot() {
        Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
        Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
        Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
        Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
        Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
        switch (BuildConstants.DIRTY) {
            case 0:
                Logger.recordMetadata("GitDirty", "All changes committed");
                break;
            case 1:
                Logger.recordMetadata("GitDirty", "Uncomitted changes");
                break;
            default:
                Logger.recordMetadata("GitDirty", "Unknown");
                break;
        }

        if (RobotBase.isReal()) {
            Logger.addDataReceiver(new WPILOGWriter());
            if (!DriverStation.isFMSAttached()) {
                Logger.addDataReceiver(new NT4Publisher());
            }
        } else if (Constants.kIsReplay) {
            setUseTiming(false);
            String logPath = LogFileUtil.findReplayLog();
            Logger.setReplaySource(new WPILOGReader(logPath));
            Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
        } else if (RobotBase.isSimulation()) {
            Logger.addDataReceiver(new NT4Publisher());
            Logger.addDataReceiver(new WPILOGWriter());
        }

        Logger.start();
        if (!Logger.hasReplaySource()) {
            RobotController.setTimeSource(RobotController::getFPGATime);
        }

        robotContainer = RobotContainer.getInstance();
        if (RobotBase.isSimulation()) {
            robotContainer.getDriveSubsystem().resetPose(new Pose2d(3, 3, new Rotation2d()));
        }
        SmartDashboard.putData("Command Scheduler", CommandScheduler.getInstance());
        SignalLogger.enableAutoLogging(false);
    }

    @Override
    public void robotPeriodic() {

        if (DriverStation.isEnabled()) {
            Threads.setCurrentThreadPriority(true, kRTPriority);
        } else {
            Threads.setCurrentThreadPriority(false, kNonRTPriority);
        }

        CommandScheduler.getInstance().run();

        RobotState.getInstance().updateLogger();
        robotContainer.getRobotViz().updateViz();
        if (Robot.isSimulation()) {
            robotContainer.getSimulatedRobotState().updateSim();
        }

        Threads.setCurrentThreadPriority(false, kNonRTPriority);
    }

    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {}

    @Override
    public void autonomousInit() {
        Threads.setCurrentThreadPriority(true, kRTPriority);

        Pathfinding.ensureInitialized();
        // Pathfinding.setCacheDistanceToleranceMeters(0.8);

        if (Robot.isSimulation()) {
            if (!hasEnabled) {
                SimulatedArena.getInstance().placeGamePiecesOnField();
            }
        }
        if (!hasEnabled) {
            hasEnabled = true;
        }

        RobotState.getInstance().setAutoStartTime(Timer.getFPGATimestamp());

        if (autonomousCommand != null) {
            CommandScheduler.getInstance().schedule(autonomousCommand);
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {}

    @Override
    public void teleopInit() {}

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

    // simulation period method in your Robot.java
    @Override
    public void simulationPeriodic() {
        SimulatedArena.getInstance().simulationPeriodic();
    }
}
