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

    private static Command complexAuto(AutoTrajectory path) {
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
                AutoFactory900.stopShoot(), 

                Commands.parallel(path.cmd(), 
                        AutoFactory900.deploySlapdownAndRunIntake(container)),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)), 

                path.cmd(),
                Commands.parallel(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(1)),
                Commands.parallel(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(5)),
                AutoFactory900.stopShoot(),

                path.cmd()
        );

    }

    public static Command A() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("A");
        AutoTrajectory path = routine.trajectory("A");

        routine.active()
                .onTrue(
                        simpleAuto(path)
                );

        return routine.cmd();
    }

    public static Command B() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("B");
        AutoTrajectory path = routine.trajectory("B");

        routine.active()
                .onTrue(
                        simpleAuto(path)
                );

        return routine.cmd();
    }

    public static Command C() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("C");
        AutoTrajectory path = routine.trajectory("C");

        routine.active()
                .onTrue(
                        simpleAuto(path)
                );

        return routine.cmd();
    }

    public static Command D() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("D");
        AutoTrajectory path = routine.trajectory("D");

        routine.active()
                .onTrue(
                        simpleAuto(path)
                );

        return routine.cmd();
    }

    public static Command E() {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory();
        AutoRoutine routine = choreoFactory.newRoutine("E");
        AutoTrajectory path = routine.trajectory("E");

        routine.active()
                .onTrue(
                        simpleAuto(path)
                );

        return routine.cmd();
    }
}
