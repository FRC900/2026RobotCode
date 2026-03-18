package com.team900.frc2026.subsystems.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class IntakeRollerConstants {

    public static ServoMotorSubsystemConfig kIntakeRollerConfig = new ServoMotorSubsystemConfig();

    static {
        kIntakeRollerConfig.name = "Intake";
        kIntakeRollerConfig.talonCANID = new CANDeviceId(60, new CANBus("mech"));
        // Top Roller - 16t:24t 24t:15t 30t:15t overall: 0.46875:1
        // Middle Roller - 16t:24t 24t:15t 30t:15t 24t:24t overall: 0.46875:1
        // Bottom Roller - 16t:24t 24t:15t 30t:15t 36t:36t 15t:15t overall: 0.46875:1
        kIntakeRollerConfig.unitToRotorRatio = 16.0 / 24.0 * 24.0 / 15.0 * 30.0 / 15.0;


        kIntakeRollerConfig.fxConfig = new TalonFXConfiguration();
        kIntakeRollerConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kIntakeRollerConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 80;
                kIntakeRollerConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
                        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 70;
                                kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
                                        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
                                        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kIntakeRollerConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kIntakeRollerConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;


    public static final double kIntakeGearRatio = 1;
    public static final double kIntakeDutyCycle = 12;
    public static final double kIntakeDutyCycleExhaust = -12;
    public static final double kRollerGearRatio = 15 / 32;
    public static final double stallCurrent = 100;
    public static final double stallRPS = 1;
}
