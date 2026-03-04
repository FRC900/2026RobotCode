package com.team900.frc2026.viz;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class RobotViz {
    private RobotState state = RobotState.getInstance();

    private Pose3d hoodPose3d = new Pose3d();
    private Pose3d intakePose3d = new Pose3d();
    private Pose3d shooterPose3d = new Pose3d();
    // Offset from hood center to the hood's pivot point (in meters)

        private static final Translation3d HOOD_PIVOT_OFFSET = new Translation3d(0.1, 0.0, 0.3);

    public RobotViz()   {}

    public void updateViz() {
         if (state.getLatestRobotToTurret().getValue() != null) {

                  double turretRotation = state.getLatestRobotToTurret().getValue().getRadians();

            shooterPose3d = new Pose3d(new Translation3d(), new Rotation3d(0,0,turretRotation));
         // Rotate the hood's pivot offset by the turret angle,
            // then translate the hood to that rotated pivot position
            Translation3d rotatedHoodPivot = HOOD_PIVOT_OFFSET.rotateBy( new Rotation3d(0,0,turretRotation));
                        hoodPose3d = new Pose3d(rotatedHoodPivot, new Rotation3d(0, Units.rotationsToRadians(state.getHoodRotations()), turretRotation));

        }

        intakePose3d = new Pose3d(new Translation3d(), new Rotation3d(0,Units.rotationsToRadians(state.getIntakePivotRotations()),0));
    

        Logger.recordOutput("ComponentsPoseArray",
                new Pose3d[] { intakePose3d, shooterPose3d, hoodPose3d});
    
}

}