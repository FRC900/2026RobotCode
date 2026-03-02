package com.team900.frc2026.subsystems.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class IntakeRollerConstants {

    public static ServoMotorSubsystemConfig kIntakeConfig = new ServoMotorSubsystemConfig();

    static {
        kIntakeConfig.name = "Intake";
        kIntakeConfig.talonCANID = new CANDeviceId(40, new CANBus("mech"));
        kIntakeConfig.unitToRotorRatio = 1;


        kIntakeConfig.fxConfig = new TalonFXConfiguration();
        kIntakeConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kIntakeConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 80;
                kIntakeConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
                        kIntakeConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 70;
                                kIntakeConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
                                        kIntakeConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
                                        kIntakeConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kIntakeConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kIntakeConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;


    }
    public static final double kIntakeGearRatio = 1;
    public static final double kIntakeDutyCycle = 0.5;
    public static final double kIntakeDutyCycleExhaust = -0.5;
    public static final double kRollerGearRatio = 15/32;
}
