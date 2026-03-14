package com.team900.frc2026.controlboard;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ControlBoard implements IDriveControlBoard, IButtonControlBoard {
    private static ControlBoard instance = null;

    public static ControlBoard getInstance() {
        if (instance == null) {
            instance = new ControlBoard();
        }
        return instance;
    }

    private final IDriveControlBoard driveControlBoard;
    private final IButtonControlBoard buttonControlBoard;

    private ControlBoard() {
        driveControlBoard = GamepadDriveControlBoard.getInstance();
        buttonControlBoard = GamepadButtonControlBoard.getInstance();
    }

    @Override
    public double getThrottle() {
        return driveControlBoard.getThrottle();
    }

    @Override
    public double getStrafe() {
        return driveControlBoard.getStrafe();
    }

    @Override
    public double getRotation() {
        return driveControlBoard.getRotation();
    }

    @Override
    public double getRotationY() {
        return driveControlBoard.getRotationY();
    }

    @Override
    public Trigger resetGyro() {
        return driveControlBoard.resetGyro();
    }

    @Override
    public Trigger intake() {
        return buttonControlBoard.intake();
    }

    @Override
    public Trigger exhaust() {
        return buttonControlBoard.exhaust();
    }

    @Override
    public Trigger leftStick() {
        return buttonControlBoard.leftStick();
    }

    @Override
    public Trigger rightStick() {
        return buttonControlBoard.rightStick();
    }

    @Override
    public Trigger swerveAlignToHub() {
        return buttonControlBoard.swerveAlignToHub();
    }

    @Override
    public Trigger turretAlignToHub() {
        return buttonControlBoard.turretAlignToHub();
    }

    @Override
    public Trigger povUp() {
        return buttonControlBoard.povUp();
    }

    @Override
    public Trigger povDown() {
        return buttonControlBoard.povDown();
    }

    @Override
    public Trigger povLeft() {
        return buttonControlBoard.povLeft();
    }

    @Override
    public Trigger povRight() {
        return buttonControlBoard.povRight();
    }

    @Override
    public void setRumble(boolean rumble) {
        buttonControlBoard.setRumble(rumble);
    }

    public Command rumble() {
        return Commands.startEnd(() -> setRumble(true), () -> setRumble(false));
    }

    @Override
    public Trigger shoot() {
        return buttonControlBoard.shoot();
    }

    @Override
    public Trigger shootAuto() {
        return buttonControlBoard.shootAuto();
    }

    @Override
    public Trigger stowHood() {
        return buttonControlBoard.stowHood();
    }

    @Override
    public Trigger toggleIntake() {
        return buttonControlBoard.toggleIntake();
    }

    @Override
    public Trigger resetHood() {
        return buttonControlBoard.resetHood();
    }

    @Override
    public Trigger pass() {
        return buttonControlBoard.pass();
    }

    @Override
    public Trigger toggleHoodMax() {
        return buttonControlBoard.toggleHoodMax();
    }
}
