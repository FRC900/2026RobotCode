package com.team900.frc2026.subsystems.turret;

import com.team900.frc2026.Constants;
import com.team900.frc2026.Constants.Gains;
import com.team900.lib.drivers.CANDeviceId;
import edu.wpi.first.math.util.Units;

public class TurretConstants {

    // 14t:42t  24:176t  overall: 22:1
    public static final double kTurretGearRatio = 22.0;
    // TODO: Check this canID just for the Talon
    public static final CANDeviceId kTurretTalonCanID =
            new CANDeviceId(41, Constants.kCanBusCanivoreMech);
    public static final CANDeviceId kTurret33To1CANCoder =
            new CANDeviceId(32, Constants.kCanBusCanivoreMech);
    public static final CANDeviceId kTurret29To1CANCoder =
            new CANDeviceId(31, Constants.kCanBusCanivoreMech);
    // TODO: Update these offsets, 0 should be facing directly forward.
    public static final double k33To1TurretCancoderOffset = 0;
    public static final double k29To1TurretCancoderOffset = 0;

    // 33to1 cancoder: 33 rotations to 170 turret rotations
    // 29to1 cancoder: 29 rotations to 170 turret rotations
    // Both are direct — no additional gear stages
    public static final int kCRTRatio33 = 33;
    public static final int kCRTRatio29 = 29;

    // TODO: Measure the actual hard stop positions on the robot and set these values
    // These are hard stop limits in radians from center (0 = forward)
    // clockwise is positive
    public static final double kTurretMinPositionRadians = 0;
    public static final double kTurretMaxPositionRadians = 3.0 * Math.PI / 2.0;

    public static final double turretOffSetFromCenterX =
            0.1524; // parallel to the front bumpers, need to tune this
    public static final double turretOffSetFromCenterY =
            -0.148908; // parallel to the side bumpers, need to tune this

    // Software buffer so we don't slam into the hard stops (back off by this amount)
    public static final double kSoftwareLimitBufferRadians = Units.degreesToRadians(5.0);
    public static final double kTurretSoftMinRadians =
            kTurretMinPositionRadians + kSoftwareLimitBufferRadians;
    public static final double kTurretSoftMaxRadians =
            kTurretMaxPositionRadians - kSoftwareLimitBufferRadians;

    public static final double kTurretEpsilon = Units.degreesToRadians(2.0);
    public static final double kTurretShootingEpsilon = Units.degreesToRadians(3.0);

    public static final double toleranceRad = 0.1;

    public static final Gains COMP_GAINS = new Gains(0, 0, 0, 0, 0, 0, 0);
}
