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
        kIntakeConfig.talonCANID = new CANDeviceId(60, new CANBus("mech"));
        // Top Roller - 16t:24t 24t:15t 30t:15t overall: 0.46875:1
        // Middle Roller - 16t:24t 24t:15t 30t:15t 24t:24t overall: 0.46875:1
        // Bottom Roller - 16t:24t 24t:15t 30t:15t 36t:36t 15t:15t overall: 0.46875:1
        kIntakeConfig.unitToRotorRatio = 16.0 / 24.0 * 24.0 / 15.0 * 30.0 / 15.0;


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
    public static final double kIntakeGearRatio = 0.46875;
    public static final double kIntakeDutyCycle = 0.5;
    public static final double kIntakeDutyCycleExhaust = -0.5;
}
