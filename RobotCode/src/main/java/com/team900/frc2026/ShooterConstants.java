package com.team900.frc2026;

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class ShooterConstants {

        public static final double kBallReleaseHeight = Units.inchesToMeters(22.183);

        public static final CANBus CAN_BUS = CANBus.roboRIO();
        public static final Rotation2d kTurretToShotCorrection =
                new Rotation2d(Units.degreesToRadians(1.5));

        public static final double kPoopMaxApexHeight = Units.inchesToMeters(160.0);

        public static final double kStage2ShooterWheelDiameter = Units.inchesToMeters(3.0); // in
        public static final double kStage1ShooterWheelDiameter = Units.inchesToMeters(2.0); // in

        public static final double kRingLaunchVelMetersPerSecPerRotPerSec = 0.141;
        public static final double kRingLaunchLiftCoeff =
                0.013; // Multiply by v^2 to get lift accel
        public static final double kShooterTopRPSShortRange = 120.0; // rot/s
        public static final double kShooterTopMaxShortRangeDistance = 2.0;
        public static final double kShooterTopMinLongRangeDistance = 3.0;
        public static final double kShooterTopRPSLongRange = 120.0; // rot/s
        public static final double kShooterTopRPSCap = 130.0; // rot/s
        public static final double kShooterBottomRPS = 70.0; // rot/s
        public static final double kShooterTopEpsilon = 3.0;
        public static final double kShooterSpinupBottomRPS = 0.0;



        public static final double kFenderShotRPS = 100.0;
        public static final double kPreloadShotRPS = 90.0;


        public static final double kTopRollerSpeedupFactor = 1.0;
        public static final double kBottomRollerSpeedupFactor = 1.0;

}
