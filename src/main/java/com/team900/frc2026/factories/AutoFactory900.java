package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.commands.DriveMaintainingHeadingCommand;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class AutoFactory900 {

    private static final RobotContainer container = RobotContainer.getInstance();

    public static Command resetPose(Pose2d pose) {
        return new InstantCommand(
                () -> container.getDriveSubsystem().resetPose(pose), container.getDriveSubsystem());
    }

    public static Command waitSeconds(double seconds) {
        return new WaitCommand(seconds);
    }

    public static Command intakeSlapdown() {
        return new ParallelCommandGroup(
                IntakeFactory.deploySlapdown(container), IntakeFactory.runIntake(container));
    }

    public static Command retractSlapdown() {
        return new ParallelCommandGroup(
                IntakeFactory.retractSlapdown(container), IntakeFactory.stopIntake(container));
    }

    public static Command runIntake() {
        return IntakeFactory.runIntake(container);
    }

    public static Command stopIntake() {
        return IntakeFactory.stopIntake(container);
    }

    public static Command shoot(Supplier<ShooterSetpoint> setpointSupplier) {
        return new ParallelCommandGroup(ShootingFactory.shoot(setpointSupplier, container));
    }

    public static Command stopShoot() {
        return new ParallelCommandGroup(
                ShooterFactory.setShooterRPS(0, container),
                SpindexerFactory.stopSpindexer(container),
                HandoffFactory.stopHandoff(container),
                HoodFactory.stow(container));
    }

    public static Command deploySlapdownAndRunIntake(RobotContainer container) {
        return new SequentialCommandGroup(
                IntakeFactory.deploySlapdown(container), IntakeFactory.runIntake(container));
    }

    public static Command resetHood(RobotContainer container) {
        return new InstantCommand(() -> HoodFactory.stow(container));
    }

    public static Command zeroHood(RobotContainer container) {
        return HoodFactory.zero(container);
    }

    public static Command alignToHub(
            DoubleSupplier throttle, DoubleSupplier strafe, DoubleSupplier turn) {

        DriveMaintainingHeadingCommand autoDriveCommand =
                new DriveMaintainingHeadingCommand(container, throttle, strafe, turn);
        autoDriveCommand.setKAiming(true);
        return autoDriveCommand;
    }
}
