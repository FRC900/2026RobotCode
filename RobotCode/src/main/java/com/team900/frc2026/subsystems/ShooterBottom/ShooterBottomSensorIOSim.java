package com.team900.frc2026.subsystems.ShooterBottom;

import edu.wpi.first.hal.simulation.DIODataJNI;

public class ShooterBottomSensorIOSim extends ShooterBottomSensorIOHardware {

    public ShooterBottomSensorIOSim(int dioPort) {
        super(dioPort);
        setHasNote();
    }

    public void setNoNote() {
        DIODataJNI.setValue(this.getBanner().getChannel(), false);
    }

    public void setHasNote() {
        DIODataJNI.setValue(this.getBanner().getChannel(), true);
    }
}
