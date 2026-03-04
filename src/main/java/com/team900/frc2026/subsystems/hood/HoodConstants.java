package com.team900.frc2026.subsystems.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.util.Units;

public class HoodConstants {

    public static ServoMotorSubsystemWithCanCoderConfig kHoodConfig =
            new ServoMotorSubsystemWithCanCoderConfig();

    public static final double kHoodGearRatio = 15.625;
    public static final double kHoodToleranceRadians = 0.1;
    // TODO: Update these values with tunerX
    public static final double kHoodMinPositionRadians = 0.0;
    public static final double kHoodMaxPositionRadians = Units.degreesToRadians(45.0);
    public static final double kHoodZeroedAngleDegrees = 15;
    public static final double kHoodEpsilon = Units.degreesToRadians(1.0);
    public static final double kHoodShootingEpsilon = Units.degreesToRadians(5.0);
    public static final double kHoodStowTrenchPositionRadians = 15.0;

    static {
        kHoodConfig.name = "Hood";
        // TODO: Get hood talon motor CAN ID and update, cancoder is correct
        kHoodConfig.talonCANID = new CANDeviceId(29, Constants.kCanBusCanivoreMech);
        kHoodConfig.canCoderConfig.CANID = new CANDeviceId(30, Constants.kCanBusCanivoreMech);
        kHoodConfig.fxConfig = new TalonFXConfiguration();
        kHoodConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kHoodConfig.unitToRotorRatio = 2.0 * Math.PI / kHoodGearRatio;
        kHoodConfig.cancoderToUnitsRatio = 2.0 * Math.PI;
        kHoodConfig.kMinPositionUnits = kHoodMinPositionRadians;
        kHoodConfig.kMaxPositionUnits = kHoodMaxPositionRadians;
    }
}
