package com.team900.frc2026.controlboard;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface IButtonControlBoard {

    Trigger shoot();

    Trigger stowHood();

    Trigger intake();

    Trigger deployIntake();

    Trigger exhaust();

    Trigger leftStick();

    Trigger rightStick();

    Trigger povUp();

    Trigger povDown();

    Trigger povLeft();

    Trigger povRight();

    void setRumble(boolean rumble);
}
