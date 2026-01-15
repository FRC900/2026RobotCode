package com.team900.frc2026.subsystems.ShooterTop;

import edu.wpi.first.hal.simulation.DIODataJNI;

public class ShooterTopSensorIOSim extends ShooterTopSensorIOHardware{


    public ShooterTopSensorIOSim(int dioPort) {
        super(dioPort);
        setHasNote();

        
    }



    public void setNoNote(){
        DIODataJNI.setValue(this.getBanner().getChannel(), false);
    }

    
    public void setHasNote(){
        DIODataJNI.setValue(this.getBanner().getChannel(), true);
    }

}
