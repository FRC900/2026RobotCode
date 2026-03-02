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
    }

    public static final double kIntakeDutyCycle = 0.5;
    public static final double kIntakeDutyCycleExhaust = -0.5;
    public static final double kRollerGearRatio = 15/32;
}
