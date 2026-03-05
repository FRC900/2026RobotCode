package com.team900.lib.util;

import com.team900.frc2026.Constants;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class CommandSimPS5Controller extends CommandPS5Controller {
    private final SimPS5Controller m_hid;
    private final ControllerMapping mapping;

    public CommandSimPS5Controller(int port) {
        super(port);
        switch (Constants.kSimControllerType) {
            case XBOX:
                mapping = ControllerMappings.XBOX_MAPPING;
                m_hid = new SimPS5Controller(port, mapping);
                break;
            case DUAL_SENSE:
                mapping = ControllerMappings.DUALSENSE_MAPPING;
                m_hid = new SimPS5Controller(port, mapping);
                break;
            default:
                mapping = ControllerMappings.XBOX_MAPPING;
                m_hid = new SimPS5Controller(port, mapping);
                break;
        }
    }

    @Override
    public PS5Controller getHID() {
        return m_hid;
    }

    @Override
    public Trigger cross(EventLoop loop) {
        return button(mapping.getButton("A"), loop);
    }

    @Override
    public Trigger circle(EventLoop loop) {
        return button(mapping.getButton("B"), loop);
    }

    @Override
    public Trigger square(EventLoop loop) {
        return button(mapping.getButton("X"), loop);
    }

    @Override
    public Trigger triangle(EventLoop loop) {
        return button(mapping.getButton("Y"), loop);
    }

    @Override
    public Trigger L1(EventLoop loop) {
        return button(mapping.getButton("LeftBumper"), loop);
    }

    @Override
    public Trigger R1(EventLoop loop) {
        return button(mapping.getButton("RightBumper"), loop);
    }

    @Override
    public Trigger create(EventLoop loop) {
        return button(mapping.getButton("Back"), loop);
    }

    @Override
    public Trigger options(EventLoop loop) {
        return button(mapping.getButton("Start"), loop);
    }

    @Override
    public Trigger L3(EventLoop loop) {
        return button(mapping.getButton("LeftStick"), loop);
    }

    @Override
    public Trigger R3(EventLoop loop) {
        return button(mapping.getButton("RightStick"), loop);
    }

    @Override
    public Trigger L2(EventLoop loop) {
        return button(mapping.getAxis("LeftTrigger"), loop);
    }

    @Override
    public Trigger R2(EventLoop loop) {
        return button(mapping.getAxis("RightTrigger"), loop);
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
        return m_hid.getL2Axis();
    }

    @Override
    public double getR2Axis() {
        return m_hid.getR2Axis();
    }
}
