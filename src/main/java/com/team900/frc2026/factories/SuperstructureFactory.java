package com.team900.frc2026.factories;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.util.ShooterSetpoint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import java.util.function.Supplier;

public class SuperstructureFactory {

    public static Command aimAndShoot(
            RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        return new ParallelCommandGroup(
                TurretFactory.aimTurretToPose(setPointSupplier),
                HoodFactory.aimHoodToPose(setPointSupplier),
                SpindexerFactory.runSpindexer(),
                HandoffFactory.runHandoff(),
                ShooterFactory.setShooterRPS(setPointSupplier));
        /*Command to shoot all balls in robot (should auto aim)
         * Aim turret*
         * Aim hood*
         * Run handoff, shooter, and spindexer as appropriate**/
    }

    public static Command shoot(RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        return new ParallelCommandGroup(
                SpindexerFactory.runSpindexer(),
                HandoffFactory.runHandoff(),
                ShooterFactory.setShooterRPS(setPointSupplier));
        /*Command to shoot all balls in robot (should NOT auto aim)
         * Run handoff, shooter, and spindexer as appropriate**/
    }

    public static Command aim(
            RobotContainer container, Supplier<ShooterSetpoint> setPointSupplier) {
        // return null;
        return new ParallelCommandGroup(
                TurretFactory.aimTurretToPose(setPointSupplier),
                HoodFactory.aimHoodToPose(setPointSupplier));
        /*Command to aim at target
         * Aim turret
         * Aim hood */
    }

    public static Command intake(RobotContainer container) {
        return new ParallelCommandGroup(
                IntakeFactory.runIntake(),
                SpindexerFactory.runSpindexer());
        /*Command to intake balls into robot
        Assumes intake is already deployed
         * Run intake, spindexer, and handoff as appropriate**/
    }

    public static Command deploy(RobotContainer container) {
        return IntakeFactory.deploySlapdown();
        /*Command to deploy intake
         * Deploy intake**/
    }

    public static Command deploy_then_intake(RobotContainer container){
        return new ParallelCommandGroup(
                IntakeFactory.deploySlapdown(),
                IntakeFactory.runIntake(),
                SpindexerFactory.runSpindexer());
         /*Command to deploy intake and then run intake
         * Deploy intake
         * Run intake, spindexer, and handoff as appropriate**/
    }

    public static Command stowIntake(RobotContainer container){
        return IntakeFactory.retractSlapdown();
        /*Command to stow intake
         * Stow intake**/
    }

    public Command outtake(RobotContainer container) {
        return IntakeFactory.exhaustIntake();
        /*Command to outtake balls from robot
         * Run intake in reverse**/
    }

    public Command deploy_then_outtake(RobotContainer container){
        return new ParallelCommandGroup(
                IntakeFactory.deploySlapdown(),
                IntakeFactory.exhaustIntake());
         /*Command to deploy intake and then run outtake
         * Deploy intake
         * Run intake in reverse**/
    }   

    
}
