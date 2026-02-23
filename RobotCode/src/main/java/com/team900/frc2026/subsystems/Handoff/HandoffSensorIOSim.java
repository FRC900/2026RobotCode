package com.team900.frc2026.subsystems.Handoff;

import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team254.lib.subsystems.TalonFXIO;
import com.team900.frc2026.Constants.ShooterConstants;

import edu.wpi.first.units.Units;
import edu.wpi.first.hal.simulation.DIODataJNI;
import edu.wpi.first.units.AngularVelocityUnit;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.DigitalInput;

public class HandoffSensorIOSim implements HandoffIO {
    protected final DigitalInput shooterBanner;

    private final int port;
    private final ServoMotorSubsystemConfig config;
    private final SimTalonFXIO wheelTalon;

    public HandoffSensorIOSim(int dioPort, ServoMotorSubsystemConfig config,SimTalonFXIO talon) {
        super();
        this.port = dioPort;
        this.config = config;
        shooterBanner = new DigitalInput(dioPort);
        this.wheelTalon = talon;

        setNoNote();

    }

    public SimTalonFXIO getTalon(){
        return this.wheelTalon;
    }

    public void setNoNote() {
        DIODataJNI.setValue(port, false);
    }

    public void setHasNote() {
        DIODataJNI.setValue(port, true);
    }

    @Override
    public void readInputs(HandoffSensorInputs inputs) {
        wheelTalon.readInputs(inputs);
        inputs.handoffBannerHasPiece = shooterBanner.get();
   


    }

    @Override
    public void setFlywheelSpeed(double speed) {
        wheelTalon.setVelocitySetpoint(speed);}

    public void resetTalon(){
        wheelTalon.resetSimState();
    }

    @Override
    public DigitalInput getBanner() {
        return null;
    }

}
