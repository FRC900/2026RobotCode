package com.team900.frc2026.subsystems.vision;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.FieldConstants;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import org.photonvision.PhotonCamera;

/** IO implementation for real PhotonVision hardware. */
public class VisionIOPhotonVision implements VisionIO {
    protected final PhotonCamera camera;
    protected final Supplier<Transform3d> robotToCameraSupplier;

    /**
     * Creates a new VisionIOPhotonVision.
     *
     * <p>For a fixed camera, pass a constant supplier: {@code () -> staticTransform}.
     * For a turret-mounted camera, pass a supplier that composes the current
     * robot-to-turret transform with the static turret-to-camera transform.
     *
     * @param name The configured name of the camera.
     * @param robotToCameraSupplier Supplier for the current robot-to-camera transform.
     */
    public VisionIOPhotonVision(String name, Supplier<Transform3d> robotToCameraSupplier) {
        camera = new PhotonCamera(name);
        this.robotToCameraSupplier = robotToCameraSupplier;
    }

    /** Convenience constructor for a fixed (non-turret) camera. */
    public VisionIOPhotonVision(String name, Transform3d robotToCamera) {
        this(name, () -> robotToCamera);
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        inputs.connected = camera.isConnected();

        // Grab the current transform for this frame
        Transform3d robotToCamera = robotToCameraSupplier.get();

        Set<Short> tagIds = new HashSet<>();
        List<PoseObservation> poseObservations = new LinkedList<>();

        for (var result : camera.getAllUnreadResults()) {
            // Update latest target observation
            if (result.hasTargets()) {
                inputs.latestTargetObservation =
                        new TargetObservation(
                                Rotation2d.fromDegrees(result.getBestTarget().getYaw()),
                                Rotation2d.fromDegrees(result.getBestTarget().getPitch()));

                // --- Single-tag pinhole observations ---
                for (var target : result.targets) {
                    Optional<Pose3d> tagPose =
                            FieldConstants.defaultAprilTagType
                                    .getLayout()
                                    .getTagPose(target.fiducialId);
                    if (tagPose.isEmpty()) continue;

                    double tagDistance =
                            target.getBestCameraToTarget().getTranslation().getNorm();

                    // Pinhole model: build direction vector from yaw/pitch, scale to
                    // measured distance
                    Translation3d cameraToTag =
                            new Translation3d(
                                    1,
                                    -Math.tan(Math.toRadians(target.getYaw())),
                                    Math.tan(Math.toRadians(target.getPitch())));
                    cameraToTag = cameraToTag.times(tagDistance / cameraToTag.getNorm());

                    // Transform into robot frame using the CURRENT robot-to-camera
                    Translation3d robotToTag =
                            cameraToTag.rotateBy(robotToCamera.getRotation());
                    robotToTag = robotToTag.plus(robotToCamera.getTranslation());

                    // Rotate to field coordinates using current gyro heading
                    Rotation2d robotRotation =
                            RobotContainer.getInstance()
                                    .getDriveSubsystem()
                                    .getRotation();
                    Translation2d robotToTagFC =
                            robotToTag.toTranslation2d().rotateBy(robotRotation);
                    Translation2d fieldToRobot =
                            tagPose.get()
                                    .getTranslation()
                                    .toTranslation2d()
                                    .minus(robotToTagFC);

                    Pose3d robotPose =
                            new Pose3d(
                                    new Translation3d(fieldToRobot),
                                    new Rotation3d(robotRotation));

                    poseObservations.add(
                            new PoseObservation(
                                    result.getTimestampSeconds(),
                                    robotPose,
                                    target.getPoseAmbiguity(),
                                    1,
                                    tagDistance,
                                    target.getArea(),
                                    PoseObservationType.PINHOLE));

                    tagIds.add((short) target.fiducialId);
                }
            } else {
                inputs.latestTargetObservation =
                        new TargetObservation(new Rotation2d(), new Rotation2d());
            }

            // --- Multi-tag SolvePNP observation ---
            if (result.multitagResult.isPresent()) {
                var multitagResult = result.multitagResult.get();

                // PhotonVision gives us fieldToCamera; we need fieldToRobot
                Transform3d fieldToCamera = multitagResult.estimatedPose.best;
                Transform3d fieldToRobot =
                        fieldToCamera.plus(robotToCamera.inverse());
                Pose3d robotPose =
                        new Pose3d(
                                fieldToRobot.getTranslation(),
                                fieldToRobot.getRotation());

                // Calculate average tag distance and area
                double totalTagDistance = 0.0;
                double totalTagArea = 0.0;
                for (var target : result.targets) {
                    totalTagDistance +=
                            target.bestCameraToTarget.getTranslation().getNorm();
                    totalTagArea += target.getArea();
                }
                double avgDistance = totalTagDistance / result.targets.size();
                double avgArea = totalTagArea / result.targets.size();

                tagIds.addAll(multitagResult.fiducialIDsUsed);

                poseObservations.add(
                        new PoseObservation(
                                result.getTimestampSeconds(),
                                robotPose,
                                multitagResult.estimatedPose.ambiguity,
                                multitagResult.fiducialIDsUsed.size(),
                                avgDistance,
                                avgArea,
                                PoseObservationType.SOLVE_PNP));
            }
        }

        // Save to inputs
        inputs.poseObservations = poseObservations.toArray(new PoseObservation[0]);
        inputs.tagIds = new int[tagIds.size()];
        int i = 0;
        for (int id : tagIds) {
            inputs.tagIds[i++] = id;
        }
    }
}
