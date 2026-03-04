package com.team900.frc2026.viz;

import com.team900.frc2026.RobotState;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import org.littletonrobotics.junction.Logger;

public class RobotViz {
    private RobotState state = RobotState.getInstance();

    private Pose3d hoodPose3d = new Pose3d();
    private Pose3d intakePose3d = new Pose3d();
    private Pose3d shooterPose3d = new Pose3d();
    // Offset from hood center to the hood's pivot point (in meters)
    private static final Translation3d HOOD_PIVOT_OFFSET =
            new Translation3d(0.223907, 0, 0).minus(new Translation3d(0.148908, 0, 0));

    public RobotViz() {}

    public void updateViz() {
        if (state.getLatestRobotToTurret().getValue() != null) {

            double turretRotation = state.getLatestRobotToTurret().getValue().getRadians();

            shooterPose3d =
                    new Pose3d(
                            new Translation3d(-0.148908, -0.152400, 0.260636 + 0.09525),
                            new Rotation3d(0, 0, turretRotation));
            // Rotate the hood's pivot offset by the turret angle,
            // then translate the hood to that rotated pivot position
            Translation3d rotatedHoodPivot =
                    HOOD_PIVOT_OFFSET.rotateBy(new Rotation3d(0, 0, turretRotation));
            hoodPose3d =
                    new Pose3d(
                            new Translation3d(-0.148908, -0.152400, 0.342476 + 0.09525)
                                    .minus(rotatedHoodPivot),
                            new Rotation3d(
                                    0,
                                    Units.rotationsToRadians(state.getHoodRotations()),
                                    turretRotation));
        }

        intakePose3d =
                new Pose3d(
                        new Translation3d(0.165101, 0, 0.012717 + 0.09525),
                        new Rotation3d(
                                0, Units.rotationsToRadians(state.getIntakePivotRotations()), 0));

        Logger.recordOutput(
                "ComponentsPoseArray", new Pose3d[] {intakePose3d, shooterPose3d, hoodPose3d});
    }
}
