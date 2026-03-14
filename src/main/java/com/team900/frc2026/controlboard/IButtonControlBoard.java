package com.team900.frc2026.controlboard;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface IButtonControlBoard {

    Trigger shoot();

    Trigger shootAuto();

    Trigger stowHood();

    Trigger intake();

    Trigger toggleIntake();

    Trigger exhaust();

    Trigger leftStick();

    Trigger rightStick();

    Trigger povUp();

    Trigger povDown();

    Trigger povLeft();

    Trigger povRight();

    void setRumble(boolean rumble);

    Trigger resetHood();

    Trigger pass();

    Trigger toggleHoodMax();

    Trigger swerveAlignToHub();

    Trigger turretAlignToHub();
}
