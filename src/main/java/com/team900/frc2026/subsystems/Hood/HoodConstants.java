package com.team900.frc2026.subsystems.hood;

import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import edu.wpi.first.math.util.Units;

public class HoodConstants {
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
