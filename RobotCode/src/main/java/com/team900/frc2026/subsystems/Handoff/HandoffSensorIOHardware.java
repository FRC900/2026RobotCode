package com.team900.frc2026.subsystems.Handoff;

import com.team254.lib.subsystems.MotorInputs;
import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team254.lib.subsystems.TalonFXIO;

import edu.wpi.first.hal.simulation.DIODataJNI;
import edu.wpi.first.wpilibj.DigitalInput;

public class HandoffSensorIOHardware implements HandoffIO {
    private final int port;
    private final ServoMotorSubsystemConfig config;
    protected final DigitalInput shooterBanner;
    private final TalonFXIO wheelTalon;

    public HandoffSensorIOHardware(int dioPort, ServoMotorSubsystemConfig config, TalonFXIO talon) {
        this.port = dioPort;
        this.config = config;
        shooterBanner = new DigitalInput(dioPort);
        this.wheelTalon = talon;
    }

    @Override
    public void setFlywheelSpeed(double speed) {
        wheelTalon.setVelocitySetpoint(speed);}

    public void setNoNote() {
        DIODataJNI.setValue(port, false);
    }

    public TalonFXIO getTalon(){
        return this.wheelTalon;
    }

    public void setHasNote() {
        DIODataJNI.setValue(port, true);
    }

    @Override
    public void readInputs(MotorInputs inputs) {

        //inputs.handoffBannerHasPiece = shooterBanner.get();
    }

    @Override
    public DigitalInput getBanner() {
        return shooterBanner;
    }
}
