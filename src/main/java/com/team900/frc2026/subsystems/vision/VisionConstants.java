package com.team900.frc2026.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class VisionConstants {

    // ---- Camera names (must match coprocessor config) ----
    public static String camera0Name = "TurretCamera";

    // ---- Basic filtering thresholds ----
    public static double maxAmbiguity = 0.3;
    public static double maxZError = 0.75;

    // ---- Standard deviation baselines (for 1 m distance, 1 tag) ----
    public static double linearStdDevBaseline = 0.02; // Meters
    public static double angularStdDevBaseline = 0.06; // Radians

    // Per-camera std dev multipliers
    public static double[] cameraStdDevFactors =
            new double[] {
                1.0, // Camera 0
                1.0 // Camera 1
            };

    // Multipliers for PINHOLE / single-tag observations
    public static double linearStdDevPinholeFactor = 0.5;
    public static double angularStdDevPinholeFactor =
            Double.POSITIVE_INFINITY; // No reliable rotation from pinhole

    // ---- Angular velocity rejection thresholds ----
    public static double angularVelocityTimeWindowSec = 0.1;
    public static double maxYawAngularVelocityForSolvePNP =
            Units.degreesToRadians(200.0);
    public static double maxYawAngularVelocityForPinhole =
            Units.degreesToRadians(100.0);
    public static double maxPitchAngularVelocityForPinhole =
            Units.degreesToRadians(10.0);
    public static double maxRollAngularVelocityForPinhole =
            Units.degreesToRadians(10.0);

    // ---- Tiered std dev parameters ----
    public static double maxPoseDifferenceForTightStdDev = 0.3; // meters
    public static double maxPoseDifferenceForMediumStdDev = 0.5; // meters

    // SolvePNP tiered std devs
    public static double solvePnpMultiTagTightXY = 0.2;
    public static double solvePnpMultiTagLooseXY = 0.5;
    public static double solvePnpSingleTagCloseXY = 0.5;
    public static double solvePnpSingleTagFarXY = 1.0;
    public static double solvePnpFallbackXY = 2.0;
    public static double solvePnpMultiTagDegStdDev = 6.0;
    public static double solvePnpSingleTagDegStdDev = 12.0;
    public static double solvePnpFallbackDegStdDev = 30.0;

    // Pinhole tiered std devs
    public static double pinholeMultiTagTightXY = 0.2;
    public static double pinholeSingleTagCloseXY = 0.5;
    public static double pinholeSingleTagFarXY = 1.0;
    public static double pinholeFallbackXY = 2.0;
    public static double pinholeRotStdDevDeg = 50.0;

    // Tag area thresholds
    public static double tagAreaThresholdLarge = 0.8;
    public static double tagAreaThresholdMedium = 0.1;

    // ---- Camera T (Turret camera) physical constants ----
    public static final double kCameraTPitchDegrees = 30.0;
    public static final double kCameraTPitchRads =
            Units.degreesToRadians(kCameraTPitchDegrees);
    public static final double kCameraTHeightOffGroundMeters =
            Units.inchesToMeters(18.25);
    public static final Rotation2d kCameraTYawOffset = Rotation2d.fromDegrees(180);

    public static final double kRobotToCameraTForward =
            Units.inchesToMeters(-12.235063);
    public static final double kRobotToCameraTSide = Units.inchesToMeters(-5.863);
    public static final Transform2d kRobotToCameraT =
            new Transform2d(
                    new Translation2d(kRobotToCameraTForward, kRobotToCameraTSide),
                    kCameraTYawOffset);

    public static Transform3d robotToCamera0 =
            new Transform3d(
                    kRobotToCameraTForward,
                    kRobotToCameraTSide,
                    kCameraTHeightOffGroundMeters,
                    new Rotation3d(
                            0,
                            Units.degreesToRadians(kCameraTPitchDegrees),
                            kCameraTYawOffset.getRadians()));

    public static final String kROSTTableName = "limelight-turret";
    public static final double kTurretToCameraXMeters = 0;
    public static final double kTurretToCameraYMeters = 0;

    // Static turret-to-camera transform (used by the dynamic supplier for turret
    // cameras). Adjust these values to represent the camera's position relative to
    // the turret pivot, NOT relative to the robot origin.
    public static final Transform3d turretToCamera0 =
            new Transform3d(
                    kTurretToCameraXMeters,
                    kTurretToCameraYMeters,
                    kCameraTHeightOffGroundMeters,
                    new Rotation3d(
                            0,
                            Units.degreesToRadians(kCameraTPitchDegrees),
                            kCameraTYawOffset.getRadians()));
}
