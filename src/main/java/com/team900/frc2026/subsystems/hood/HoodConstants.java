package com.team900.frc2026.subsystems.hood;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.util.Units;

public class HoodConstants {
    // TODO: in static just do khoodconfig. and then try to fill out as many of the fields with the
    // info you have
    public static ServoMotorSubsystemWithCanCoderConfig kHoodConfig =
            new ServoMotorSubsystemWithCanCoderConfig();

    static {
        kHoodConfig.name = "Hood";
        kHoodConfig.talonCANID = new CANDeviceId(19, Constants.kCanBusCanivoreMech);
        kHoodConfig.unitToRotorRatio = 2 * Math.PI;

        kHoodConfig.fxConfig = new TalonFXConfiguration();
        kHoodConfig.fxConfig.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        kHoodConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 120;
        kHoodConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 70;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;
        kHoodConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kHoodConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    }

    // this should all lowkey go into the khoodconfig bc none of the below constants are actually
    // used
    public static final CANDeviceId kHoodTalonCanID =
            new CANDeviceId(19, Constants.kCanBusCanivoreMech);
    public static final double kHoodGearRatio = 2125 / 8;
    // TODO: what should this even be what
    public static final double kHoodRotorMaxPosition = 0;
    public static final double kHoodRotorMinPosition = 0;
    public static final double kHoodToleranceRadians = 0.1;
    public static final double kHoodMinPositionRadians = 0.0;
    public static final double kHoodMaxPositionRadians =
            Units.rotationsToRadians(kHoodRotorMaxPosition * kHoodGearRatio);
    public static final double kHoodZeroedAngleDegrees = 15;

    public static final double kHoodEpsilon = Units.degreesToRadians(1.0);
    public static final double kHoodShootingEpsilon = Units.degreesToRadians(5.0);

    public static final double kHoodStowTrenchPositionRadians = 15.0;
}
