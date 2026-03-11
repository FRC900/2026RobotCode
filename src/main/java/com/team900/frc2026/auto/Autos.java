package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.factories.AutoFactory900;
import com.team900.lib.util.ShooterSetpoint;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class Autos {
    private static final RobotContainer container = RobotContainer.getInstance();

    private static Command simpleAuto(double x, double y, double deg) {
        return Commands.sequence(
                Commands.runOnce(() -> container.getDriveSubsystem().resetPose(new Pose2d(x, y, Rotation2d.fromDegrees(deg)))),
                AutoFactory900.resetHood(container),
                // reset turret
                Commands.race(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                Commands.race(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(AutoConstants.eightBallShootTime)),
                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime));
    }

    // One-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot
    // at the end.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command oneSwipe(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("OneSwipe" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("OneSwipe");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                // Reset
                                oneSwipePath.resetOdometry(),
                                AutoFactory900.resetHood(container),

                                // shoot first eight (when shoot on the move works, do this while moving)
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.eightBallShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),

                                // Intake and run path 
                                Commands.deadline(
                                        oneSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),

                                // Aim turret/hood and shoot
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime)));

        return routine.cmd();
    }

    // Two-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot, then run 2and3 swipe.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command twoSwipe(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("TwoSwipe" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("OneSwipe");
        AutoTrajectory twoAndThreeSwipePath = routine.trajectory("TwoAndThreeSwipe");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                // reset
                                oneSwipePath.resetOdometry(),
                                AutoFactory900.resetHood(container),

                                // shoot first eight (when shoot on the move works, do this while moving)
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.eightBallShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),


                                // Intake and run first swipe path
                                Commands.deadline(
                                        oneSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                // Aim turret/hood and shoot and stop
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),

                                // Intake and run second swipe path 
                                Commands.deadline(
                                        twoAndThreeSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                // Aim turret/hood and shoot again and stop
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime)));

        return routine.cmd();
    }

    // Three-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot, then run 2and3 swipe twice.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command threeSwipe(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("TwoSwipe" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("OneSwipe");
        AutoTrajectory twoAndThreeSwipePath = routine.trajectory("TwoAndThreeSwipe");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                // reset
                                oneSwipePath.resetOdometry(),
                                AutoFactory900.resetHood(container),

                                // shoot first eight (when shoot on the move works, do this while moving)
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.eightBallShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),

                                // Intake and run first swipe path
                                Commands.deadline(
                                        oneSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                // Aim turret/hood and shoot and stop
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),

                                // Intake and run second swipe path 
                                Commands.deadline(
                                        twoAndThreeSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                // Aim turret/hood and shoot again and stop
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),

                                // Intake and run third swipe path 
                                Commands.deadline(
                                        twoAndThreeSwipePath.cmd(),
                                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                                // Aim turret/hood and shoot again and stop
                                Commands.race(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                                Commands.race(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
                                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime)
                        )
                );

        return routine.cmd();
    }

    // Simple standstill autos

    public static Command A_Simple() {
        return simpleAuto(AutoConstants.line_xposition, AutoConstants.A_yposition, 0);
    }

    public static Command B_Simple() {
        return simpleAuto(AutoConstants.line_xposition, AutoConstants.B_yposition, 0);
    }

    public static Command C_Simple() {
        return simpleAuto(AutoConstants.line_xposition, AutoConstants.C_yposition, 0);
    }

    public static Command D_Simple() {
        return simpleAuto(AutoConstants.line_xposition, AutoConstants.D_yposition, 0);
    }

    public static Command E_Simple() {
        return simpleAuto(AutoConstants.line_xposition, AutoConstants.E_yposition, 0);
    }

    // OneSwipe autos
    // right = normal
    // left = Y-mirrored

    public static Command OneSwipe_Left() {
        return oneSwipe(true);
    }

    public static Command OneSwipe_Right() {
        return oneSwipe(false);
    }

    // TwoSwipe autos
    // Right = normal
    // Left = Y-mirrored

    public static Command TwoSwipe_Left() {
        return twoSwipe(true);
    }

    public static Command TwoSwipe_Right() {
        return twoSwipe(false);
    }

    // ThreeSwipe autos
    // Right = normal
    // Left = Y-mirrored

    public static Command ThreeSwipe_Left() {
        return threeSwipe(true);
    }

    public static Command ThreeSwipe_Right() {
        return threeSwipe(false);
    }
}
