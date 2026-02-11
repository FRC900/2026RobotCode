package com.team900.frc2026;

import edu.wpi.first.math.util.Units;

import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.team254.lib.drivers.CANDeviceId;

public class Constants {
    public static final String kCanBusCanivore = "canivore";
    
    public static final ClosedLoopRampsConfigs makeDefaultClosedLoopRampConfig() {
        return new ClosedLoopRampsConfigs()
                .withDutyCycleClosedLoopRampPeriod(0.02)
                .withTorqueClosedLoopRampPeriod(0.02)
                .withVoltageClosedLoopRampPeriod(0.02);
    }

    public static final OpenLoopRampsConfigs makeDefaultOpenLoopRampConfig() {
        return new OpenLoopRampsConfigs()
                .withDutyCycleOpenLoopRampPeriod(0.02)
                .withTorqueOpenLoopRampPeriod(0.02)
                .withVoltageOpenLoopRampPeriod(0.02);
    }

    public static final class HoodConstants {
        public static final CANDeviceId kHoodTalonCanID = new CANDeviceId(19, kCanBusCanivore);
        public static final double kHoodGearRatio = (14.0 / 48.0) * (15.0 / 36.0) * (10.0 / 160.0);
        public static final double kHoodRotorMaxPosition = 15.368164;
        public static final double kHoodRotorMinPosition = 0.0;
        public static final double kHoodPositionTolerance = 0.1;
        public static final double kHoodMinPositionRadians = 0.0;
        public static final double kHoodMaxPositionRadians = Units
                .rotationsToRadians(kHoodRotorMaxPosition * kHoodGearRatio);
        public static final double kHoodZeroedAngleDegrees = 51.7;
        public static final double kHoodEpsilon = Units.degreesToRadians(1.0);
        public static final double kHoodShootingEpsilon = Units.degreesToRadians(5.0);

        public static final double kFenderShotRadians = Units.degreesToRadians(0);
    }
}
