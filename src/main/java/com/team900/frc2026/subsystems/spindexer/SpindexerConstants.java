package com.team900.frc2026.subsystems.spindexer;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class SpindexerConstants {

    public static ServoMotorSubsystemConfig kSpindexerConfig = new ServoMotorSubsystemConfig();

    static {
        kSpindexerConfig.name = "Spindexer";
        kSpindexerConfig.talonCANID = new CANDeviceId(50, new CANBus("mech"));
        kSpindexerConfig.unitToRotorRatio = 1;


        kSpindexerConfig.fxConfig = new TalonFXConfiguration();
        kSpindexerConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kSpindexerConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 80;
                kSpindexerConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
                        kSpindexerConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 70;
                                kSpindexerConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
                                        kSpindexerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
                                        kSpindexerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kSpindexerConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kSpindexerConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;


    }

    public static final double kSpindexerGearRatio = 1;
    public static final double kSpindexerDutyCycle = 0.5;
    public static final double kSpindexerDutyCycleExhaust = -0.5;
}
