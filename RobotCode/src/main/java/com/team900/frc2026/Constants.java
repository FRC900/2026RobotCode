package com.team900.frc2026;

import com.team900.frc2026.subsystems.drive.CommandSwerveDrivetrain;
import com.team900.frc2026.subsystems.drive.TunerConstants;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;

public class Constants {
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : Mode.SIM;

    public enum Mode {
        REAL,
        SIM,
        REPLAY
    }

    public static final String kCANBusDrive = "drivebase";

    public static final double kRobotMassKg = Units.lbsToKilograms(125.0);
    public static final double kRobotMomentOfInertia = 6.0; // kg * m^2

    // Controls
    public static final int kDriverControllerPort = 0;
    public static final double kJoystickDeadband = 0.1;
    public static final double kTranslationExponent = 3.0;
    public static final double kRotationExponent = 2.0;
    public static final double kSlowModeScalar = 0.7; // how much left trigger reduces speed (1.0 = full stop)

    public static final class DriveConstants {
        // MK5n with Kraken X60 FOC, R2 ratio: ~16.8 ft/s = ~5.12 m/s free speed
        // Derate for real-world: ~4.5 m/s
        public static final double kDriveMaxSpeed = 4.5; // m/s
        public static final double kDriveMaxAngularRate = 8.0; // rad/s
        public static final double kMaxAccelerationMetersPerSecondSquared = 10.0;
        public static final double kMaxAngularAccelerationRadiansPerSecondSquared = 20.0;

        public static final double kHeadingControllerP = 5.0;
        public static final double kHeadingControllerI = 0.0;
        public static final double kHeadingControllerD = 0.0;

        public static final CommandSwerveDrivetrain kDrivetrain = TunerConstants.createDrivetrain();

        public static final double kWheelCoefficientOfFriction = 1.0;

        // Odometry standard deviations
        public static final double kDisabledDriveXStdDev = 1.0;
        public static final double kDisabledDriveYStdDev = 1.0;
        public static final double kDisabledDriveRotStdDev = 1.0;

        public static final double kEnabledDriveXStdDev = 0.3;
        public static final double kEnabledDriveYStdDev = 0.3;
        public static final double kEnabledDriveRotStdDev = 0.2;
    }

    public static final class AutoConstants {
        public static final double kMaxSpeedMetersPerSecond = 3.6;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3.0;
        public static final double kMaxAngularSpeedRadiansPerSecond = 6.5;
        public static final double kMaxAngularAccelerationRadiansPerSecondSquared = 20.0;

        public static final double kPTranslationController = 5.0;
        public static final double kPRotationController = 5.0;
    }
}
