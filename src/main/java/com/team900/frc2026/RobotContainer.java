// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.auto.AutoDashboard;
import com.team900.frc2026.commands.DriveMaintainingHeadingCommand;
import com.team900.frc2026.controlboard.ControlBoard;
import com.team900.frc2026.factories.HandoffFactory;
import com.team900.frc2026.factories.IntakeFactory;
import com.team900.frc2026.factories.ShooterFactory;
import com.team900.frc2026.factories.ShootingFactory;
import com.team900.frc2026.factories.SpindexerFactory;
import com.team900.frc2026.factories.SuperstructureFactory;
import com.team900.frc2026.simulation.SimulatedRobotState;
import com.team900.frc2026.subsystems.coprocessor.CoprocessorSubsystem;
import com.team900.frc2026.subsystems.drive.CompTunerConstants;
import com.team900.frc2026.subsystems.drive.DriveSubsystem;
import com.team900.frc2026.subsystems.drive.GyroIO;
import com.team900.frc2026.subsystems.drive.GyroIOPigeon2;
import com.team900.frc2026.subsystems.drive.GyroIOSim;
import com.team900.frc2026.subsystems.drive.ModuleIO;
import com.team900.frc2026.subsystems.drive.ModuleIOTalonFXReal;
import com.team900.frc2026.subsystems.drive.ModuleIOTalonFXSim;
import com.team900.frc2026.subsystems.drive.SimTunerConstants;
import com.team900.frc2026.subsystems.handoff.HandoffConstants;
import com.team900.frc2026.subsystems.handoff.HandoffSubsystem;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.hood.HoodSubsystem;
import com.team900.frc2026.subsystems.intake.IntakePivotConstants;
import com.team900.frc2026.subsystems.intake.IntakePivotSubsystem;
import com.team900.frc2026.subsystems.intake.IntakeRollerConstants;
import com.team900.frc2026.subsystems.intake.IntakeRollerSubsystem;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.frc2026.subsystems.shooter.ShooterSubsystem;
import com.team900.frc2026.subsystems.spindexer.SpindexerConstants;
import com.team900.frc2026.subsystems.spindexer.SpindexerSubsystem;
import com.team900.frc2026.subsystems.turret.TurretIO;
import com.team900.frc2026.subsystems.turret.TurretIOHardware;
import com.team900.frc2026.subsystems.turret.TurretIOSim;
import com.team900.frc2026.subsystems.turret.TurretSubsystem;
import com.team900.frc2026.subsystems.vision.VisionFieldPoseEstimate;
import com.team900.frc2026.viz.RobotViz;
import com.team900.lib.subsystems.CanCoderIOHardware;
import com.team900.lib.subsystems.SimCanCoderIO;
import com.team900.lib.subsystems.SimTalonFXIO;
import com.team900.lib.subsystems.SimTalonFXWithCancoder;
import com.team900.lib.subsystems.TalonFXIO;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import java.util.function.Consumer;
import lombok.Getter;
import org.ironmaple.simulation.SimulatedArena;

public class RobotContainer {
    private DriveSubsystem buildDriveSystem() {

        if (RobotBase.isSimulation()) {
            SimulatedArena.getInstance().addDriveTrainSimulation(simulatedRobotState.getSimDrive());

            return new DriveSubsystem(
                    new GyroIOSim(simulatedRobotState.getSimDrive().getGyroSimulation()),
                    new ModuleIOTalonFXSim(
                            SimTunerConstants.FrontLeft,
                            simulatedRobotState.getSimDrive().getModules()[0]),
                    new ModuleIOTalonFXSim(
                            SimTunerConstants.FrontRight,
                            simulatedRobotState.getSimDrive().getModules()[1]),
                    new ModuleIOTalonFXSim(
                            SimTunerConstants.BackLeft,
                            simulatedRobotState.getSimDrive().getModules()[2]),
                    new ModuleIOTalonFXSim(
                            SimTunerConstants.BackRight,
                            simulatedRobotState.getSimDrive().getModules()[3]));
        } else if (RobotBase.isReal())
            return new DriveSubsystem(
                    new GyroIOPigeon2(),
                    new ModuleIOTalonFXReal(CompTunerConstants.FrontLeft),
                    new ModuleIOTalonFXReal(CompTunerConstants.FrontRight),
                    new ModuleIOTalonFXReal(CompTunerConstants.BackLeft),
                    new ModuleIOTalonFXReal(CompTunerConstants.BackRight));

        return new DriveSubsystem(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
    }

    private SpindexerSubsystem buildSpindexerSubsystem() {
        if (RobotBase.isSimulation())
            return new SpindexerSubsystem(
                    SpindexerConstants.kSpindexerConfig,
                    new SimTalonFXIO(SpindexerConstants.kSpindexerConfig));

        return new SpindexerSubsystem(
                SpindexerConstants.kSpindexerConfig,
                new TalonFXIO(SpindexerConstants.kSpindexerConfig));
    }

    private HoodSubsystem buildHoodSubsystem() {
        if (RobotBase.isSimulation())
            return new HoodSubsystem(
                    HoodConstants.kHoodConfig,
                    simulatedHoodMotor,
                    new SimCanCoderIO(
                            HoodConstants.kHoodCanCoderConfig,
                            simulatedHoodMotor.getSupplierForCancoder(HoodConstants.kHoodConfig)));

        return new HoodSubsystem(
                HoodConstants.kHoodConfig,
                new TalonFXIO(HoodConstants.kHoodConfig),
                new CanCoderIOHardware(HoodConstants.kHoodCanCoderConfig));
    }

    private TurretSubsystem buildTurretSubsystem() {
        if (RobotBase.isSimulation()) return new TurretSubsystem(new TurretIOSim());

        if (RobotBase.isReal()) return new TurretSubsystem(new TurretIOHardware());

        return new TurretSubsystem(new TurretIO() {});
    }

    private IntakeRollerSubsystem buildIntakeRollerSubsystem() {
        if (RobotBase.isSimulation())
            return new IntakeRollerSubsystem(
                    IntakeRollerConstants.kIntakeRollerConfig,
                    new SimTalonFXIO(IntakeRollerConstants.kIntakeRollerConfig));

        return new IntakeRollerSubsystem(
                IntakeRollerConstants.kIntakeRollerConfig,
                new TalonFXIO(IntakeRollerConstants.kIntakeRollerConfig));
    }

    private IntakePivotSubsystem buildIntakePivotSubsystem() {
        if (RobotBase.isSimulation())
            return new IntakePivotSubsystem(
                    IntakePivotConstants.kIntakePivotConfig,
                    simulatedIntakeMotor,
                    new SimCanCoderIO(
                            IntakePivotConstants.kIntakeCanCoderConfig,
                            simulatedIntakeMotor.getSupplierForCancoder(
                                    IntakePivotConstants.kIntakePivotConfig)));

        return new IntakePivotSubsystem(
                IntakePivotConstants.kIntakePivotConfig,
                new TalonFXIO(IntakePivotConstants.kIntakePivotConfig),
                new CanCoderIOHardware(IntakePivotConstants.kIntakeCanCoderConfig));
    }

    private HandoffSubsystem buildHandoffSubsystem() {
        if (RobotBase.isSimulation())
            return new HandoffSubsystem(
                    HandoffConstants.kHandoffConfig,
                    new SimTalonFXIO(HandoffConstants.kHandoffConfig));
        return new HandoffSubsystem(
                HandoffConstants.kHandoffConfig, new TalonFXIO(HandoffConstants.kHandoffConfig));
    }

    private ShooterSubsystem buildShooterSubsystem() {
        if (RobotBase.isSimulation())
            return new ShooterSubsystem(
                    ShooterConstants.kShooterConfig,
                    new SimTalonFXIO(ShooterConstants.kShooterConfig),
                    new TalonFXIO[] {
                        new SimTalonFXIO(ShooterConstants.kShooterConfig.followers[0].config)
                    });
        return new ShooterSubsystem(
                ShooterConstants.kShooterConfig,
                new TalonFXIO(ShooterConstants.kShooterConfig),
                new TalonFXIO[] {
                    new TalonFXIO(ShooterConstants.kShooterConfig.followers[0].config)
                });
    }

    @Getter private final ControlBoard controlBoard = ControlBoard.getInstance();

    private final SimTalonFXWithCancoder simulatedHoodMotor =
            Robot.isSimulation() ? new SimTalonFXWithCancoder(HoodConstants.kHoodConfig) : null;

    private final SimTalonFXWithCancoder simulatedIntakeMotor =
            Robot.isSimulation()
                    ? new SimTalonFXWithCancoder(IntakePivotConstants.kIntakePivotConfig)
                    : null;

    private static volatile RobotContainer instance;

    @Getter private SimulatedRobotState simulatedRobotState = new SimulatedRobotState();

    @Getter private final DriveSubsystem driveSubsystem = buildDriveSystem();

    @Getter
    private final CoprocessorSubsystem coprocessorSubsystem =
            new CoprocessorSubsystem(driveSubsystem);

    private final Consumer<VisionFieldPoseEstimate> visionEstimateConsumer =
            new Consumer<VisionFieldPoseEstimate>() {
                @Override
                public void accept(VisionFieldPoseEstimate estimate) {
                    driveSubsystem.addVisionMeasurement(estimate);
                }
            };

    private final RobotState robotState = RobotState.getInstance(visionEstimateConsumer);

    @Getter
    private final DriveMaintainingHeadingCommand driveCommand =
            (new DriveMaintainingHeadingCommand(
                    this,
                    controlBoard::getThrottle,
                    controlBoard::getStrafe,
                    controlBoard::getRotation));

    @Getter private final RobotViz robotViz = new RobotViz();

    private final CommandPS5Controller driveController = new CommandPS5Controller(0);

    @Getter private final SpindexerSubsystem spindexerSubsystem = buildSpindexerSubsystem();
    @Getter private final HoodSubsystem hoodSubsystem = buildHoodSubsystem();

    // @Getter private final TurretSubsystem turretSubsystem = buildTurretSubsystem();

    @Getter
    private final IntakeRollerSubsystem intakeRollerSubsystem = buildIntakeRollerSubsystem();

    @Getter private final IntakePivotSubsystem intakePivotSubsystem = buildIntakePivotSubsystem();

    @Getter private final HandoffSubsystem handoffSubsystem = buildHandoffSubsystem();
    @Getter private final ShooterSubsystem shooterSubsystem = buildShooterSubsystem();

    private RobotContainer() {
        if (Robot.isSimulation()) {
            assert this.simulatedRobotState != null;
            this.simulatedRobotState.init();
        }
        configureBindings();
    }

    private boolean intakeDeployed = false;

    private void configureBindings() {
        // Swerve Drive
        driveSubsystem.setDefaultCommand(driveCommand);

        // Intake pivot, l1 to retract and deploy intake
        controlBoard
                .toggleIntake()
                .onTrue(
                        Commands.either(
                                IntakeFactory.retractSlapdown(this)
                                        .andThen(new InstantCommand(() -> intakeDeployed = false)),
                                IntakeFactory.deploySlapdown(this)
                                        .andThen(new InstantCommand(() -> intakeDeployed = true)),
                                () -> intakeDeployed));

        controlBoard.shoot().whileTrue(ShootingFactory.shoot(ShooterSetpoint::setpointHub, this));

        controlBoard.resetGyro().onTrue(new InstantCommand(driveSubsystem::teleopResetRotation));

        controlBoard.stowHood().onTrue(SuperstructureFactory.stow(this));

        controlBoard
                .intake()
                .onTrue(
                        new ParallelCommandGroup(
                                SpindexerFactory.runSpindexer(this),
                                HandoffFactory.runHandoff(this),
                                ShooterFactory.setShooterRPM(20, this)));
    }

    public boolean odometryCloseToPose(Pose2d pose) {
        Pose2d fieldToRobot = robotState.getLatestFieldToRobot().getValue();
        double distance = fieldToRobot.getTranslation().getDistance(pose.getTranslation());
        SmartDashboard.putNumber("Distance From Start Pose", distance);
        double rotation =
                Math.abs(
                        fieldToRobot
                                .getRotation()
                                .rotateBy(pose.getRotation().unaryMinus())
                                .getDegrees());
        SmartDashboard.putNumber("Rotation From Start Pose", rotation);
        if (distance < 0.25 && rotation < 8.0) {
            return true;
        }
        return false;
    }

    private final AutoDashboard autoDashboard = new AutoDashboard();

    public Command getAutonomousCommand() {
        return autoDashboard.getSelectedAuto();
    }

    public static synchronized RobotContainer getInstance() {
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
