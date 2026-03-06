package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.commands.DriveMaintainingHeadingCommand;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
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
                IntakeFactory.retractSlapdown(container), IntakeFactory.exhaustIntake(container));
    }

    public static Command shoot(Supplier<ShooterSetpoint> setpointSupplier) {
        return new ParallelCommandGroup(
                ShooterFactory.setShooterRPM(setpointSupplier, container),
                SpindexerFactory.runSpindexer(container),
                HandoffFactory.runHandoff(container),
                HoodFactory.aimHoodToPose(setpointSupplier, container));
    }

    public static Command stopShoot(Supplier<ShooterSetpoint> setpointSupplier) {
        return new ParallelCommandGroup(
                ShooterFactory.setShooterRPM(0, container),
                SpindexerFactory.exhaustSpindexer(container),
                HandoffFactory.exhaustHandoff(container),
                HoodFactory.aimHoodToPose(setpointSupplier, container));
    }

    public static Command alignToHub(
            DoubleSupplier throttle, DoubleSupplier strafe, DoubleSupplier turn) {
        return new DriveMaintainingHeadingCommand(container, throttle, strafe, turn);
    }
}
