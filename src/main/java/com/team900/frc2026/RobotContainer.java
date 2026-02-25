// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;

import com.team900.frc2026.subsystems.Intake.IntakeConstants;
import com.team900.frc2026.subsystems.Intake.IntakePivotSubsystem;
import com.team900.frc2026.subsystems.Intake.IntakeRollerSubsystem;
import com.team900.frc2026.subsystems.Led.LedIOHardware;
import com.team900.frc2026.subsystems.Led.LedSubsystem;
import com.team900.frc2026.RobotState;

import com.team900.lib.subsystems.CanCoderIOHardware;
import com.team900.lib.subsystems.SimCanCoderIO;
import com.team900.lib.subsystems.SimTalonFXIO;
import com.team900.lib.subsystems.TalonFXIO;

public class RobotContainer {
    private static volatile RobotContainer instance;
    public SwerveDriveSimulation driveSimulation = null;
    private static final RobotState robotState = new RobotState();

    private RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {}

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }

    public static RobotContainer getInstance() {
        if (instance == null) {
            synchronized (RobotContainer.class) {
                if (instance == null) {
                    instance = new RobotContainer();
                }
            }
        }
        return instance;
    }

    public IntakeRollerSubsystem buildIntakeRollerSubsystem(){
    if(RobotBase.isSimulation()){
      return new IntakeRollerSubsystem(
        IntakeConstants.kIntakeRollerConfig,
        new SimTalonFXIO(IntakeConstants.kIntakeRollerConfig),
        robotState);
    }
    return new IntakeRollerSubsystem(
      IntakeConstants.kIntakeRollerConfig,
      new SimTalonFXIO(IntakeConstants.kIntakeRollerConfig),
      robotState);
    }

    public IntakePivotSubsystem buildIntakePivotSubsystem(){
        if(RobotBase.isSimulation()){
        var simTalon = new SimTalonFXIO(IntakeConstants.kIntakePivotConfig);
        return new IntakePivotSubsystem(
            IntakeConstants.kIntakePivotConfig, 
            simTalon, 
            new SimCanCoderIO(
            IntakeConstants.kIntakePivotConfig.canCoderConfig, 
            simTalon.getSupplierForCancoder(IntakeConstants.kIntakePivotConfig)), 
            robotState);
        }
        return new IntakePivotSubsystem(
        IntakeConstants.kIntakePivotConfig, 
        new TalonFXIO(IntakeConstants.kIntakePivotConfig.canCoderConfig), 
        new CanCoderIOHardware(IntakeConstants.kIntakePivotConfig), 
        robotState);
    }

    public LedSubsystem buildLedSubsystem(){
        return new LedSubsystem(new LedIOHardware(),robotState);
    }

    private final IntakeRollerSubsystem intakeRollerSubsystem = buildIntakeRollerSubsystem();

    private final IntakePivotSubsystem intakePivotSubsystem = buildIntakePivotSubsystem();

    private final LedSubsystem ledSubsystem = buildLedSubsystem();

    public IntakeRollerSubsystem getIntakeRoller() {
        return intakeRollerSubsystem;
    }

    public IntakePivotSubsystem getIntakePivot() {
        return intakePivotSubsystem;
    }

    public LedSubsystem getLeds(){
        return ledSubsystem;
    }
}
