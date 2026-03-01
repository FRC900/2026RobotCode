package com.team900.frc2026.subsystems.handoff;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class HandoffConstants {

    public static ServoMotorSubsystemConfig kHandoffConfig = new ServoMotorSubsystemConfig();

    static {
        kHandoffConfig.fxConfig = new TalonFXConfiguration();
        // TODO: upate can id here
        kHandoffConfig.name = "Handoff";
        kHandoffConfig.talonCANID = new CANDeviceId(0, new CANBus(null));
    }

    public static final double kHandoffDutyCycle = 0.5;
    public static final double kHandoffDutyCycleExhaust = -0.5;
}
