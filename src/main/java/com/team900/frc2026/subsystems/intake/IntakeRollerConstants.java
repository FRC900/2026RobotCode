package com.team900.frc2026.subsystems.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class IntakeRollerConstants {

    public static ServoMotorSubsystemConfig kIntakeRollerConfig = new ServoMotorSubsystemConfig();

    static {
        kIntakeRollerConfig.name = "Intake Roller";
        kIntakeRollerConfig.talonCANID = new CANDeviceId(60, Constants.kCanBusCanivoreMech);

        kIntakeRollerConfig.fxConfig = new TalonFXConfiguration();
        kIntakeRollerConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
        kIntakeRollerConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kIntakeRollerConfig.fxConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        kIntakeRollerConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // using arbitralily small value for not since handoff position and velocity isn't important
        kIntakeRollerConfig.momentOfInertia = 0.00042474;
    }

    public static final double kIntakeGearRatio = 1;
    public static final double kIntakeDutyCycle = 12;
    public static final double kIntakeDutyCycleExhaust = -8.5;
    public static final double kRollerGearRatio = 15 / 32;
}
