package com.team900.frc2026.controlboard;

import com.team900.frc2026.Constants;
import com.team900.frc2026.Robot;
import com.team900.lib.util.CommandSimPS5Controller;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class GamepadDriveControlBoard implements IDriveControlBoard {
    private static GamepadDriveControlBoard instance = null;

    public static GamepadDriveControlBoard getInstance() {
        if (instance == null) {
            instance = new GamepadDriveControlBoard();
        }

        return instance;
    }

    private final CommandPS5Controller controller;

    private GamepadDriveControlBoard() {
        if (Robot.isSimulation()) {
            controller = new CommandSimPS5Controller(Constants.kDriveGamepadPort);
        } else {
            controller = new CommandPS5Controller(Constants.kDriveGamepadPort);
        }
    }

    @Override
    public double getThrottle() {
        return -(Math.pow(Math.abs(controller.getLeftY()), 1.5))
                * Math.signum(controller.getLeftY());
    }

    @Override
    public double getStrafe() {
        return -(Math.pow(Math.abs(controller.getLeftX()), 1.5))
                * Math.signum(controller.getLeftX());
    }

    @Override
    public double getRotation() {
        return -(Math.pow(Math.abs(controller.getRightX()), 2.0))
                * Math.signum(controller.getRightX());
    }

    @Override
    public double getRotationY() {
        return -(Math.pow(Math.abs(controller.getRightY()), 2.0))
                * Math.signum(controller.getRightY());
    }

    @Override
    public Trigger resetGyro() {
        return controller.create().and(controller.options().negate());
    }
}
