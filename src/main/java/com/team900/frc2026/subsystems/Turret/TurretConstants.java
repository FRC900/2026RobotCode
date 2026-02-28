package com.team900.frc2026.subsystems.turret;

import com.team900.frc2026.Constants;
import com.team900.lib.drivers.CANDeviceId;
import edu.wpi.first.math.util.Units;

public class TurretConstants {

    public static final double kTurretGearRatio = 1 / 22;
    public static final CANDeviceId kTurretTalonCanID =
            new CANDeviceId(21, Constants.kCanBusCanivoreMech);
    public static final CANDeviceId kTurret33To1CANCoder =
            new CANDeviceId(13, Constants.kCanBusCanivoreMech);
    public static final CANDeviceId kTurret29To1CANCoder =
            new CANDeviceId(14, Constants.kCanBusCanivoreMech);
    public static final double k33To1TurretCancoderOffset = -0.057617;
    public static final double k29To1TurretCancoderOffset = 0.482178;
    // TODO: add right thign here
    public static final double kTurretMinPositionRadians = -2. * Math.PI;
    public static final double kTurretMaxPositionRadians = 2. * Math.PI;

    public static final double kTurretEpsilon = Units.degreesToRadians(2.0);
    public static final double kTurretShootingEpsilon = Units.degreesToRadians(5.0);

    public static final double toleranceRad = 0.1;
}
