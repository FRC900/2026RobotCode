package com.team900.frc2026.subsystems.ShooterBottom;

import static edu.wpi.first.units.Units.Radians;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team900.frc2026.Constants.ShooterConstants;

import edu.wpi.first.units.Units;
import edu.wpi.first.hal.simulation.DIODataJNI;
import edu.wpi.first.units.AngularVelocityUnit;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterBottomSensorIOSim implements ShooterBottomSensorIO {
    protected final DigitalInput shooterBanner;

    private final int port;
    private final ServoMotorSubsystemConfig config;
    private final SimTalonFXIO wheelTalon;

    public ShooterBottomSensorIOSim(int dioPort, ServoMotorSubsystemConfig config) {
        super();
        this.port = dioPort;
        this.config = config;
        shooterBanner = new DigitalInput(dioPort);

        setHasNote();
        wheelTalon = new SimTalonFXIO(config);
    }

    public void setNoNote() {
        DIODataJNI.setValue(port, false);
    }

    public void setHasNote() {
        DIODataJNI.setValue(port, true);
    }

    @Override
    public void readInputs(ShooterBottomSensorInputs inputs) {
        wheelTalon.readInputs(inputs);
        inputs.bottomShooterBannerHasPiece = shooterBanner.get();
   

     //  inputs.wheelVelocity = Units.RotationsPerSecond.of(wheelTalon.getAngularVelocityRadPerSec() / (2.0*Math.PI));
     //  inputs.wheelVelocity = wheelTalon.rotorToUnits(wheelTalon.getAngularVelocityRadPerSec());
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
