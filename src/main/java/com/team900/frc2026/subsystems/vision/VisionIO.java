package com.team900.frc2026.subsystems.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;

public interface VisionIO {

    @AutoLog
    public static class VisionIOInputs {
        public boolean connected = false;
        public TargetObservation latestTargetObservation =
                new TargetObservation(new Rotation2d(), new Rotation2d());
        public PoseObservation[] poseObservations = new PoseObservation[0];
        public int[] tagIds = new int[0];
    }

    /** Represents the angle to a simple target, not used for pose estimation. */
    public static record TargetObservation(Rotation2d tx, Rotation2d ty) {}

    /**
     * Represents a robot pose sample used for pose estimation.
     *
     * <p>Carries all the metadata the subsystem needs to decide whether to accept
     * the observation and how to weight it.
     */
    public static record PoseObservation(
            double timestamp,
            Pose3d pose,
            double ambiguity,
            int tagCount,
            double averageTagDistance,
            double averageTagArea,
            PoseObservationType type) {}

    public static enum PoseObservationType {
        /** Multi-tag SolvePNP (PhotonVision multi-tag result). */
        SOLVE_PNP,
        /** Single-tag pinhole projection model. */
        PINHOLE
    }

    public default void updateInputs(VisionIOInputs inputs) {}
}
