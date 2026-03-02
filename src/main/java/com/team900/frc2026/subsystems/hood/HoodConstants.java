package com.team900.frc2026.subsystems.hood;

import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.util.Units;

public class HoodConstants {
    // TODO: in static just do khoodconfig. and then try to fill out as many of the fields with the
    // info you have
    public static ServoMotorSubsystemWithCanCoderConfig kHoodConfig =
            new ServoMotorSubsystemWithCanCoderConfig();

            

    // this should all lowkey go into the khoodconfig bc none of the below constants are actually
    // used
    public static final CANDeviceId kHoodTalonCanID =
            new CANDeviceId(19, Constants.kCanBusCanivoreMech);
    public static final double kHoodGearRatio = 0;
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
