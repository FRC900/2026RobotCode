package com.team900.frc2026.subsystems.vision;

/**
 * Configuration for a single vision camera. Tells the subsystem whether the
 * camera is mounted on a turret (which affects angular velocity rejection
 * logic and std dev calculations).
 *
 * @param isTurretCamera True if this camera is mounted on a turret.
 */
public record CameraConfig(boolean isTurretCamera) {

    /** Config for a camera rigidly mounted to the robot chassis. */
    public static CameraConfig fixed() {
        return new CameraConfig(false);
    }

    /** Config for a camera mounted on the turret. */
    public static CameraConfig turret() {
        return new CameraConfig(true);
    }
}
