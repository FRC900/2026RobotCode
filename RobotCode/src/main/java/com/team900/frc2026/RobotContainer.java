// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team900.frc2026;

import com.team900.frc2026.subsystems.Hood.HoodSubsystem;
import com.team900.frc2026.subsystems.Hood.HoodIOHardware;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import edu.wpi.first.wpilibj.RobotBase;
import com.team900.frc2026.subsystems.Hood.HoodIOSim;

import com.team900.frc2026.subsystems.Turret.TurretIOHardware;
import com.team900.frc2026.subsystems.Turret.TurretIOSim;
import com.team900.frc2026.subsystems.Turret.TurretSubsystem;

public class RobotContainer {

  private final HoodSubsystem Hood;
  private final TurretSubsystem Turret;

  public RobotContainer() {

    if (RobotBase.isReal()) {
      Hood = new HoodSubsystem(new HoodIOHardware());
    } else {
      Hood = new HoodSubsystem(new HoodIOSim());
    }

    if (RobotBase.isReal()) {
      Turret = new TurretSubsystem(new TurretIOHardware());
    } else {
      Turret = new TurretSubsystem(new TurretIOSim());
    }

    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }

  public HoodSubsystem getHood() {
    return Hood;
  }
  
  public TurretSubsystem getTurret() {
    return Turret;
  }
}
