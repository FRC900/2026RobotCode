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

public class ShooterConstants {

    public static final ServoMotorSubsystemWithFollowersConfig kShooterConfig =
            new ServoMotorSubsystemWithFollowersConfig();
    public static final FollowerConfig kShooterLeftConfig = new FollowerConfig();

    public static final Gains gains = new Gains(0, 0, 0, 0, 0, 0, 0);

    static {
        kShooterConfig.name = "Shooter Right";
        kShooterConfig.talonCANID = new CANDeviceId(40, Constants.kCanBusCanivoreMech);
        kShooterConfig.unitToRotorRatio = 1;

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

        kShooterConfig.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 150;
        kShooterConfig.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = 150;

        kShooterConfig.fxConfig.Slot0.kA = gains.ffkA();
        kShooterConfig.fxConfig.Slot0.kD = gains.kD();
        kShooterConfig.fxConfig.Slot0.kG = gains.ffkG();
        kShooterConfig.fxConfig.Slot0.kI = gains.kI();
        kShooterConfig.fxConfig.Slot0.kP = gains.kP();
        kShooterConfig.fxConfig.Slot0.kS = gains.ffkS();
        kShooterConfig.fxConfig.Slot0.kV = gains.ffkV();

        kShooterLeftConfig.config.name = "Shooter Left";
        kShooterLeftConfig.inverted = false;
        kShooterLeftConfig.config.momentOfInertia = 1;
        kShooterLeftConfig.config.talonCANID = new CANDeviceId(41, new CANBus("mech"));
        kShooterLeftConfig.config.unitToRotorRatio = 1;

        kShooterLeftConfig.config.fxConfig = new TalonFXConfiguration();
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
        kShooterLeftConfig.config.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = 150;

        kShooterConfig.followers[0] = kShooterLeftConfig;

        kShooterLeftConfig.config.fxConfig.Slot0.kA = gains.ffkA();
        kShooterLeftConfig.config.fxConfig.Slot0.kD = gains.kD();
        kShooterLeftConfig.config.fxConfig.Slot0.kG = gains.ffkG();
        kShooterLeftConfig.config.fxConfig.Slot0.kI = gains.kI();
        kShooterLeftConfig.config.fxConfig.Slot0.kP = gains.kP();
        kShooterLeftConfig.config.fxConfig.Slot0.kS = gains.ffkS();
        kShooterLeftConfig.config.fxConfig.Slot0.kV = gains.ffkV();
    }

    public static final double kShooterGearRatio = 1;
    public static final double kIdleRPM = 2000;
    public static final double kShootingRPM = 3500;
    public static final double kFeedingRPM = 5500;
}
