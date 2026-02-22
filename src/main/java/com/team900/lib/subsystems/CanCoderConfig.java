package com.team900.lib.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.team900.lib.drivers.CANDeviceId;

public class CanCoderConfig {
    public CANDeviceId CANID;
    public CANcoderConfiguration config = new CANcoderConfiguration();
}
