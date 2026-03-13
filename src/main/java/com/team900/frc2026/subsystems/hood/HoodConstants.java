package com.team900.frc2026.subsystems.hood;

import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.team900.frc2026.Constants;
import com.team900.frc2026.Constants.Gains;
import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.CanCoderConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.util.Units;

public class HoodConstants {
    // position voltage
    public static final Gains COMP_GAINS = new Gains(130, 0, 15, 0.37, 100, 0, 0);
    public static final double kHoodGearRatio = 15.625 * 170. / 10.;

    public static final double kHoodToleranceRadians = 0.1;
    public static final double kHoodZeroedAngleDegrees = 15;

    public static final double kHoodRotorMaxPosition = 0.0754 + Units.degreesToRotations(15.0);
    public static final double kHoodRotorMinPosition = Units.degreesToRotations(15.0);

    public static final double kZeroingAmps = 23;
    public static final double kZeroingSeconds = 0.1;

    public static final double kHoodMinPositionRadians =
            Units.rotationsToDegrees(kHoodRotorMinPosition);
    public static final double kHoodMaxPositionRadians =
            Units.rotationsToRadians(kHoodRotorMaxPosition);

    public static final double kHoodEpsilon = Units.degreesToRadians(1.0);
    public static final double kHoodShootingEpsilon = Units.degreesToRadians(1);
    // TODO: find this experimetnatlly
    public static final double kHoodStowTrenchPositionRadians = Units.degreesToRadians(15.0);

    public static ServoMotorSubsystemWithCanCoderConfig kHoodConfig =
            new ServoMotorSubsystemWithCanCoderConfig();
    public static CanCoderConfig kHoodCanCoderConfig = new CanCoderConfig();

    static {
        // subsystem configs
        kHoodConfig.name = "Hood";

        // TODO: verify these tm
        kHoodConfig.cancoderToUnitsRatio = 2. * Math.PI;
        kHoodConfig.isFusedCancoder = true;
        kHoodConfig.kMaxPositionUnits = kHoodMaxPositionRadians - Units.degreesToRadians(3);
        kHoodConfig.kMinPositionUnits = kHoodMinPositionRadians;
        kHoodConfig.momentOfInertia = 0.0255356814;
        kHoodConfig.talonCANID = new CANDeviceId(34, Constants.kCanBusCanivoreMech);
        kHoodConfig.unitToRotorRatio = 2 * Math.PI;

        // configs for sim
        kHoodConfig.ratioForSim = kHoodGearRatio;
        kHoodConfig.cancoderUnitsForSim = 1;

        // cancoder config
        kHoodCanCoderConfig.CANID = new CANDeviceId(30, Constants.kCanBusCanivoreMech);
        kHoodCanCoderConfig.config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = 1;
        kHoodCanCoderConfig.config.MagnetSensor.MagnetOffset = -0.35;
        kHoodCanCoderConfig.config.MagnetSensor.SensorDirection =
                SensorDirectionValue.Clockwise_Positive;

        // fxConfig
        kHoodConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
        kHoodConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kHoodConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;

        kHoodConfig.fxConfig.Feedback.FeedbackRemoteSensorID =
                kHoodCanCoderConfig.CANID.getDeviceNumber();
        kHoodConfig.fxConfig.Feedback.FeedbackSensorSource =
                FeedbackSensorSourceValue.FusedCANcoder;
        kHoodConfig.fxConfig.Feedback.RotorToSensorRatio = 15.625;
        kHoodConfig.fxConfig.Feedback.SensorToMechanismRatio = 17;

        kHoodConfig.fxConfig.MotorOutput.ControlTimesyncFreqHz = 500;
        kHoodConfig.fxConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        kHoodConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        kHoodConfig.fxConfig.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        kHoodConfig.fxConfig.Slot0.StaticFeedforwardSign =
                StaticFeedforwardSignValue.UseVelocitySign;
        kHoodConfig.fxConfig.Slot0.kA = COMP_GAINS.ffkA();
        kHoodConfig.fxConfig.Slot0.kD = COMP_GAINS.kD();
        kHoodConfig.fxConfig.Slot0.kG = COMP_GAINS.ffkG();
        kHoodConfig.fxConfig.Slot0.kI = COMP_GAINS.kI();
        kHoodConfig.fxConfig.Slot0.kP = COMP_GAINS.kP();
        kHoodConfig.fxConfig.Slot0.kS = COMP_GAINS.ffkS();
        kHoodConfig.fxConfig.Slot0.kV = COMP_GAINS.ffkV();

        kHoodConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold = kHoodRotorMaxPosition;
        kHoodConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        kHoodConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold = kHoodRotorMinPosition;
        kHoodConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

        kHoodConfig.canCoderConfig = kHoodCanCoderConfig;
    }
}
