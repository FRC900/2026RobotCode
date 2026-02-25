package com.team900.frc2026.subsystems.drive;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.signals.*;
import com.ctre.phoenix6.swerve.*;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.*;
import edu.wpi.first.units.measure.*;

/**
 * Swerve module constants.
 *
 * <p>Drive: Kraken X60 FOC, MK5n (made a choosable ratio with kDriveRatioSelection - Currently R2)
 * Steer: Kraken X44, gear ratio 287:11 (~26.09:1) CANivore
 */
public class TunerConstants {

    // PID gains(tune later)
    private static final Slot0Configs steerGains =
            new Slot0Configs()
                    .withKP(100)
                    .withKI(0)
                    .withKD(0.5)
                    .withKS(0.1)
                    .withKV(0.0)
                    .withKA(0)
                    .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);

    // Drive motor gains (tune later)
    private static final Slot0Configs driveGains =
            new Slot0Configs().withKP(10.0).withKI(0.0).withKD(0.0).withKS(1.5).withKV(0.0);

    // Closed loop output types
    private static final ClosedLoopOutputType kSteerClosedLoopOutput = ClosedLoopOutputType.Voltage;
    private static final ClosedLoopOutputType kDriveClosedLoopOutput =
            ClosedLoopOutputType.TorqueCurrentFOC;

    // Motor types
    private static final DriveMotorArrangement kDriveMotorType =
            DriveMotorArrangement.TalonFX_Integrated;
    private static final SteerMotorArrangement kSteerMotorType =
            SteerMotorArrangement.TalonFX_Integrated;

    // Feedback
    private static final SteerFeedbackType kSteerFeedbackType = SteerFeedbackType.FusedCANcoder;

    // Stator current at which wheels start to slip (tune per robot)
    private static final Current kSlipCurrent = Amps.of(120.0);

    // Motor Configs
    private static final TalonFXConfiguration driveInitialConfigs =
            new TalonFXConfiguration()
                    .withCurrentLimits(
                            new CurrentLimitsConfigs()
                                    .withSupplyCurrentLimit(Amps.of(60))
                                    .withSupplyCurrentLimitEnable(true)
                                    .withStatorCurrentLimit(Amps.of(120))
                                    .withStatorCurrentLimitEnable(true))
                    .withMotorOutput(
                            new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    // Kraken X44 for steering
    private static final TalonFXConfiguration steerInitialConfigs =
            new TalonFXConfiguration()
                    .withCurrentLimits(
                            new CurrentLimitsConfigs()
                                    .withSupplyCurrentLimit(Amps.of(30))
                                    .withSupplyCurrentLimitEnable(true)
                                    .withStatorCurrentLimit(Amps.of(60))
                                    .withStatorCurrentLimitEnable(true))
                    .withMotorOutput(
                            new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake));

    private static final CANcoderConfiguration encoderInitialConfigs = new CANcoderConfiguration();

    private static final Pigeon2Configuration pigeonConfigs = null;

    // CAN Bus
    public static final CANBus kCANBus = new CANBus("drivebase", "./logs/example.hoot");

    // Mech Constants
    // R1 = 8.10:1 (14T pinion), R2 = 6.54:1 (16T pinion), R3 = 5.36:1 (18T pinion)
    private enum MK5nDriveRatio {
        R1(14),
        R2(16),
        R3(18);

        final int pinionTeeth;

        MK5nDriveRatio(int pinionTeeth) {
            this.pinionTeeth = pinionTeeth;
        }
    }

    // Drive Ratio Choice
    private static final MK5nDriveRatio kDriveRatioSelection = MK5nDriveRatio.R2;

    // Drive gear ratio computed from pinion selection
    // MK5n formula: (pinionTeeth/54) * (32/25) * (15/30)
    private static final double kDriveGearRatio =
            1.0 / ((kDriveRatioSelection.pinionTeeth / 54.0) * (32.0 / 25.0) * (15.0 / 30.0));

    // MK5n steering gear ratio: 287:11 = ~26.09:1
    // motor rotations per azimuth rotation
    private static final double kSteerGearRatio = 287.0 / 11.0;

    // MK5n wheel radius
    private static final Distance kWheelRadius = Inches.of(2.0);

    // Coupling ratio: every 1 azimuth rotation causes this many drive motor turns
    // Derived from the 54T bevel gear and the selected pinion
    private static final double kCoupleRatio = 54.0 / kDriveRatioSelection.pinionTeeth;

    // Theoretical free speed at 12V: Kraken X60 FOC (5800 RPM) / 6.03 ratio = 962 RPM wheel = ~5.12
    // m/s with 2" radius wheel
    public static final LinearVelocity kSpeedAt12Volts = MetersPerSecond.of(5.12);

    // Drive inversions
    private static final boolean kInvertLeftSide = false;
    private static final boolean kInvertRightSide = true;

    // Pigeon 2 IMU
    // TODO: EDIT ID
    private static final int kPigeonId = 13;

    // Simulation Constants
    private static final MomentOfInertia kSteerInertia = KilogramSquareMeters.of(0.01);
    private static final MomentOfInertia kDriveInertia = KilogramSquareMeters.of(0.01);
    private static final Voltage kSteerFrictionVoltage = Volts.of(0.2);
    private static final Voltage kDriveFrictionVoltage = Volts.of(0.2);

    // Drivetrain Constants
    public static final SwerveDrivetrainConstants DrivetrainConstants =
            new SwerveDrivetrainConstants()
                    .withCANBusName(kCANBus.getName())
                    .withPigeon2Id(kPigeonId)
                    .withPigeon2Configs(pigeonConfigs);

    // Module Constants Factory
    private static final SwerveModuleConstantsFactory<
                    TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>
            ConstantCreator =
                    new SwerveModuleConstantsFactory<
                                    TalonFXConfiguration,
                                    TalonFXConfiguration,
                                    CANcoderConfiguration>()
                            .withDriveMotorGearRatio(kDriveGearRatio)
                            .withSteerMotorGearRatio(kSteerGearRatio)
                            .withCouplingGearRatio(kCoupleRatio)
                            .withWheelRadius(kWheelRadius)
                            .withSteerMotorGains(steerGains)
                            .withDriveMotorGains(driveGains)
                            .withSteerMotorClosedLoopOutput(kSteerClosedLoopOutput)
                            .withDriveMotorClosedLoopOutput(kDriveClosedLoopOutput)
                            .withSlipCurrent(kSlipCurrent)
                            .withSpeedAt12Volts(kSpeedAt12Volts)
                            .withDriveMotorType(kDriveMotorType)
                            .withSteerMotorType(kSteerMotorType)
                            .withFeedbackSource(kSteerFeedbackType)
                            .withDriveMotorInitialConfigs(driveInitialConfigs)
                            .withSteerMotorInitialConfigs(steerInitialConfigs)
                            .withEncoderInitialConfigs(encoderInitialConfigs)
                            .withSteerInertia(kSteerInertia)
                            .withDriveInertia(kDriveInertia)
                            .withSteerFrictionVoltage(kSteerFrictionVoltage)
                            .withDriveFrictionVoltage(kDriveFrictionVoltage);

    // Module CAN IDs and encoder offsets
    // TODO: Update these with axctual CANIDS and Encoder Offsets

    // Front Left
    private static final int kFrontLeftDriveMotorId = 1;
    private static final int kFrontLeftSteerMotorId = 2;
    private static final int kFrontLeftEncoderId = 9;
    private static final Angle kFrontLeftEncoderOffset = Rotations.of(0.0); // TODO: calibrate
    private static final boolean kFrontLeftSteerMotorInverted = true;
    private static final boolean kFrontLeftEncoderInverted = false;
    private static final Distance kFrontLeftXPos = Inches.of(10.875);
    private static final Distance kFrontLeftYPos = Inches.of(10.875);

    // Front Right
    private static final int kFrontRightDriveMotorId = 3;
    private static final int kFrontRightSteerMotorId = 4;
    private static final int kFrontRightEncoderId = 10;
    private static final Angle kFrontRightEncoderOffset = Rotations.of(0.0); // TODO: calibrate
    private static final boolean kFrontRightSteerMotorInverted = true;
    private static final boolean kFrontRightEncoderInverted = false;
    private static final Distance kFrontRightXPos = Inches.of(10.875);
    private static final Distance kFrontRightYPos = Inches.of(-10.875);

    // Back Left
    private static final int kBackLeftDriveMotorId = 5;
    private static final int kBackLeftSteerMotorId = 6;
    private static final int kBackLeftEncoderId = 11;
    private static final Angle kBackLeftEncoderOffset = Rotations.of(0.0); // TODO: calibrate
    private static final boolean kBackLeftSteerMotorInverted = true;
    private static final boolean kBackLeftEncoderInverted = false;
    private static final Distance kBackLeftXPos = Inches.of(-10.875);
    private static final Distance kBackLeftYPos = Inches.of(10.875);

    // Back Right
    private static final int kBackRightDriveMotorId = 7;
    private static final int kBackRightSteerMotorId = 8;
    private static final int kBackRightEncoderId = 12;
    private static final Angle kBackRightEncoderOffset = Rotations.of(0.0); // TODO: calibrate
    private static final boolean kBackRightSteerMotorInverted = true;
    private static final boolean kBackRightEncoderInverted = false;
    private static final Distance kBackRightXPos = Inches.of(-10.875);
    private static final Distance kBackRightYPos = Inches.of(-10.875);

    // --- Module Constants ---
    public static final SwerveModuleConstants<
                    TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>
            FrontLeft =
                    ConstantCreator.createModuleConstants(
                            kFrontLeftSteerMotorId,
                            kFrontLeftDriveMotorId,
                            kFrontLeftEncoderId,
                            kFrontLeftEncoderOffset,
                            kFrontLeftXPos,
                            kFrontLeftYPos,
                            kInvertLeftSide,
                            kFrontLeftSteerMotorInverted,
                            kFrontLeftEncoderInverted);

    public static final SwerveModuleConstants<
                    TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>
            FrontRight =
                    ConstantCreator.createModuleConstants(
                            kFrontRightSteerMotorId,
                            kFrontRightDriveMotorId,
                            kFrontRightEncoderId,
                            kFrontRightEncoderOffset,
                            kFrontRightXPos,
                            kFrontRightYPos,
                            kInvertRightSide,
                            kFrontRightSteerMotorInverted,
                            kFrontRightEncoderInverted);

    public static final SwerveModuleConstants<
                    TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>
            BackLeft =
                    ConstantCreator.createModuleConstants(
                            kBackLeftSteerMotorId,
                            kBackLeftDriveMotorId,
                            kBackLeftEncoderId,
                            kBackLeftEncoderOffset,
                            kBackLeftXPos,
                            kBackLeftYPos,
                            kInvertLeftSide,
                            kBackLeftSteerMotorInverted,
                            kBackLeftEncoderInverted);

    public static final SwerveModuleConstants<
                    TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration>
            BackRight =
                    ConstantCreator.createModuleConstants(
                            kBackRightSteerMotorId,
                            kBackRightDriveMotorId,
                            kBackRightEncoderId,
                            kBackRightEncoderOffset,
                            kBackRightXPos,
                            kBackRightYPos,
                            kInvertRightSide,
                            kBackRightSteerMotorInverted,
                            kBackRightEncoderInverted);

    /** Creates the CommandSwerveDrivetrain constants container. */
    public static CommandSwerveDrivetrain createDrivetrain() {
        return new CommandSwerveDrivetrain(
                DrivetrainConstants, FrontLeft, FrontRight, BackLeft, BackRight);
    }
}
