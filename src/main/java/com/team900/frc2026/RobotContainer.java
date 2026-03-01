// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.factories.HandoffFactory;
import com.team900.frc2026.factories.IntakeFactory;
import com.team900.frc2026.factories.SpindexerFactory;
import com.team900.frc2026.subsystems.drive.CompTunerConstants;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.frc2026.subsystems.drive.GyroIOPigeon2;
import com.team900.frc2026.subsystems.drive.ModuleIOTalonFXReal;
import com.team900.frc2026.subsystems.handoff.HandoffConstants;
import com.team900.frc2026.subsystems.handoff.HandoffSubsystem;
import com.team900.frc2026.subsystems.Hood.HoodConstants;
import com.team900.frc2026.subsystems.Hood.HoodSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import com.team900.frc2026.subsystems.intake.IntakeRollerSubsystem;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.frc2026.subsystems.shooter.ShooterSubsystem;
import com.team900.frc2026.subsystems.spindexer.SpindexerConstants;
import com.team900.frc2026.subsystems.spindexer.SpindexerSubsystem;
import com.team900.frc2026.subsystems.vision.VisionFieldPoseEstimate;
import com.team900.lib.subsystems.CanCoderIOHardware;
import com.team900.lib.subsystems.TalonFXIO;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import java.util.function.Consumer;
import lombok.Getter;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

public class RobotContainer {
    private DriveSubsystem buildDriveSystem() {

        return new DriveSubsystem(
                new GyroIOPigeon2(),
                new ModuleIOTalonFXReal(CompTunerConstants.FrontLeft),
                new ModuleIOTalonFXReal(CompTunerConstants.FrontRight),
                new ModuleIOTalonFXReal(CompTunerConstants.BackLeft),
                new ModuleIOTalonFXReal(CompTunerConstants.BackRight));
    }

    private SpindexerSubsystem buildSpindexerSubsystem() {
        return new SpindexerSubsystem(
                SpindexerConstants.kSpindexerConfig,
                new TalonFXIO(SpindexerConstants.kSpindexerConfig));
    }

    private HoodSubsystem buildHoodSubsystem() {
        return new HoodSubsystem(
                HoodConstants.kHoodConfig,
                new TalonFXIO(HoodConstants.kHoodConfig),
                new CanCoderIOHardware(HoodConstants.kHoodConfig.canCoderConfig));
    }

    private IntakeRollerSubsystem buildIntakeRollerSubsystem() {
        return new IntakeRollerSubsystem(
                IntakeRollerConstants.kIntakeRollerConfig,
                new TalonFXIO(IntakeRollerConstants.kIntakeRollerConfig));
    }

    private HandoffSubsystem buildHandoffSubsystem() {
        return new HandoffSubsystem(
                HandoffConstants.kHandoffConfig, new TalonFXIO(HandoffConstants.kHandoffConfig));
    }

    private ShooterSubsystem buildShooterSubsystem() {
        return new ShooterSubsystem(
                ShooterConstants.kShooterConfig,
                new TalonFXIO(ShooterConstants.kShooterConfig),
                new TalonFXIO[] {
                    new TalonFXIO(ShooterConstants.kShooterConfig.followers[0].config)
                });
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
    @Getter private final DriveSubsystem driveSubsystem = buildDriveSystem();

    private final CommandPS5Controller driveController = new CommandPS5Controller(0);

    @Getter private final SpindexerSubsystem spindexerSubsystem = buildSpindexerSubsystem();

    // private final HoodSubsystem hoodSubsystem = buildHoodSubsystem();
    @Getter
    private final IntakeRollerSubsystem intakeRollerSubsystem = buildIntakeRollerSubsystem();

    @Getter private final HandoffSubsystem handoffSubsystem = buildHandoffSubsystem();
    @Getter private final ShooterSubsystem shooterSubsystem = buildShooterSubsystem();

    private RobotContainer() {
        if (Robot.isSimulation()) {
            // assert this.simulatedRobotState != null;
            // this.simulatedRobotState.init();
        }
        configureBindings();
    }

    private void configureBindings() {
        // Swerve Drive
        driveSubsystem.setDefaultCommand(
                driveSubsystem.run(
                        () ->
                                driveSubsystem.teleopControl(
                                        -driveController.getLeftY(),
                                        -driveController.getLeftX(),
                                        -driveController.getRightX())));

        driveController
                .R1()
                .whileTrue(
                        new ParallelCommandGroup(
                                IntakeFactory.runIntake(instance),
                                HandoffFactory.runHandoff(instance),
                                SpindexerFactory.runSpindexer(instance)));
        driveController
                .L1()
                .whileTrue(
                        new ParallelCommandGroup(
                                IntakeFactory.exhaustIntake(instance),
                                HandoffFactory.exhaustHandoff(instance),
                                SpindexerFactory.exhaustSpindexer(instance)));
    }

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
