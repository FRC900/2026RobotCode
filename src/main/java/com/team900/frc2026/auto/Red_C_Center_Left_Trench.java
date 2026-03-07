package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.SwerveSample;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.factories.AutoFactory900;
import com.team900.lib.util.FieldConstants;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class Red_C_Center_Left_Trench {
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

    public static Command getAutoCommand() {
        RobotContainer container = RobotContainer.getInstance();

        PIDController xController = new PIDController(kTranslationP, 0, 0);
        PIDController yController = new PIDController(kTranslationP, 0, 0);
        PIDController rotController = new PIDController(kRotationP, 0, 0);
        rotController.enableContinuousInput(-Math.PI, Math.PI);

        AutoFactory choreoFactory =
                new AutoFactory(
                        container.getDriveSubsystem()::getPose,
                        (Pose2d pose) -> {
                            Pose2d mirrored =
                                    new Pose2d(
                                            pose.getX(),
                                            mirrorY(pose.getY()),
                                            new Rotation2d(mirrorHeading(
                                                    pose.getRotation().getRadians())));
                            container.getDriveSubsystem().resetPose(mirrored);
                        },
                        (SwerveSample sample) -> {
                            double mirroredY = mirrorY(sample.y);
                            double mirroredHeading = mirrorHeading(sample.heading);
                            double mirroredVy = -sample.vy;
                            double mirroredOmega = -sample.omega;

                            double xFB =
                                    xController.calculate(
                                            container.getDriveSubsystem().getPose().getX(),
                                            sample.x);
                            double yFB =
                                    yController.calculate(
                                            container.getDriveSubsystem().getPose().getY(),
                                            mirroredY);
                            double rFB =
                                    rotController.calculate(
                                            container
                                                    .getDriveSubsystem()
                                                    .getPose()
                                                    .getRotation()
                                                    .getRadians(),
                                            mirroredHeading);

                            ChassisSpeeds fieldRelative =
                                    new ChassisSpeeds(
                                            sample.vx + xFB,
                                            mirroredVy + yFB,
                                            mirroredOmega + rFB);
                            ChassisSpeeds robotRelative =
                                    ChassisSpeeds.fromFieldRelativeSpeeds(
                                            fieldRelative,
                                            container.getDriveSubsystem().getPose().getRotation());
                            container.getDriveSubsystem().runVelocity(robotRelative);
                        },
                        DriverStation.getAlliance().orElse(Alliance.Blue) == Alliance.Red,
                        container.getDriveSubsystem());

        AutoRoutine routine = choreoFactory.newRoutine("Red_C_Center_Left_Trench");
        AutoTrajectory path = routine.trajectory("Red_C_Center_Left_Trench");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                path.resetOdometry(),
                                AutoFactory900.resetHood(container),
                                path.cmd(),
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(1)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(3)),
                                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub),
                                Commands.parallel(
                                        path.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)))
                                
                                );

        return routine.cmd();
    }
}


