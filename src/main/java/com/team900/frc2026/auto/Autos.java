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

    private static Command oneShot(AutoTrajectory path) {
        return Commands.sequence(
                path.resetOdometry(),
                AutoFactory900.resetHood(container),
                path.cmd(),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)),
                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub));
    }

    private static Command onePointFiveShots(AutoTrajectory path) {
        return Commands.sequence(
                oneShot(path),
                Commands.parallel(path.cmd(), AutoFactory900.deploySlapdownAndRunIntake(container)),
                AutoFactory900.retractSlapdown());
    }

    private static Command twoShots(AutoTrajectory path) {
        return Commands.sequence(
                onePointFiveShots(path),
                path.cmd(),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)),
                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub));
    }

    private static Command threeShots(AutoTrajectory path) {
        return Commands.sequence(
                twoShots(path),
                Commands.parallel(path.cmd(), AutoFactory900.deploySlapdownAndRunIntake(container)),
                AutoFactory900.retractSlapdown(),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)),
                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub));
    }

    public static Command C_RT_Ce() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("C_RT_Ce");
        AutoTrajectory path = routine.trajectory("C_RT_Ce");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                path.resetOdometry(),
                                AutoFactory900.resetHood(container),
                                path.cmd(),
                                Commands.parallel(
                                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                                        AutoFactory900.waitSeconds(1)),
                                Commands.parallel(
                                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                                        AutoFactory900.waitSeconds(5)),
                                AutoFactory900.stopShoot(ShooterSetpoint::setpointHub)));
        return routine.cmd();
    }
}
