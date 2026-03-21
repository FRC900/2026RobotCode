package com.team900.frc2026.subsystems.shooter;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.frc2026.Constants.Gains;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig.FollowerConfig;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class ShooterConstants {

    public static final ServoMotorSubsystemWithFollowersConfig kShooterConfig =
            new ServoMotorSubsystemWithFollowersConfig();
    public static final FollowerConfig kShooterLeftConfig = new FollowerConfig();

    public static final Gains gains = new Gains(4.5, 0, 0, 0, 0.203, 0, 0);

    static {
        kShooterConfig.name = "Shooter Right";
        kShooterConfig.talonCANID = new CANDeviceId(55, Constants.kCanBusCanivoreMech);
        kShooterConfig.unitToRotorRatio = 1;

        kShooterConfig.momentOfInertia = 0.0011720789;

        kShooterConfig.fxConfig = new TalonFXConfiguration();
        kShooterConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kShooterConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
        kShooterConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kShooterConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
        kShooterConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kShooterConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kShooterConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kShooterConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kShooterConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        kShooterConfig.fxConfig.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.05;

        kShooterConfig.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 150;
        kShooterConfig.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = -150;

        kShooterConfig.fxConfig.Slot0.kA = gains.ffkA();
        kShooterConfig.fxConfig.Slot0.kD = gains.kD();
        kShooterConfig.fxConfig.Slot0.kG = gains.ffkG();
        kShooterConfig.fxConfig.Slot0.kI = gains.kI();
        kShooterConfig.fxConfig.Slot0.kP = gains.kP();
        kShooterConfig.fxConfig.Slot0.kS = gains.ffkS();
        kShooterConfig.fxConfig.Slot0.kV = gains.ffkV();

        kShooterLeftConfig.config.name = "Shooter Left";
        kShooterLeftConfig.inverted = true;
        kShooterLeftConfig.config.momentOfInertia = 0.0011720789;
        kShooterLeftConfig.config.talonCANID = new CANDeviceId(56, new CANBus("mech"));
        kShooterLeftConfig.config.unitToRotorRatio = 1.;

        kShooterLeftConfig.config.fxConfig = new TalonFXConfiguration();

        kShooterConfig.fxConfig.Feedback.SensorToMechanismRatio = 14. / 18.;
        kShooterConfig.fxConfig.Feedback.RotorToSensorRatio = 1.;

        kShooterLeftConfig.config.fxConfig.Slot0.kA = gains.ffkA();
        kShooterLeftConfig.config.fxConfig.Slot0.kD = gains.kD();
        kShooterLeftConfig.config.fxConfig.Slot0.kG = gains.ffkG();
        kShooterLeftConfig.config.fxConfig.Slot0.kI = gains.kI();
        kShooterLeftConfig.config.fxConfig.Slot0.kP = gains.kP();
        kShooterLeftConfig.config.fxConfig.Slot0.kS = gains.ffkS();
        kShooterLeftConfig.config.fxConfig.Slot0.kV = gains.ffkV();

        kShooterLeftConfig.config.fxConfig.OpenLoopRamps =
                Constants.makeDefaultOpenLoopRampConfig();
        kShooterLeftConfig.config.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
        kShooterLeftConfig.config.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kShooterLeftConfig.config.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kShooterLeftConfig.config.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        kShooterLeftConfig.config.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 150;
        kShooterLeftConfig.config.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = -150;

        kShooterConfig.followers =
                new ServoMotorSubsystemWithFollowersConfig.FollowerConfig[] {kShooterLeftConfig};
    }

    public static final double kShooterGearRatio = 1;
    public static final double kIdleRPS = 1200. / 60.;
    public static final double kCloseShotRPS = 3700. / 60.;
    public static final double kFarShotRPS = 4200. / 60.;
    public static final double kFeedingRPS = 4200. / 60.;
    public static final double kPassingRPS = 4800. / 60.;
    public static final Rotation2d kTurretToShotCorrection =
            Rotation2d.fromRadians(Units.degreesToRadians(0));

    public static final double kLaunchVelMetersPerSecPerRotPerSec = 0.141;
    public static final double kShooterRPSCap = 5500. / 60.;
}
