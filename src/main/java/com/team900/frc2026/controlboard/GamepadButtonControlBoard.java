package com.team900.frc2026.controlboard;

import com.team900.frc2026.Constants;
import com.team900.frc2026.Robot;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class GamepadButtonControlBoard implements IButtonControlBoard {
    private static GamepadButtonControlBoard instance = null;

    public static GamepadButtonControlBoard getInstance() {
        if (instance == null) {
            instance = new GamepadButtonControlBoard();
        }
        return instance;
    }

    private final CommandPS5Controller controller;

    private final CommandPS5Controller additionalController;

    @SuppressWarnings("unused")
    private GamepadButtonControlBoard() {
        if (Robot.isSimulation()) {
            controller = new CommandPS5Controller(Constants.kDriveGamepadPort);
        } else {
            controller = new CommandPS5Controller(Constants.kDriveGamepadPort);
        }
        additionalController = new CommandPS5Controller(Constants.kOperatorControllerPort);
    }

    @Override
    public Trigger intake() {
        return controller.L2();
    }

    @Override
    public Trigger toggleIntake() {
        return controller.L1();
    }

    @Override
    public Trigger exhaust() {
        return additionalController.circle();
    }

    @Override
    public Trigger leftStick() {
        return controller.L3();
    }

    @Override
    public Trigger rightStick() {
        return controller.R3();
    }

    @Override
    public Trigger povUp() {
        return controller.povUp();
    }

    @Override
    public Trigger povDown() {
        return controller.povDown();
    }

    @Override
    public Trigger povLeft() {
        return controller.povLeft();
    }

    @Override
    public Trigger povRight() {
        return controller.povRight();
    }

    @Override
    public void setRumble(boolean rumble) {
        controller.getHID().setRumble(RumbleType.kBothRumble, rumble ? 1 : 0);
    }

    @Override
    public Trigger shoot() {
        return controller.R2();
    }

    @Override
    public Trigger shootAuto() {
        return controller.triangle();
    }

    @Override
    public Trigger stowHood() {
        return controller.R1().or(additionalController.R1());
    }
}
