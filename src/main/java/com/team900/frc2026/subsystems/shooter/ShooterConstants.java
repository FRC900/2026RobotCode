package com.team900.frc2026.subsystems.shooter;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig.FollowerConfig;

public class ShooterConstants {


    public static ServoMotorSubsystemWithFollowersConfig kShooterConfig = new ServoMotorSubsystemWithFollowersConfig();
    public static ServoMotorSubsystemWithFollowersConfig.FollowerConfig kShooterLeftConfig = new FollowerConfig();

    static {
        kShooterConfig.name = "Shooter Right";
        kShooterConfig.talonCANID = new CANDeviceId(55, new CANBus("mech"));
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
        kShooterConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        kShooterConfig.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 150;
                kShooterConfig.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = 150;





        kShooterLeftConfig.config.name = "Shooter Left";
        kShooterLeftConfig.inverted = false;
        kShooterLeftConfig.config.momentOfInertia = 1;
        kShooterLeftConfig.config.talonCANID = new CANDeviceId(56, new CANBus("mech"));
        kShooterLeftConfig.config.unitToRotorRatio = 1;

kShooterLeftConfig.config.fxConfig = new TalonFXConfiguration();
        kShooterLeftConfig.config.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kShooterLeftConfig.config.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
                kShooterLeftConfig.config.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
                        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
                                kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
                                        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
                                        kShooterLeftConfig.config.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kShooterLeftConfig.config.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kShooterLeftConfig.config.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        kShooterLeftConfig.config.fxConfig.TorqueCurrent.PeakForwardTorqueCurrent = 150;
                kShooterLeftConfig.config.fxConfig.TorqueCurrent.PeakReverseTorqueCurrent = 150;



                kShooterConfig.followers[0] = kShooterLeftConfig;
                


    }
    public static final double kShooterGearRatio = 1;
    public static final double kShooterDutyCycle = 0.5;
    public static final double kShooterDutyCycleExhaust = -0.5;
}