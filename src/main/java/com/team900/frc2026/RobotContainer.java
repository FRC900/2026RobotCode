// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.auto.AutoDashboard;
import com.team900.frc2026.commands.DriveMaintainingHeadingCommand;
import com.team900.frc2026.controlboard.ControlBoard;
import com.team900.frc2026.factories.HandoffFactory;
import com.team900.frc2026.factories.HoodFactory;
import com.team900.frc2026.factories.IntakeFactory;
import com.team900.frc2026.factories.ShooterFactory;
import com.team900.frc2026.factories.ShootingFactory;
import com.team900.frc2026.factories.SpindexerFactory;
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
import com.team900.frc2026.subsystems.vision.VisionConstants;
import com.team900.frc2026.subsystems.vision.VisionIOPhotonVision;
import com.team900.frc2026.subsystems.vision.VisionIOPhotonVisionSim;
import com.team900.frc2026.subsystems.vision.VisionSubsystem;
import com.team900.frc2026.viz.RobotViz;
import com.team900.lib.subsystems.CanCoderIOHardware;
import com.team900.lib.subsystems.SimCanCoderIO;
import com.team900.lib.subsystems.SimTalonFXIO;
import com.team900.lib.subsystems.SimTalonFXWithCancoder;
import com.team900.lib.subsystems.TalonFXIO;
import com.team900.lib.time.RobotTime;
import com.team900.lib.util.FieldConstants;
import com.team900.lib.util.FieldConstants.LeftTrench;
import com.team900.lib.util.FieldConstants.LinesVertical;
import com.team900.lib.util.FieldConstants.RightTrench;
import com.team900.lib.util.HubFlipUtil;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.*;
import lombok.Getter;
import lombok.Setter;
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

    private VisionSubsystem buildVisionSubsystem() {
        if (RobotBase.isSimulation()) {
            return new VisionSubsystem(
                    robotState,
                    new VisionIOPhotonVisionSim(
                            VisionConstants.camera0Name,
                            VisionConstants.robotToCamera0,
                            simulatedRobotState.getSimDrive()::getSimulatedDriveTrainPose));
        } else {
            return new VisionSubsystem(
                    robotState,
                    new VisionIOPhotonVision(
                            VisionConstants.camera0Name, VisionConstants.robotToCamera0));
        }
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
                    HoodConstants.kHoodConfig, new SimTalonFXIO(HoodConstants.kHoodConfig));

        return new HoodSubsystem(
                HoodConstants.kHoodConfig, new TalonFXIO(HoodConstants.kHoodConfig));
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

    private final Trigger zeroHood;

    private final SimTalonFXWithCancoder simulatedIntakeMotor =
            Robot.isSimulation()
                    ? new SimTalonFXWithCancoder(IntakePivotConstants.kIntakePivotConfig)
                    : null;

    private static volatile RobotContainer instance;

    @Getter private SimulatedRobotState simulatedRobotState = new SimulatedRobotState();

    @Getter private final DriveSubsystem driveSubsystem = buildDriveSystem();

    private final RobotState robotState = RobotState.getInstance();

    @Getter private final VisionSubsystem visionSubsystem = buildVisionSubsystem();

    @Getter
    private final CoprocessorSubsystem coprocessorSubsystem = new CoprocessorSubsystem(robotState);

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

    // @Getter private final TurretSubsystem turretSubsystem = buildTurretSubsystem();=======

    @Getter
    private final IntakeRollerSubsystem intakeRollerSubsystem = buildIntakeRollerSubsystem();

    @Getter private final IntakePivotSubsystem intakePivotSubsystem = buildIntakePivotSubsystem();

    @Getter private final HandoffSubsystem handoffSubsystem = buildHandoffSubsystem();
    @Getter private final ShooterSubsystem shooterSubsystem = buildShooterSubsystem();

    private RobotContainer() {
        instance = this;

        if (Robot.isSimulation()) {
            assert this.simulatedRobotState != null;
            this.simulatedRobotState.init();
        }
        autoDashboard = new AutoDashboard();

        zeroHood = new Trigger(DriverStation::isEnabled)
                .onTrue(Commands.defer(() -> HoodFactory.zero(this), Set.of(hoodSubsystem)));
        

        configureBindings();
    }

    @Setter @Getter private boolean intakeDeployed = intakePivotSubsystem.isDeployed();

    private void configureBindings() {
        
        // Swerve Drive
        driveSubsystem.setDefaultCommand(driveCommand);

        // controlBoard
        //         .turretAlignToHub()
        //         .whileTrue(
        //                 new HubAlignTurretCommand(
        //                         driveSubsystem,
        //                         turretSubsystem));

        controlBoard
                .swerveAlignToHub()
                .onTrue(new InstantCommand(() -> getDriveCommand().setKAiming(true)))
                .onFalse(new InstantCommand(() -> getDriveCommand().setKAiming(false)));

        // Intake pivot, l1 to retract and deploy intake
        // controlBoard
        //         .toggleIntake()
        //         .onTrue(
        //                 Commands.defer(
        //                         () -> {
        //                             if (intakeDeployed) {
        //                                 intakeDeployed = false;
        //                                 return IntakeFactory.retractSlapdown(this);
        //                             } else {
        //                                 intakeDeployed = true;
        //                                 return IntakeFactory.deploySlapdown(this);
        //                             }
        //                         },
        //                         Set.of(getIntakePivotSubsystem())));

        // controlBoard
        //         .toggleIntake()
        //         .onTrue(
        //                 Commands.defer(
        //                         () ->
        //                                 intakePivotSubsystem.isDeployed()
        //                                         ? IntakeFactory.retractSlapdown(this)
        //                                         : IntakeFactory.deploySlapdown(this),
        //                         Set.of(getIntakePivotSubsystem())));

        controlBoard
                .toggleIntake()
                .and(() -> !intakePivotSubsystem.isDeployed())
                .onTrue(IntakeFactory.deploySlapdown(this));

        controlBoard
                .toggleIntake()
                .and(() -> intakePivotSubsystem.isDeployed())
                .onTrue(IntakeFactory.retractSlapdown(this));

        controlBoard
                .shoot()
                .onTrue(
                        ((ShooterFactory.setShooterRPS(ShooterConstants.kShootingRPS, this)
                                        .until(
                                                () ->
                                                        MathUtil.isNear(
                                                                ShooterConstants.kShootingRPS,
                                                                shooterSubsystem
                                                                        .getCurrentVelocity(),
                                                                5))))
                                .andThen(
                                        new ParallelCommandGroup(
                                                HandoffFactory.runHandoff(this),
                                                SpindexerFactory.runSpindexer(this))))
                .onFalse(
                        new ParallelCommandGroup(
                                // ShooterFactory.setShooterRPS(0, this),
                                shooterSubsystem.voltageCommand(() -> 0),
                                SpindexerFactory.exhaustSpindexer(instance)
                                        .withTimeout(0.25)
                                        .andThen(SpindexerFactory.stopSpindexer(this)),
                                HandoffFactory.exhaustHandoff(this)
                                        .withTimeout(0.25)
                                        .andThen(HandoffFactory.stopHandoff(this))));

        controlBoard
                .shootAuto()
                .whileTrue(ShootingFactory.shoot(ShooterSetpoint::setpointHub, this))
                .onFalse(
                        new ParallelCommandGroup(
                                ShooterFactory.setShooterRPS(0, this),
                                new InstantCommand(() -> getDriveCommand().setKAiming(false)),
                                SpindexerFactory.stopSpindexer(this),
                                IntakeFactory.stopIntake(this),
                                HandoffFactory.stopHandoff(this)));

        controlBoard.resetGyro().onTrue(new InstantCommand(driveSubsystem::teleopResetRotation));

        controlBoard.stowHood().onTrue(HoodFactory.stow(instance));

        controlBoard
                .intake()
                .onTrue(IntakeFactory.runIntake(this))
                .onFalse(IntakeFactory.stopIntake(this));

        controlBoard
                .exhaust()
                .onTrue(IntakeFactory.exhaustIntake(this))
                .onFalse(IntakeFactory.stopIntake(this));

        controlBoard.resetHood().onTrue(HoodFactory.zero(this));

        Trigger isTeleop = new Trigger(DriverStation::isTeleopEnabled);

        new Trigger(intakeRollerSubsystem::isStalled)
                .debounce(0.1)
                .onTrue((IntakeFactory.exhaustIntake(this)))
                .onFalse(Commands.none());

        new Trigger(() -> HubFlipUtil.isFlip((long) (RobotTime.getTimestampSeconds())))
                .and(isTeleop)
                .onTrue(
                        Commands.sequence(
                                Commands.runOnce(
                                        () ->
                                                driveController
                                                        .getHID()
                                                        .setRumble(RumbleType.kBothRumble, 1.0)),
                                Commands.waitSeconds(0.3),
                                Commands.runOnce(
                                        () ->
                                                driveController
                                                        .getHID()
                                                        .setRumble(RumbleType.kBothRumble, 0.0))));

        double trenchXHalfDepth = RightTrench.depth / 2.0;
        double fieldWidth = FieldConstants.fieldWidth;

        double[][] trenchHoodZeroingBoxes = {
            // xmin, xmax, ymin, ymax
            // Alliance side, right trench (y near 0)
            {
                LinesVertical.hubCenter - trenchXHalfDepth,
                LinesVertical.hubCenter + trenchXHalfDepth,
                0,
                RightTrench.openingWidth
            },
            // Alliance side, left trench (y near fieldWidth)
            {
                LinesVertical.hubCenter - trenchXHalfDepth,
                LinesVertical.hubCenter + trenchXHalfDepth,
                fieldWidth - LeftTrench.openingWidth,
                fieldWidth
            },
            // Opponent side, right trench
            {
                LinesVertical.oppHubCenter - trenchXHalfDepth,
                LinesVertical.oppHubCenter + trenchXHalfDepth,
                0,
                RightTrench.openingWidth
            },
            // Opponent side, left trench
            {
                LinesVertical.oppHubCenter - trenchXHalfDepth,
                LinesVertical.oppHubCenter + trenchXHalfDepth,
                fieldWidth - LeftTrench.openingWidth,
                fieldWidth
            },
        };

        new Trigger(
                        () -> {
                            Pose2d pose = robotState.getLatestFieldToRobot().getValue();
                            double x = pose.getTranslation().getX();
                            double y = pose.getTranslation().getY();

                            for (double[] box : trenchHoodZeroingBoxes) {
                                if (x >= box[0] && x <= box[1] && y >= box[2] && y <= box[3]) {
                                    double trenchCenterX = (box[0] + box[1]) / 2.0;
                                    double xDist = x - trenchCenterX;
                                    double xVel =
                                            robotState.getLatestMeasuredFieldRelativeChassisSpeeds()
                                                    .vxMetersPerSecond;

                                    boolean isApproachingTrench =
                                            Math.signum(xVel) * Math.signum(xDist)
                                                    < 0; // positive = moving into trench
                                    return isApproachingTrench;
                                }
                            }
                            return false;
                        })
                .and(isTeleop)
                .onTrue(HoodFactory.stow(this));
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

    private final AutoDashboard autoDashboard;

    public Command getAutonomousCommand() {
        return autoDashboard.getSelectedAuto();
    }

    public Command getTestCommand() {
        return null;
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
