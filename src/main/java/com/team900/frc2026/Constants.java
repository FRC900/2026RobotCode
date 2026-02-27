package com.team900.frc2026;

import edu.wpi.first.math.util.Units;

import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;
import com.team254.lib.drivers.CANDeviceId;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.wpilibj.RobotBase;
import java.util.Arrays;

public class Constants {

    public static final Mode simMode = Mode.SIM;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;
    public static final SimControllerType kSimControllerType = SimControllerType.XBOX;

    public static final boolean tuningMode = false;

    public static final double kRealDt = 0.02;

    public enum SimControllerType {
        XBOX,
        DUAL_SENSE
    }

    public enum Mode {
        /** Running on a real robot. */
        REAL,

        /** Running a physics simulator. */
        SIM,

        /** Replaying from a log file. */
        REPLAY
    }

    public static boolean disableHAL = false;

    public static void disableHAL() {
        disableHAL = true;
    }

    // TODO: temporary code to be changed to reflect rebuilt map
    public static final AprilTagFieldLayout kAprilTagLayout =
            AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded);
    public static final int[] kAllowedTagIDs = {17, 18, 19, 20, 21, 22, 6, 7, 8, 9, 10, 11};
    public static final AprilTagFieldLayout kAprilTagLayoutReefsOnly =
            new AprilTagFieldLayout(
                    kAprilTagLayout.getTags().stream()
                            .filter(
                                    tag ->
                                            Arrays.stream(kAllowedTagIDs)
                                                    .anyMatch(element -> element == tag.ID))
                            .toList(),
                    kAprilTagLayout.getFieldLength(),
                    kAprilTagLayout.getFieldWidth());

    public static final double kFieldWidthMeters = kAprilTagLayout.getFieldWidth();
    public static final double kFieldLengthMeters = kAprilTagLayout.getFieldLength();

    public record Gains(
            double kP, double kI, double kD, double ffkS, double ffkV, double ffkA, double ffkG) {}


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
