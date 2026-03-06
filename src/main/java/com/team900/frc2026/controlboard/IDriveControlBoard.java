package com.team900.frc2026.controlboard;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface IDriveControlBoard {
    double getThrottle();

    double getStrafe();

    double getRotation();

    double getRotationY();

    Trigger resetGyro();
}
