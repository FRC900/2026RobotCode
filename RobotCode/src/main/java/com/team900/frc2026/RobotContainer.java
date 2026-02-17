// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team254.lib.subsystems.SimCanCoderIO;
import com.team900.frc2026.subsystems.Intake.IntakeConstants;
import com.team900.frc2026.subsystems.Intake.IntakePivotSubsystem;
import com.team900.frc2026.subsystems.Intake.IntakeRollerSubsystem;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team254.lib.subsystems.TalonFXIO;
import com.team254.lib.subsystems.CanCoderIOHardware;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class RobotContainer {
  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
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
      new TalonFXIO(IntakeConstants.kIntakePivotConfig), 
      new CanCoderIOHardware(IntakeConstants.kIntakePivotConfig.canCoderConfig), 
      robotState);
  }

  private final IntakeRollerSubsystem intakeRollerSubsystem = buildIntakeRollerSubsystem();

  private final IntakePivotSubsystem intakePivotSubsystem = buildIntakePivotSubsystem();

  public IntakeRollerSubsystem getIntakeRoller() {
    return intakeRollerSubsystem;
  }

  public IntakePivotSubsystem getIntakePivot() {
    return intakePivotSubsystem;
  }
}