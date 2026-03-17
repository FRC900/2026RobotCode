package com.team900.lib.util;

import com.ctre.phoenix6.CANBus;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.littletonrobotics.junction.Logger;

public class CANBusStatusLogger {
    private final CANBus bus;

    public CANBusStatusLogger(CANBus bus) {
        this.bus = bus;
    }

    public void logStatus() {
        var status = bus.getStatus();
        Logger.recordOutput("CANBusStatus/" + bus.getName(), status.Status);
        SmartDashboard.putString("CANBusStatus/" + bus.getName(), status.Status.getName());
    }
}
