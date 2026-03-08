package com.team900.frc2026.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoDashboard {

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public AutoDashboard() {
        autoChooser.setDefaultOption("A", APath.getAutoCommand());

        // Add more autos here as you create them:
        // autoChooser.addOption("My Other Auto", MyOtherAuto.getAutoCommand());

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    /** Call this in Robot.autonomousInit() or RobotContainer.getAutonomousCommand() */
    public Command getSelectedAuto() {
        Command selected = autoChooser.getSelected();
        return selected != null ? selected : Commands.none();
    }
}
