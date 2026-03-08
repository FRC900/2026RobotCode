package com.team900.frc2026.subsystems.handoff;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class HandoffConstants {

    public static ServoMotorSubsystemConfig kHandoffConfig = new ServoMotorSubsystemConfig();

    static {
        kHandoffConfig.name = "Handoff";
        kHandoffConfig.talonCANID = new CANDeviceId(40, Constants.kCanBusCanivoreMech);
        // 1:1 for all wheels
        // Above motor - 18t:18t 30t:30t
        // First wheels - 18t:18t 18t:18t
        // Second wheels - 18t:18t 18t:18t 18t:18t
        kHandoffConfig.unitToRotorRatio = 1.0;

        kHandoffConfig.fxConfig = new TalonFXConfiguration();
        kHandoffConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        // TODO: experimentally find the current limits which we need
        kHandoffConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 80;
        kHandoffConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kHandoffConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 70;
        kHandoffConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kHandoffConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kHandoffConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kHandoffConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kHandoffConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        // using arbitralily small value for not since handoff position and velocity isn't important
        kHandoffConfig.momentOfInertia = 0.00042474;
    }

    public static final double kHandoffGearRatio = 1;
    public static final double kHandoffDutyCycle = 1;
    public static final double kHandoffDutyCycleExhaust = -0.5;
}
