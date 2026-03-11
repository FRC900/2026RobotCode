package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.factories.AutoFactory900;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class Autos {
    private static final RobotContainer container = RobotContainer.getInstance();

    private static Command simpleAuto(AutoTrajectory path) {
        return Commands.sequence(
                path.resetOdometry(),
                AutoFactory900.resetHood(container),
                // reset turret, 
                path.cmd(),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)),
                AutoFactory900.stopShoot()
        );
    }


    // One-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot at the end.
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
                                // Intake and run path
                                Commands.parallel(
                                        AutoFactory900.deploySlapdownAndRunIntake(container),
                                        oneSwipePath.cmd()),
                                // Aim turret/hood and shoot
                                Commands.parallel(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(1)),
                                Commands.parallel(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(5)),
                                AutoFactory900.stopShoot()
                        ));

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
                                // First: Intake and run first swipe path
                                Commands.parallel(
                                        AutoFactory900.deploySlapdownAndRunIntake(container),
                                        oneSwipePath.cmd()),
                                // Aim turret/hood and shoot and stop
                                Commands.parallel(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(1)),
                                Commands.parallel(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(5)),
                                AutoFactory900.stopShoot(),
                                // Second: Intake and run second swipe path
                                Commands.parallel(
                                        AutoFactory900.deploySlapdownAndRunIntake(container),
                                        twoAndThreeSwipePath.cmd()),
                                // Aim turret/hood and shoot again and stop
                                Commands.parallel(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(1)),
                                Commands.parallel(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(5)),
                                AutoFactory900.stopShoot()
                        ));

        return routine.cmd();
    }

    // OneSwipe autos
    // right = normal
    // left = Y-mirrored

    public static Command OneSwipe_Left() {
        return oneSwipe(false);
    }

    public static Command OneSwipe_Right() {
        return oneSwipe(true);
    }

    // TwoSwipe autos
    // Right = normal
    // Left = Y-mirrored

    public static Command TwoSwipe_Left() {
        return twoSwipe(false);
    }

    public static Command TwoSwipe_Right() {
        return twoSwipe(true);
    }
}
