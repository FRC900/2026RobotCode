package com.team900.frc2026.viz;

import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;

public class RobotViz {
    private RobotState state = RobotState.getInstance();

    private Pose3d turretPose3d = new Pose3d();
    private Pose3d hoodPose3d = new Pose3d();
    private Pose3d intakePose3d = new Pose3d();
    private Pose3d shooterPose3d = new Pose3d();

    public RobotViz()   {}

    public void updateViz() {
         if (state.getLatestRobotToTurret().getValue() != null) {
            shooterPose3d = new Pose3d(new Translation3d(),
                    new Rotation3d(0, 0,
                            state.getLatestRobotToTurret().getValue().getRadians()));
            hoodPose3d = new Pose3d(new Translation3d(),
                    new Rotation3d(0, 0,
                            state.getLatestRobotToTurret().getValue().getRadians()));
        }

        intakePose3d = new Pose3d(new Translation3d(), new Rotation3d(0,state.getIntakePivotRotations(),0));
    }
    
}
