package com.team900.frc2026.subsystems.shooter;

import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithFollowersConfig.FollowerConfig;

public class ShooterConstants {

    public static ServoMotorSubsystemWithFollowersConfig kShooterConfig =
            new ServoMotorSubsystemWithFollowersConfig();

    static {
        // kShooterConfig.fxConfig = ;
        // kShooterConfig.kMaxPositionUnits;
        // kShooterConfig.kMinPositionUnits;
        // kShooterConfig.momentOfInertia;
        // kShooterConfig.name;
        // kShooterConfig.talonCANID;
        // kShooterConfig.unitToRotorRatio;
        kShooterConfig.followers[0] = new FollowerConfig();
    }

    public static final double kIdleRPM = 2000.0;
}
