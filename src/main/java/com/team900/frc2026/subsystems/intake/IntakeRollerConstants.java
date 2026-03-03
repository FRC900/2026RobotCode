package com.team900.frc2026.subsystems.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class IntakeRollerConstants {

    public static ServoMotorSubsystemConfig kIntakeRollerConfig = new ServoMotorSubsystemConfig();

    static {
        kIntakeRollerConfig.fxConfig = new TalonFXConfiguration();
        kIntakeRollerConfig.name = "Intake";
        kIntakeRollerConfig.talonCANID = new CANDeviceId(60, new CANBus(null));
        // Top Roller - 16t:24t 24t:15t 30t:18t overall: 0.5625:1 
        // Middle Roller - 16t:24t 24t:15t 30t:18t 18t:18t overall: 0.5625:1 
        // Bottom Roller - 16t:24t 24t:15t 30t:18t 36t:36t 15t:15t overall: 0.5625:1 
        kIntakeRollerConfig.unitToRotorRatio = 16.0 / 24.0 * 24.0 / 15.0 * 30.0 / 18.0;
    }

    public static final double kIntakeDutyCycle = 0.5;
    public static final double kIntakeDutyCycleExhaust = -0.5;
}
