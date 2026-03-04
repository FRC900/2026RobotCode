package com.team900.frc2026.subsystems.shooter;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig.FollowerConfig;

public class ShooterConstants {

    public static ServoMotorSubsystemWithFollowersConfig kShooterConfig =
            new ServoMotorSubsystemWithFollowersConfig();

    static {
        kShooterConfig.name = "Shooter";
        kShooterConfig.talonCANID = new CANDeviceId(55, Constants.kCanBusCanivoreMech);
        kShooterConfig.unitToRotorRatio = 1.0;
        kShooterConfig.fxConfig = new TalonFXConfiguration();
        kShooterConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kShooterConfig.followers = new FollowerConfig[] {new FollowerConfig()};
        kShooterConfig.followers[0].config.talonCANID =
                new CANDeviceId(56, Constants.kCanBusCanivoreMech);
        kShooterConfig.followers[0].config.fxConfig = new TalonFXConfiguration();
    }

    public static final double kIdleRPM = 2000.0;
}
