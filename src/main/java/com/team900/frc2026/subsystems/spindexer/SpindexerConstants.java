package com.team900.frc2026.subsystems.spindexer;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class SpindexerConstants {
    
   public static ServoMotorSubsystemConfig kSpindexerConfig = new ServoMotorSubsystemConfig();

     static {

        kSpindexerConfig.fxConfig = new TalonFXConfiguration();
        //TODO: upate can id here
        kSpindexerConfig.name = "Spindexer";
        kSpindexerConfig.talonCANID = new CANDeviceId(0, new CANBus(null));
    }

    public static final double kSpindexerDutyCycle = 0.5;
    public static final double kSpindexerDutyCycleExhaust = -0.5;
}
