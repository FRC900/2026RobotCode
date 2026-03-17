package com.team900.frc2026.auto;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.factories.AutoFactory900;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import java.util.Set;

public class Autos {
    private static final RobotContainer container = RobotContainer.getInstance();

    // shoot Fuel from starting position
    private static Command simpleAuto(double x, double y, double deg) {
        return Commands.sequence(
                Commands.runOnce(
                        () ->
                                container
                                        .getDriveSubsystem()
                                        .resetPose(new Pose2d(x, y, Rotation2d.fromDegrees(deg)))),
                // reset
                AutoFactory900.resetHood(container),
                // reset turret
                Commands.race(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                Commands.race(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(AutoConstants.eightBallShootTime)),
                AutoFactory900.stopShoot().withTimeout(AutoConstants.stopShootTime),
                Commands.runOnce(() -> container.getDriveSubsystem().stop()));
    }

    // shoot Fuel from starting position from AutoTrajectory
    private static Command simpleAutoFromPath(AutoTrajectory path) {
        return Commands.sequence(
                // reset
                path.resetOdometry(),
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

    // Command for moving robot to one swipe of Fuel and back
    private static Command swipeCommand(AutoTrajectory path, String pathName) {
        return Commands.sequence(
                // Intake (only at start) and run path
                Commands.deadline(
                        path.cmd(),
                        pathName.equals("OneSwipe")
                                ? AutoFactory900.deploySlapdownAndRunIntake(container)
                                : Commands.none()),

                // Aim turret/hood and shoot
                Commands.race(
                        AutoFactory900.alignToHub(() -> 0.0, () -> 0.0, () -> 0.0),
                        AutoFactory900.waitSeconds(AutoConstants.alignTime)),
                Commands.race(
                        AutoFactory900.shoot(ShooterSetpoint::setpointHub),
                        AutoFactory900.waitSeconds(AutoConstants.fullHopperShootTime)),
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
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "OneSwipe"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

        return routine.cmd();
    }

    // Two-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot,
    // then run 2and3 swipe.
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
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "OneSwipe"),
                                swipeCommand(twoAndThreeSwipePath, "TwoSwipe"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

        return routine.cmd();
    }

    // Three-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and
    // shoot, then run 2and3 swipe twice.
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
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "OneSwipe"),
                                swipeCommand(twoAndThreeSwipePath, "TwoSwipe"),
                                swipeCommand(twoAndThreeSwipePath, "ThreeSwipe"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

        return routine.cmd();
    }

    // One-swipe auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and shoot
    // at the end.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command oneSwipeCenter(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("oneSwipeCenter" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("oneSwipeCenter");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "oneSwipeCenter"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

        return routine.cmd();
    }

    // Two-swipe Center auto: deploy intake + run path (OneSwipe) while intaking, then aim hood and
    // shoot,
    // then run 2and3 swipe.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command twoSwipeCenter(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("TwoSwipeCenter" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("OneSwipeCenter");
        AutoTrajectory twoAndThreeSwipePath = routine.trajectory("TwoAndThreeSwipeCenter");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "OneSwipeCenter"),
                                swipeCommand(twoAndThreeSwipePath, "TwoSwipeCenter"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

        return routine.cmd();
    }

    // Three-swipe Center auto: deploy intake + run path (OneSwipe) while intaking, then aim hood
    // and
    // shoot, then run 2and3 swipe twice.
    // mirrorY bool to flip across y axis (switch from left side to right or vice versa)
    private static Command threeSwipeCenter(boolean mirrorY) {
        AutoFactory choreoFactory = GenericAuto.getAutoFactory(mirrorY);
        String end = mirrorY ? "_Left" : "_Right";
        AutoRoutine routine = choreoFactory.newRoutine("TwoSwipe" + end);
        AutoTrajectory oneSwipePath = routine.trajectory("OneSwipeCenter");
        AutoTrajectory twoAndThreeSwipePath = routine.trajectory("TwoAndThreeSwipeCenter");

        routine.active()
                .onTrue(
                        Commands.sequence(
                                simpleAutoFromPath(oneSwipePath),
                                swipeCommand(oneSwipePath, "OneSwipeCenter"),
                                swipeCommand(twoAndThreeSwipePath, "TwoSwipeCenter"),
                                swipeCommand(twoAndThreeSwipePath, "ThreeSwipeCenter"),
                                Commands.runOnce(() -> container.getDriveSubsystem().stop())));

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

    public static Command OneSwipeCenter_Left() {
        return oneSwipeCenter(true);
    }

    public static Command OneSwipeCenter_Right() {
        return oneSwipeCenter(false);
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

    public static Command TwoSwipeCenter_Left() {
        return twoSwipeCenter(true);
    }

    public static Command TwoSwipeCenter_Right() {
        return twoSwipeCenter(false);
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

    public static Command ThreeSwipeCenter_Left() {
        return threeSwipeCenter(true);
    }

    public static Command ThreeSwipeCenter_Right() {
        return threeSwipeCenter(false);
    }

    // logic to choose auto based on start position

    public static Command OneSwipe() {
        return Commands.defer(
                () -> {
                    if (DriverStation.getLocation().orElse(0) == 1) {
                        return OneSwipe_Left();
                    }
                    return OneSwipe_Right();
                },
                Set.of());
    }

    public static Command TwoSwipe() {
        return Commands.defer(
                () -> {
                    if (DriverStation.getLocation().orElse(0) == 1) {
                        return TwoSwipe_Left();
                    }
                    return TwoSwipe_Right();
                },
                Set.of());
    }

    public static Command ThreeSwipe() {
        return Commands.defer(
                () -> {
                    if (DriverStation.getLocation().orElse(0) == 1) {
                        return ThreeSwipe_Left();
                    }
                    return ThreeSwipe_Right();
                },
                Set.of());
    }
}
