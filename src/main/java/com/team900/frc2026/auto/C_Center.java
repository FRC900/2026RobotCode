package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.SwerveSample;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.factories.AutoFactory900;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class C_Center {
    private static final double kTranslationP = 5.0;
    private static final double kRotationP = 5.0;

    public static Command getAutoCommand() {
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

        AutoRoutine routine = choreoFactory.newRoutine("C_Center");
        AutoTrajectory path = routine.trajectory("C_Center");
        
        routine.active()
                .onTrue(
                        Commands.sequence(
                                path.resetOdometry(),
                                AutoFactory900.resetHood(container),
                                AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0)
                                        .withTimeout(1.0),

        
                                AutoFactory900.shoot(ShooterSetpoint::setpointHub).withTimeout(3.0),
                                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub),
                                Commands.parallel(
                                        path.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                AutoFactory900.retractSlapdown()
                        )
                );

        // for sim
        // // routine.active()
        // //         .onTrue(Commands.sequence(path.resetOdometry(),
        // Commands.parallel(path.cmd())));

        return routine.cmd();
    }
}
