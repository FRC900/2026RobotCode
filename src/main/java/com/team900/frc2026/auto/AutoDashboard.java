package com.team900.frc2026.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoDashboard {

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public AutoDashboard() {
        autoChooser.setDefaultOption("A", Autos.A());

        autoChooser.addOption("B", Autos.B());
        autoChooser.addOption("C", Autos.C());
        autoChooser.addOption("D", Autos.D());
        autoChooser.addOption("E", Autos.E());

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getSelectedAuto() {
        Command selected = autoChooser.getSelected();
        return selected != null ? selected : Commands.none();
    }
}
