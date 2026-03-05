package com.team900.frc2026.subsystems.intake;

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

public class IntakePivotConstants {
    // Intake radians will be measured from horizontal to avoid confusion where positive direction
    // is up
    public static final double kIntakePivotMinRadians = 0.0;
    public static final double kIntakePivotMaxRadians = 0.0;
    public static final double kIntakePivotToleranceRadians = 0.1;
    public static final double kIntakePivotStowRadians = 0.0;
    public static final double kIntakePivotDeployRadians = 1.0;

    public static final double kIntakeGearRatio = 41.9894179894;

    public static final Gains COMP_GAINS = new Gains(0, 0, 0, 0, 0, 0, 0);

    public static ServoMotorSubsystemWithCanCoderConfig kIntakePivotConfig =
            new ServoMotorSubsystemWithCanCoderConfig();
    public static CanCoderConfig kIntakeCanCoderConfig = new CanCoderConfig();

    static {
        kIntakePivotConfig.name = "Intake Pivot";
        kIntakePivotConfig.cancoderToUnitsRatio = 1;
        kIntakePivotConfig.isFusedCancoder = true;
        kIntakePivotConfig.kMaxPositionUnits = kIntakePivotMaxRadians;
        kIntakePivotConfig.kMinPositionUnits = kIntakePivotMinRadians;
        kIntakePivotConfig.momentOfInertia = 0.6065550876;
        kIntakePivotConfig.talonCANID = new CANDeviceId(61, Constants.kCanBusCanivoreMech);
        kIntakePivotConfig.unitToRotorRatio = 1;

        // configs for sim
        kIntakePivotConfig.ratioForSim = kIntakeGearRatio;
        kIntakePivotConfig.cancoderUnitsForSim = 2 * Math.PI;

        // cancoder config
        kIntakeCanCoderConfig.CANID = new CANDeviceId(62, Constants.kCanBusCanivoreMech);
        kIntakeCanCoderConfig.config.MagnetSensor.AbsoluteSensorDiscontinuityPoint = .7;
        kIntakeCanCoderConfig.config.MagnetSensor.MagnetOffset = 0.25;
        kIntakeCanCoderConfig.config.MagnetSensor.SensorDirection =
                SensorDirectionValue.Clockwise_Positive;

        // fxConfig
        kIntakePivotConfig.fxConfig.CurrentLimits.StatorCurrentLimit = 150;
        kIntakePivotConfig.fxConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 80;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLowerLimit = 40;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLowerTime = 1;

        kIntakePivotConfig.fxConfig.Feedback.FeedbackRemoteSensorID =
                kIntakeCanCoderConfig.CANID.getDeviceNumber();
        kIntakePivotConfig.fxConfig.Feedback.FeedbackSensorSource =
                FeedbackSensorSourceValue.FusedCANcoder;
        kIntakePivotConfig.fxConfig.Feedback.RotorToSensorRatio = 18.3704;
        kIntakePivotConfig.fxConfig.Feedback.SensorToMechanismRatio = 2;

        kIntakePivotConfig.fxConfig.MotorOutput.ControlTimesyncFreqHz = 500;
        kIntakePivotConfig.fxConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        kIntakePivotConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        kIntakePivotConfig.fxConfig.Slot0.GravityType = GravityTypeValue.Arm_Cosine;
        kIntakePivotConfig.fxConfig.Slot0.StaticFeedforwardSign =
                StaticFeedforwardSignValue.UseVelocitySign;
        kIntakePivotConfig.fxConfig.Slot0.kA = COMP_GAINS.ffkA();
        kIntakePivotConfig.fxConfig.Slot0.kD = COMP_GAINS.kD();
        kIntakePivotConfig.fxConfig.Slot0.kG = COMP_GAINS.ffkG();
        kIntakePivotConfig.fxConfig.Slot0.kI = COMP_GAINS.kI();
        kIntakePivotConfig.fxConfig.Slot0.kP = COMP_GAINS.kP();
        kIntakePivotConfig.fxConfig.Slot0.kS = COMP_GAINS.ffkS();
        kIntakePivotConfig.fxConfig.Slot0.kV = COMP_GAINS.ffkV();

        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
                Units.radiansToRotations(kIntakePivotMaxRadians);
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
                Units.radiansToRotations(kIntakePivotMinRadians);
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;

        kIntakePivotConfig.canCoderConfig = kIntakeCanCoderConfig;
    }
}
