package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.trajectory.SwerveSample;
import com.team900.frc2026.RobotContainer;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class GenericAuto {
    private static final double kTranslationP = 5.0;
    private static final double kRotationP = 5.0;

    public static AutoFactory getAutoFactory() {
        RobotContainer container = RobotContainer.getInstance();

        PIDController xController = new PIDController(kTranslationP, 0, 0);
        PIDController yController = new PIDController(kTranslationP, 0, 0);
        PIDController rotController = new PIDController(kRotationP, 0, 0);
        rotController.enableContinuousInput(-Math.PI, Math.PI);

        AutoFactory choreoFactory =
                new AutoFactory(
                        container.getDriveSubsystem()::getPose,
                        container.getDriveSubsystem()::resetPose,
                        (SwerveSample sample) -> {
                            double xFB =
                                    xController.calculate(
                                            container.getDriveSubsystem().getPose().getX(),
                                            sample.x);
                            double yFB =
                                    yController.calculate(
                                            container.getDriveSubsystem().getPose().getY(),
                                            sample.y);
                            double rFB =
                                    rotController.calculate(
                                            container
                                                    .getDriveSubsystem()
                                                    .getPose()
                                                    .getRotation()
                                                    .getRadians(),
                                            sample.heading);

                            ChassisSpeeds fieldRelative =
                                    new ChassisSpeeds(
                                            sample.vx + xFB, sample.vy + yFB, sample.omega + rFB);
                            ChassisSpeeds robotRelative =
                                    ChassisSpeeds.fromFieldRelativeSpeeds(
                                            fieldRelative,
                                            container.getDriveSubsystem().getPose().getRotation());
                            container.getDriveSubsystem().runVelocity(robotRelative);
                        },
                        DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red,
                        container.getDriveSubsystem());
            return choreoFactory;
        }
}
