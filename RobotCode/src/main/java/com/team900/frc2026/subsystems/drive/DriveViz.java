package com.team900.frc2026.subsystems.drive;

import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;
import com.team254.lib.util.MathHelpers;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.littletonrobotics.junction.Logger;

/** Telemetry visualization for the swerve drivetrain
 * Following the 254 DriveViz pattern. 
 * */

public class DriveViz {
    private final double maxSpeed;
    private final Field2d field = new Field2d();

    private Pose2d lastPose = MathHelpers.kPose2dZero;
    private double lastTime = Logger.getTimestamp();

    public DriveViz(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        SmartDashboard.putData("Field", field);
    }

    public void telemeterize(SwerveDriveState state) {
        if (state == null || state.Pose == null || state.ModuleStates == null) {
            return;
        }
        
        Pose2d pose = state.Pose;
        Logger.recordOutput("Drive/Viz/Pose", pose);

        Pose3d pose3d =
                new Pose3d(
                        pose.getX(),
                        pose.getY(),
                        0.0,
                        new Rotation3d(0.0, 0.0, pose.getRotation().getRadians()));
        Logger.recordOutput("Drive/Viz/Pose3d", pose3d);

        field.setRobotPose(pose);

        double currentTime = Logger.getTimestamp();
        double diffTime = currentTime - lastTime;
        lastTime = currentTime;
        Translation2d distanceDiff = pose.minus(lastPose).getTranslation();
        lastPose = pose;
        Translation2d velocities = distanceDiff.div(diffTime);

        Logger.recordOutput("Drive/Viz/Speed", velocities.getNorm());
        Logger.recordOutput("Drive/Viz/VelocityX", velocities.getX());
        Logger.recordOutput("Drive/Viz/VelocityY", velocities.getY());
        Logger.recordOutput("Drive/Viz/OdomPeriod", state.OdometryPeriod);
        Logger.recordOutput("Drive/Viz/ModuleStatesCurrent", state.ModuleStates);
    }
}
