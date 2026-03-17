package com.team900.lib.util;

import edu.wpi.first.hal.FRCNetComm;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.event.BooleanEvent;
import edu.wpi.first.wpilibj.event.EventLoop;

/** A simulated Xbox controller that uses a provided ControllerMapping. */
public class SimPS5Controller extends PS5Controller {
    protected final ControllerMapping mapping;

    /**
     * Constructs an instance using the provided port and mapping.
     *
     * @param port The port index on the Driver Station.
     * @param mapping The mapping of button/axis names to raw values.
     */
    public SimPS5Controller(final int port, ControllerMapping mapping) {
        super(port);
        this.mapping = mapping;
        HAL.report(FRCNetComm.tResourceType.kResourceType_XboxController, port + 1);
    }

    @Override
    public double getLeftX() {
        return getRawAxis(mapping.getAxis("LeftX"));
    }

    @Override
    public double getRightX() {
        return getRawAxis(mapping.getAxis("RightX"));
    }

    @Override
    public double getLeftY() {
        return getRawAxis(mapping.getAxis("LeftY"));
    }

    @Override
    public double getRightY() {
        return getRawAxis(mapping.getAxis("RightY"));
    }

    @Override
    public double getL2Axis() {
        return getRawAxis(mapping.getAxis("LeftTrigger"));
    }

    @Override
    public BooleanEvent L2(EventLoop loop) {
        return button(mapping.getAxis("LeftTrigger"), loop);
    }

    @Override
    public double getR2Axis() {
        return getRawAxis(mapping.getAxis("RightTrigger"));
    }

    @Override
    public BooleanEvent R2(EventLoop loop) {
        return button(mapping.getAxis("RightTrigger"), loop);
    }

    @Override
    public boolean getCrossButton() {
        return getRawButton(mapping.getButton("A"));
    }

    @Override
    public boolean getCrossButtonPressed() {
        return getRawButtonPressed(mapping.getButton("A"));
    }

    @Override
    public boolean getCrossButtonReleased() {
        return getRawButtonReleased(mapping.getButton("A"));
    }

    @Override
    public BooleanEvent cross(EventLoop loop) {
        return button(mapping.getButton("A"), loop);
    }

    @Override
    public boolean getCircleButton() {
        return getRawButton(mapping.getButton("B"));
    }

    @Override
    public boolean getCircleButtonPressed() {
        return getRawButtonPressed(mapping.getButton("B"));
    }

    @Override
    public boolean getCircleButtonReleased() {
        return getRawButtonReleased(mapping.getButton("B"));
    }

    @Override
    public BooleanEvent circle(EventLoop loop) {
        return button(mapping.getButton("B"), loop);
    }

    @Override
    public boolean getSquareButton() {
        return getRawButton(mapping.getButton("X"));
    }

    @Override
    public boolean getSquareButtonPressed() {
        return getRawButtonPressed(mapping.getButton("X"));
    }

    @Override
    public boolean getSquareButtonReleased() {
        return getRawButtonReleased(mapping.getButton("X"));
    }

    @Override
    public BooleanEvent square(EventLoop loop) {
        return button(mapping.getButton("X"), loop);
    }

    @Override
    public boolean getTriangleButton() {
        return getRawButton(mapping.getButton("Y"));
    }

    @Override
    public boolean getTriangleButtonPressed() {
        return getRawButtonPressed(mapping.getButton("Y"));
    }

    @Override
    public boolean getTriangleButtonReleased() {
        return getRawButtonReleased(mapping.getButton("Y"));
    }

    @Override
    public BooleanEvent triangle(EventLoop loop) {
        return button(mapping.getButton("Y"), loop);
    }

    @Override
    public boolean getL1Button() {
        return getRawButton(mapping.getButton("LeftBumper"));
    }

    @Override
    public boolean getL1ButtonPressed() {
        return getRawButtonPressed(mapping.getButton("LeftBumper"));
    }

    @Override
    public boolean getL1ButtonReleased() {
        return getRawButtonReleased(mapping.getButton("LeftBumper"));
    }

    @Override
    public BooleanEvent L1(EventLoop loop) {
        return button(mapping.getButton("LeftBumper"), loop);
    }

    @Override
    public boolean getR2Button() {
        return getRawButton(mapping.getButton("RightBumper"));
    }

    @Override
    public boolean getR2ButtonPressed() {
        return getRawButtonPressed(mapping.getButton("RightBumper"));
    }

    @Override
    public boolean getR2ButtonReleased() {
        return getRawButtonReleased(mapping.getButton("RightBumper"));
    }

    @Override
    public BooleanEvent R1(EventLoop loop) {
        return button(mapping.getButton("RightBumper"), loop);
    }

    @Override
    public boolean getCreateButton() {
        return getRawButton(mapping.getButton("Back"));
    }

    @Override
    public boolean getCreateButtonPressed() {
        return getRawButtonPressed(mapping.getButton("Back"));
    }

    @Override
    public boolean getCreateButtonReleased() {
        return getRawButtonReleased(mapping.getButton("Back"));
    }

    @Override
    public BooleanEvent create(EventLoop loop) {
        return button(mapping.getButton("Back"), loop);
    }

    @Override
    public boolean getOptionsButton() {
        return getRawButton(mapping.getButton("Start"));
    }

    @Override
    public boolean getOptionsButtonPressed() {
        return getRawButtonPressed(mapping.getButton("Start"));
    }

    @Override
    public boolean getOptionsButtonReleased() {
        return getRawButtonReleased(mapping.getButton("Start"));
    }

    @Override
    public BooleanEvent options(EventLoop loop) {
        return button(mapping.getButton("Start"), loop);
    }

    @Override
    public boolean getL3Button() {
        return getRawButton(mapping.getButton("LeftStick"));
    }

    @Override
    public boolean getL3ButtonPressed() {
        return getRawButtonPressed(mapping.getButton("LeftStick"));
    }

    @Override
    public boolean getL3ButtonReleased() {
        return getRawButtonReleased(mapping.getButton("LeftStick"));
    }

    @Override
    public BooleanEvent L3(EventLoop loop) {
        return button(mapping.getButton("LeftStick"), loop);
    }

    @Override
    public boolean getR3Button() {
        return getRawButton(mapping.getButton("RightStick"));
    }

    @Override
    public boolean getR3ButtonPressed() {
        return getRawButtonPressed(mapping.getButton("RightStick"));
    }

    @Override
    public boolean getR3ButtonReleased() {
        return getRawButtonReleased(mapping.getButton("RightStick"));
    }

    @Override
    public BooleanEvent R3(EventLoop loop) {
        return button(mapping.getButton("RightStick"), loop);
    }
}
