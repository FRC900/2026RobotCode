package com.team900.frc2026.controlboard;

import com.team900.frc2026.Constants;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class GamepadButtonControlBoard implements IDButtonControlBoard {
    private static GamepadButtonControlBoard instance = null;

    public static GamepadButtonControlBoard getInstance() {
        if (instance == null) {
            instance = new GamepadButtonControlBoard();
        }
        return instance;
    }

    private final CommandXboxController controller;

    private GamepadButtonControlBoard() {
        if (Constants.kForceDriveGamepad
                || DriverStation.getJoystickIsXbox(Constants.kDriveGamepadPort)) {
            controller = new CommandXboxController(Constants.kDriveGamepadPort);
        } else {
            controller = new CommandXboxController(Constants.kOperatorControllerPort);
        }
    }

    @Override
    public Trigger shoot() {
        return controller.rightBumper();
    }
}
