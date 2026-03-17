package com.team900.lib.util;

import com.team900.frc2026.subsystems.turret.TurretConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class TurretAlignUtil {
    private Pose2d currentPose;
    private double hub_x;
    private double hub_y;

    public static boolean isRedAlliance() {
        return DriverStation.getAlliance()
            .orElse(DriverStation.Alliance.Blue) == DriverStation.Alliance.Red;
    }
    
    public TurretAlignUtil(Pose2d currentPose) {
        this.currentPose = currentPose;
        Translation2d hub = AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();
        hub_x = hub.getX();
        hub_y = hub.getY();
    }

    public Pose2d getTurretPositionFromRobotPose() {
        double robotX = currentPose.getX();
        double robotY = currentPose.getY();
        Rotation2d robotRot = currentPose.getRotation();

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
        Pose2d turretPose = getTurretPositionFromRobotPose();
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
