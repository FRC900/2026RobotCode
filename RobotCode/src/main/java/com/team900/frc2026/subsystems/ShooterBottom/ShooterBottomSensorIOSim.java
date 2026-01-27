package com.team900.frc2026.subsystems.ShooterBottom;

import com.ctre.phoenix6.hardware.TalonFX;
import com.team900.frc2026.Constants.ShooterConstants;
import edu.wpi.first.hal.simulation.DIODataJNI;
import edu.wpi.first.units.measure.AngularVelocity;

public class ShooterBottomSensorIOSim extends ShooterBottomSensorIOHardware {

    private final TalonFX wheelTalon;

    public ShooterBottomSensorIOSim(int dioPort) {
        super(dioPort);
        setHasNote();
        wheelTalon = new TalonFX(3, ShooterConstants.CAN_BUS);
    }

    public void setNoNote() {
        DIODataJNI.setValue(this.getBanner().getChannel(), false);
    }

    public void setHasNote() {
        DIODataJNI.setValue(this.getBanner().getChannel(), true);
    }

    @Override
    public void setFlywheelSpeed(AngularVelocity speed) {
        wheelTalon.setControl(
                new com.ctre.phoenix6.controls.VelocityVoltage(0).withVelocity(speed).withSlot(0));
    }
}
