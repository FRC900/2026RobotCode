package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.trajectory.SwerveSample;
import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.FieldConstants;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class GenericAuto {
    private static final double kTranslationP = 5.0;
    private static final double kRotationP = 5.0;

    //  Mirrors a Y coordinate across the field center line.
    private static double mirrorY(double y) {
        return FieldConstants.fieldWidth - y;
    }

    // negating x heading
    private static double mirrorHeading(double heading) {
        return -heading;
    }

    // AutoFactory with no mirroring (default)
    public static AutoFactory getAutoFactory() {
        return getAutoFactory(false);
    }

    // AutoFactory with bool to mirror across Y Axis (switch from left side to right or vice versa)
    public static AutoFactory getAutoFactory(boolean mirrorAcrossY) {
        RobotContainer container = RobotContainer.getInstance();

        PIDController xController = new PIDController(kTranslationP, 0, 0);
        PIDController yController = new PIDController(kTranslationP, 0, 0);
        PIDController rotController = new PIDController(kRotationP, 0, 0);
        rotController.enableContinuousInput(-Math.PI, Math.PI);

        AutoFactory choreoFactory =
                new AutoFactory(
                        container.getDriveSubsystem()::getPose,
                        mirrorAcrossY
                                ? (Pose2d pose) -> {
                                    Pose2d mirrored =
                                            new Pose2d(
                                                    pose.getX(),
                                                    mirrorY(pose.getY()),
                                                    new Rotation2d(
                                                            mirrorHeading(
                                                                    pose.getRotation()
                                                                            .getRadians())));
                                    container.getDriveSubsystem().resetPose(mirrored);
                                }
                                : container.getDriveSubsystem()::resetPose,
                        (SwerveSample sample) -> {
                            double targetY = mirrorAcrossY ? mirrorY(sample.y) : sample.y;
                            double targetHeading =
                                    mirrorAcrossY ? mirrorHeading(sample.heading) : sample.heading;
                            double targetVy = mirrorAcrossY ? -sample.vy : sample.vy;
                            double targetOmega = mirrorAcrossY ? -sample.omega : sample.omega;

                            double xFB =
                                    xController.calculate(
                                            container.getDriveSubsystem().getPose().getX(),
                                            sample.x);
                            double yFB =
                                    yController.calculate(
                                            container.getDriveSubsystem().getPose().getY(),
                                            targetY);
                            double rFB =
                                    rotController.calculate(
                                            container
                                                    .getDriveSubsystem()
                                                    .getPose()
                                                    .getRotation()
                                                    .getRadians(),
                                            targetHeading);

                            ChassisSpeeds fieldRelative =
                                    new ChassisSpeeds(
                                            sample.vx + xFB, targetVy + yFB, targetOmega + rFB);
                            ChassisSpeeds robotRelative =
                                    ChassisSpeeds.fromFieldRelativeSpeeds(
                                            fieldRelative,
                                            container.getDriveSubsystem().getPose().getRotation());
                            container.getDriveSubsystem().runVelocity(robotRelative);
                        },
                        true, // always enable alliance flipping; Choreo queries the alliance lazily at runtime
                        container.getDriveSubsystem());
        return choreoFactory;
    }
}
