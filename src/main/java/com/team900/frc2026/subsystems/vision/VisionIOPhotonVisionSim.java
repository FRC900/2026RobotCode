package com.team900.frc2026.subsystems.vision;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import java.util.function.Supplier;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;

/** IO implementation for physics sim using PhotonVision simulator. */
public class VisionIOPhotonVisionSim extends VisionIOPhotonVision {
    private static VisionSystemSim visionSim;

    private final Supplier<Pose2d> poseSupplier;
    private final PhotonCameraSim cameraSim;

    /**
     * Creates a new VisionIOPhotonVisionSim.
     *
     * @param name The name of the camera.
     * @param robotToCameraSupplier Supplier for the current robot-to-camera
     *     transform (dynamic for turret cameras, constant for fixed cameras).
     * @param poseSupplier Supplier for the robot pose to use in simulation.
     */
    public VisionIOPhotonVisionSim(
            String name,
            Supplier<Transform3d> robotToCameraSupplier,
            Supplier<Pose2d> poseSupplier) {
        super(name, robotToCameraSupplier);
        this.poseSupplier = poseSupplier;

        // Initialize vision sim (shared across all cameras)
        if (visionSim == null) {
            visionSim = new VisionSystemSim("main");
            visionSim.addAprilTags(
                    AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltAndymark));
        }

        // Add sim camera with default properties
        var cameraProperties = new SimCameraProperties();
        cameraSim = new PhotonCameraSim(camera, cameraProperties);
        // Use current transform for initial placement
        visionSim.addCamera(cameraSim, robotToCameraSupplier.get());

        cameraSim.enableRawStream(true);
        cameraSim.enableProcessedStream(true);
        cameraSim.enableDrawWireframe(true);
    }

    /** Convenience constructor for a fixed (non-turret) sim camera. */
    public VisionIOPhotonVisionSim(
            String name,
            Transform3d robotToCamera,
            Supplier<Pose2d> poseSupplier) {
        this(name, () -> robotToCamera, poseSupplier);
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        // Update the sim camera position to match the current robot-to-camera
        // transform. For turret cameras, this keeps the sim camera in sync with
        // the turret rotation.
        visionSim.adjustCamera(cameraSim, robotToCameraSupplier.get());
        visionSim.update(poseSupplier.get());
        super.updateInputs(inputs);
    }
}
