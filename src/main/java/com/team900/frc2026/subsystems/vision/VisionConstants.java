package com.team900.frc2026.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;

public class VisionConstants {

        // Large variance used to downweight unreliable vision measurements
        public static final double kLargeVariance = 1e6;

        // Standard deviation constants
        public static final int kMegatag1XStdDevIndex = 0;
        public static final int kMegatag1YStdDevIndex = 1;
        public static final int kMegatag1YawStdDevIndex = 5;

        // Standard deviation array indices for Megatag2
        public static final int kMegatag2XStdDevIndex = 6;
        public static final int kMegatag2YStdDevIndex = 7;
        public static final int kMegatag2YawStdDevIndex = 11;

        // Validation constants
        public static final int kExpectedStdDevArrayLength = 12;

        public static final int kMinFiducialCount = 1;
        //TODO: fix these gosh darn constants 
        // Camera A (Left-side Camera)
        public static final double kCameraAPitchDegrees = 20.0;
        public static final double kCameraAPitchRads = Units.degreesToRadians(kCameraAPitchDegrees);
        public static final double kCameraAHeightOffGroundMeters = Units.inchesToMeters(8.3787);
        public static final String kROSATableName = "limelight-left";
        public static final double kRobotToCameraAForward = Units.inchesToMeters(7.8757);
        public static final double kRobotToCameraASide = Units.inchesToMeters(-11.9269);
        public static final Rotation2d kCameraAYawOffset = Rotation2d.fromDegrees(0.0);
        public static final Transform2d kRobotToCameraA =
                new Transform2d(
                        new Translation2d(kRobotToCameraAForward, kRobotToCameraASide),
                        kCameraAYawOffset);

        // Camera B (Right-side camera)
        public static final double kCameraBPitchDegrees = 20.0;
        public static final double kCameraBPitchRads = Units.degreesToRadians(kCameraBPitchDegrees);
        public static final double kCameraBHeightOffGroundMeters = Units.inchesToMeters(8.3787);
        public static final String kROSBTableName = "limelight-right";
        public static final double kRobotToCameraBForward = Units.inchesToMeters(7.8757);
        public static final double kRobotToCameraBSide = Units.inchesToMeters(11.9269);
        public static final Rotation2d kCameraBYawOffset = Rotation2d.fromDegrees(0.0);
        public static final Transform2d kRobotToCameraB =
                new Transform2d(
                        new Translation2d(kRobotToCameraBForward, kRobotToCameraBSide),
                        kCameraBYawOffset);

        // Camera T (Turret camera)
        public static final double kCameraTPitchDegrees = 20.0;
        public static final double kCameraTPitchRads = Units.degreesToRadians(kCameraBPitchDegrees);
        public static final double kCameraTHeightOffGroundMeters = Units.inchesToMeters(8.3787);
        public static final String kROSTTableName = "limelight-turret";
        public static final double kRobotToCameraTForward = Units.inchesToMeters(7.8757);
        public static final double kRobotToCameraTSide = Units.inchesToMeters(11.9269);
        public static final Rotation2d kCameraTYawOffset = Rotation2d.fromDegrees(0.0);
        public static final Transform2d kRobotToCameraT =
                new Transform2d(
                        new Translation2d(kRobotToCameraBForward, kRobotToCameraBSide),
                        kCameraBYawOffset);

        // Vision processing constants
        public static final double kDefaultAmbiguityThreshold = 0.19;
        public static final double kDefaultYawDiffThreshold = 5.0;
        public static final double kTagAreaThresholdForYawCheck = 2.0;
        public static final double kTagMinAreaForSingleTagMegatag = 1.0;
        public static final double kDefaultZThreshold = 0.2;
        public static final double kDefaultNormThreshold = 1.0;
        public static final double kMinAmbiguityToFlip = 0.08;

        public static final double kCameraHorizontalFOVDegrees = 81.0;
        public static final double kCameraVerticalFOVDegrees = 55.0;
        public static final int kCameraImageWidth = 1280;
        public static final int kCameraImageHeight = 800;

        public static final double kScoringConfidenceThreshold = 0.7;

        // NetworkTables constants
        public static final String kBoundingBoxTableName = "BoundingBoxes";
}
