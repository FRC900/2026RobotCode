package com.team900.lib.util;

import com.team900.frc2026.subsystems.turret.TurretConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class TurretAlignFromPose {
    private Pose2d currentPose;
    private double hub_x;
    private double hub_y;

    public static boolean isRedAlliance() {
        return DriverStation.getAlliance()
            .orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Red;
    }
    
    public TurretAlignFromPose(Pose2d currentPose) {
        this.currentPose = currentPose;
        if (isRedAlliance()) {
            this.hub_x = 4.620001792907715;
            this.hub_y = 4.042872905731201;
        } else {
            this.hub_x = 11.919336318969727;
            this.hub_y = 4.034592628479004;
        }
    }

    public Pose2d getTurretPositionFromRobotPose(Pose2d robotPose) {
        double robotX = robotPose.getX();
        double robotY = robotPose.getY();
        Rotation2d robotRot = robotPose.getRotation();

        double offsetX = TurretConstants.turretOffSetFromCenterX;
        double offsetY = TurretConstants.turretOffSetFromCenterY;

        double cos = robotRot.getCos();
        double sin = robotRot.getSin();

        // rotate offset into field frame
        double turretX = robotX + (offsetX * cos - offsetY * sin);
        double turretY = robotY + (offsetX * sin + offsetY * cos);

        return new Pose2d(turretX, turretY, robotRot);
    }

    // 0 degrees is defined when the shooter is parallel to the side bumpers and is aimed forward
    // degrees are continous (i.e., instead of saying -90 degrees, we say 270)
    public double turretDegreesFromZero() {
        Pose2d turretPose = getTurretPositionFromRobotPose(currentPose);
        double turretX = turretPose.getX();
        double turretY = turretPose.getY();

        double turretXFromHub = hub_x - turretX;
        double turretYFromHub = hub_y - turretY;

        double angleRad = Math.atan2(turretYFromHub, turretXFromHub);
        double angleDeg = Math.toDegrees(angleRad);
        if (angleDeg < 0) {
            angleDeg += 360;
        }

        return angleDeg;
    }
}
