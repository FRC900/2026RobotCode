package com.team900.frc2026;

import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class Constants {

    public static final class SensorConstants {
        // placeholder constant. Make sure to tune.
        public static final int kAmpPostChopstickSensorPort = 4;
        public static final int kShooterStage1BannerSensorPort = 3;
        public static final int kAmpBannerSensorPort = 2;
        public static final int kFeederBannerSensorPort = 1;
        public static final int kIntakeBannerSensorPort = 0;
        public static final double kShooterDebounceTime = 0.01;
        public static final double kAmpDebounceTime = 0.01;
        public static final double kFeederDebounceTime = 0.01;
        public static final double kIntakeDebounceTime = 0.01;
    }

    public static final ServoMotorSubsystemConfig kShooterBottomConfig =
            new ServoMotorSubsystemConfig();
    public static final ServoMotorSubsystemConfig kShooterTopTopConfig =
            new ServoMotorSubsystemConfig();
    public static final ServoMotorSubsystemConfig kShooterTopBottomConfig =
            new ServoMotorSubsystemConfig();
    public static final ServoMotorSubsystemWithFollowersConfig kShooterTopConfig =
            new ServoMotorSubsystemWithFollowersConfig();

    public static final double kFieldLengthMeters = 8.07;

    public static final SimControllerType kSimControllerType = SimControllerType.XBOX;

    public enum SimControllerType {
        XBOX,
        DUAL_SENSE
    }

    public static final class ShooterConstants {
        // Placeholder values. Make sure to tuen.
        public static final Rotation2d kTurretToShotCorrection =
                new Rotation2d(Units.degreesToRadians(1.5));

        public static final double kPoopMaxApexHeight = Units.inchesToMeters(160.0);

        public static final double kStage2ShooterWheelDiameter = Units.inchesToMeters(3.0); // in
        public static final double kStage1ShooterWheelDiameter = Units.inchesToMeters(2.0); // in

        public static final double kRingLaunchVelMetersPerSecPerRotPerSec = 0.141;
        public static final double kRingLaunchLiftCoeff =
                0.013; // Multiply by v^2 to get lift accel
        public static final double kShooterStage2RPSShortRange = 120.0; // rot/s
        public static final double kShooterStage2MaxShortRangeDistance = 2.0;
        public static final double kShooterStage2MinLongRangeDistance = 3.0;
        public static final double kShooterStage2RPSLongRange = 120.0; // rot/s
        public static final double kShooterStage2RPSCap = 130.0; // rot/s
        public static final double kShooterStage1RPS = 70.0; // rot/s
        public static final double kShooterStage2Epsilon = 3.0;
        public static final double kShooterSpinupStage1RPS = 0.0;

        public static final double kShooterStage1IntakeRPS = 4.0;
        public static final double kShooterStage1ExhaustRPS = -10.0;

        public static final double kFenderShotRPS = 100.0;
        public static final double kPreloadShotRPS = 90.0;

        public static final double kBottomRollerSpeedupFactor = 1.0;
        public static final double kTopRollerSpeedupFactor = 1.0;
    }
}
