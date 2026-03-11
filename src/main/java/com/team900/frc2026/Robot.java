// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import static edu.wpi.first.units.Units.Volt;

import com.pathplanner.lib.pathfinding.Pathfinding;
import com.team900.lib.util.CANBusStatusLogger;
import com.team900.lib.util.VirtualSubsystem;
import edu.wpi.first.math.MathShared;
import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.math.MathUsageId;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import org.ironmaple.simulation.SimulatedArena;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    static {
        if (RobotBase.isSimulation()) {
            SimulatedArena.overrideInstance(
                    new org.ironmaple.simulation.seasonspecific.rebuilt2026.Arena2026Rebuilt(false));
        }
    }

    static final int kRTPriority = 2;
    static final int kNonRTPriority = 1;

    private Command disabledCommand = Commands.none();
    private boolean hasEnabled = false;

    private final RobotContainer robotContainer = RobotContainer.getInstance();
    ;
    private int mIter = 0;
    private Command autonomousCommand = RobotContainer.getInstance().getAutonomousCommand();
    private Optional<Pose2d> startingPose = Optional.empty();

    private double lastTimestampNotValid = 0;

    private double timeOfLastSync = 0.0;

    private CANBusStatusLogger driverCAN = new CANBusStatusLogger(Constants.kCanBusCanivoreDrive);
    private CANBusStatusLogger mechanismCAN = new CANBusStatusLogger(Constants.kCanBusCanivoreMech);

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
            Logger.addDataReceiver(new NT4Publisher());
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

        RobotController.setBrownoutVoltage(Volt.of(6.0));

        // Silence joystick alerts
        DriverStation.silenceJoystickConnectionWarning(true);

        // Silence Rotation2d warnings
        var mathShared = MathSharedStore.getMathShared();
        MathSharedStore.setMathShared(
                new MathShared() {
                    @Override
                    public void reportError(String error, StackTraceElement[] stackTrace) {
                        if (error.startsWith("x and y components of Rotation2d are zero")) {
                            return;
                        }
                        mathShared.reportError(error, stackTrace);
                    }

                    @Override
                    public void reportUsage(MathUsageId id, int count) {
                        mathShared.reportUsage(id, count);
                    }

                    @Override
                    public double getTimestamp() {
                        return mathShared.getTimestamp();
                    }
                });

        // Log active commands
        Map<String, Integer> commandCounts = new HashMap<>();
        BiConsumer<Command, Boolean> logCommandFunction =
                (Command command, Boolean active) -> {
                    String name = command.getName();
                    int count = commandCounts.getOrDefault(name, 0) + (active ? 1 : -1);
                    commandCounts.put(name, count);
                    Logger.recordOutput(
                            "CommandsUnique/"
                                    + name
                                    + "_"
                                    + Integer.toHexString(command.hashCode()),
                            active);
                    Logger.recordOutput("CommandsAll/" + name, count > 0);
                };
        CommandScheduler.getInstance()
                .onCommandInitialize((Command command) -> logCommandFunction.accept(command, true));
        CommandScheduler.getInstance()
                .onCommandFinish((Command command) -> logCommandFunction.accept(command, false));
        CommandScheduler.getInstance()
                .onCommandInterrupt((Command command) -> logCommandFunction.accept(command, false));

        if (RobotBase.isSimulation()) {
            robotContainer.getDriveSubsystem().resetPose(new Pose2d(3, 3, new Rotation2d()));
        }
    }

    @Override
    public void robotPeriodic() {

        if (DriverStation.isEnabled()) {
            Threads.setCurrentThreadPriority(true, kRTPriority);
        } else {
            Threads.setCurrentThreadPriority(false, kNonRTPriority);
        }
        VirtualSubsystem.runAllPeriodic();
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
    public void disabledPeriodic() {
        // Periodically refresh the auto command from the dashboard chooser
        // so the driver can see what's selected and it stays up to date.
        if (mIter % 50 == 0) {
            autonomousCommand = robotContainer.getAutonomousCommand();
            SmartDashboard.putString(
                    "Selected Auto",
                    autonomousCommand != null ? autonomousCommand.getName() : "None");
        }
        mIter++;
    }

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

        autonomousCommand = robotContainer.getAutonomousCommand();
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
        var testCommand = robotContainer.getTestCommand();
        CommandScheduler.getInstance().schedule(testCommand);
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
