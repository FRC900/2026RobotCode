package com.team900.lib.drivers;

import com.ctre.phoenix6.CANBus;

public class CANDeviceId {
    private final int deviceNumber;
    private final CANBus bus;


    public CANDeviceId(int deviceNumber, CANBus bus) {
        this.deviceNumber = deviceNumber;
        this.bus = bus;
    }

    // Use the default bus name (empty string).
    public CANDeviceId(int deviceNumber) {
        this(deviceNumber, new CANBus());
    }

    public int getDeviceNumber() {
        return deviceNumber;
    }

    public CANBus getBus() {
        return bus;
    }

    public boolean equals(CANDeviceId other) {
        return other.deviceNumber == deviceNumber && other.bus == bus;
    }

}
