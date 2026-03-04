package com.team900.frc2026.subsystems.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;

public class IntakePivotConstants {

    public static ServoMotorSubsystemWithCanCoderConfig kIntakePivotConfig =
            new ServoMotorSubsystemWithCanCoderConfig();

    static {
        kIntakePivotConfig.fxConfig = new TalonFXConfiguration();
        kIntakePivotConfig.name = "IntakePivot";
        kIntakePivotConfig.talonCANID = new CANDeviceId(61, Constants.kCanBusCanivoreMech);
        kIntakePivotConfig.canCoderConfig.CANID = new CANDeviceId(0, Constants.kCanBusCanivoreMech);
        // Jackshaft overall gear ratio motor:pivot is ~42:1
        kIntakePivotConfig.unitToRotorRatio = 41.9894179894;
        kIntakePivotConfig.cancoderToUnitsRatio = 1.0;
    }

    // TODO: update this after they put on cancoder
    public static final double kIntakePivotMinRadians = 0.0;
    public static final double kIntakePivotMaxRadians = 0.0;
    public static final double kIntakePivotToleranceRadians = 0.1;
    public static final double kIntakePivotStowRadians = 0.0;
    public static final double kIntakePivotDeployRadians = 1.0;
}
