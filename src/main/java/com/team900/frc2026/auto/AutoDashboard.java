package com.team900.frc2026.auto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class AutoDashboard {

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public AutoDashboard() {
        autoChooser.setDefaultOption("Two-Swipe (default)", Autos.TwoSwipe());

        // Swipe autos
        // right = normal side
        // left = Y-mirrored
        autoChooser.addOption("One-Swipe", Autos.OneSwipe());
        autoChooser.addOption("One-Swipe Center to Left", Autos.OneSwipeCenter_Left());
        autoChooser.addOption("One-Swipe Center to Right", Autos.OneSwipeCenter_Right());

        autoChooser.addOption("Two-Swipe", Autos.TwoSwipe());
        autoChooser.addOption("Two-Swipe Center to Left", Autos.TwoSwipeCenter_Left());
        autoChooser.addOption("Two-Swipe Center to Right", Autos.TwoSwipeCenter_Right());

        autoChooser.addOption("Three-Swipe", Autos.ThreeSwipe());
        autoChooser.addOption("Three-Swipe Center to Left", Autos.ThreeSwipeCenter_Left());
        autoChooser.addOption("Three-Swipe Center to Right", Autos.ThreeSwipeCenter_Right());

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
