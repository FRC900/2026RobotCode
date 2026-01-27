package com.team900.frc2026.controlboard;

import com.team900.frc2026.Constants;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControlBoard implements IDButtonControlBoard {
    private static ControlBoard instance = null;

    private final ModalControls modalControls = ModalControls.getInstance();

    public static ControlBoard getInstance() {
        if (instance == null) {
            instance = new ControlBoard();
        }
        return instance;
    }

    final IDButtonControlBoard buttonControlBoard;

    private ControlBoard() {
        boolean useDriveGamepad =
                Constants.kForceDriveGamepad
                        || DriverStation.getJoystickIsXbox(Constants.kDriveGamepadPort);
        buttonControlBoard = GamepadButtonControlBoard.getInstance();
    }

    @Override
    public Trigger shoot() {
        return buttonControlBoard.shoot();
    }
}
