package com.team900.frc2026.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoDashboard {

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public AutoDashboard() {
        autoChooser.setDefaultOption("Two-Swipe Right (default)", Autos.TwoSwipe_Right());

        // Swipe autos
        // right = normal side
        // left = Y-mirrored
        autoChooser.addOption("One-Swipe Left", Autos.OneSwipe_Left());
        autoChooser.addOption("One-Swipe Right", Autos.OneSwipe_Right());
        autoChooser.addOption("Two-Swipe Left", Autos.TwoSwipe_Left());
        autoChooser.addOption("Two-Swipe Right", Autos.TwoSwipe_Right());
        autoChooser.addOption("Three-Swipe Left", Autos.ThreeSwipe_Left());
        autoChooser.addOption("Three-Swipe Right", Autos.ThreeSwipe_Right());

        // Simple stanstill autos
        autoChooser.addOption("A Simple", Autos.A_Simple());
        autoChooser.addOption("B Simple", Autos.B_Simple());
        autoChooser.addOption("C Simple", Autos.C_Simple());
        autoChooser.addOption("D Simple", Autos.D_Simple());
        autoChooser.addOption("E Simple", Autos.E_Simple());

        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getSelectedAuto() {
        Command selected = autoChooser.getSelected();
        return selected != null ? selected : Commands.none();
    }
}
