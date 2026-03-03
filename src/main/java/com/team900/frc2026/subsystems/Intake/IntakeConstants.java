package com.team900.frc2026.subsystems.intake;

import com.team900.lib.drivers.CANDeviceId;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.util.Units;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class IntakeConstants {
    
    public static final CANBus kCANBus = new CANBus("enable-only", "./logs/example.hoot");
    //Actually put in all positions later
    public static final double kIntakeDutyCycleIntake = 1.0;
    public static final double kIntakeDutyCycleExhaust = -1.0;

    public static final double kIntakePivotStowPositionRadians = Units.degreesToRadians(0);
    public static final double kIntakePivotDeployPositionRadians = 0.0;
    public static final double kIntakePivotToleranceRadians = 0.0;
    public static final double kIntakeRollerRadius = 0.0; 

    public static final double kIntakePivotCancoderOffset = 0.0;

    public static final ServoMotorSubsystemConfig kIntakeRollerConfig =
            new ServoMotorSubsystemConfig();

    static {
        kIntakeRollerConfig.name = "Intake_Roller";
        kIntakeRollerConfig.talonCANID = new CANDeviceId(21, kCANBus);
        kIntakeRollerConfig.momentOfInertia = 0.0;
        kIntakeRollerConfig.unitToRotorRatio = (0.0);

        kIntakeRollerConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kIntakeRollerConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
    }

    public static final ServoMotorSubsystemWithCanCoderConfig kIntakePivotConfig =
            new ServoMotorSubsystemWithCanCoderConfig();

    static {
        kIntakePivotConfig.name = "Intake_Pivot";
        kIntakePivotConfig.talonCANID = new CANDeviceId(22, kCANBus);
        kIntakePivotConfig.momentOfInertia = 0.0;
        kIntakePivotConfig.fxConfig.Slot0.kP = 0.0;
        kIntakePivotConfig.fxConfig.Slot0.kD = 0.0;
        kIntakePivotConfig.fxConfig.Slot0.kV = 0.0;
        kIntakePivotConfig.fxConfig.MotionMagic.MotionMagicCruiseVelocity = 0.0;
        kIntakePivotConfig.fxConfig.MotionMagic.MotionMagicAcceleration = 0.0;
        kIntakePivotConfig.unitToRotorRatio = Units.rotationsToRadians(0.0);

        kIntakePivotConfig.canCoderConfig.CANID =
                new CANDeviceId(30, kCANBus);
        kIntakePivotConfig.canCoderConfig.config.MagnetSensor.MagnetOffset =
                IntakeConstants.kIntakePivotCancoderOffset;
        kIntakePivotConfig.cancoderToUnitsRatio = Units.rotationsToRadians(1);

        kIntakePivotConfig.kMaxPositionUnits = 0.0;
        kIntakePivotConfig.kMinPositionUnits = Units.degreesToRadians(0.0);
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
                kIntakePivotConfig.kMaxPositionUnits / kIntakePivotConfig.unitToRotorRatio;
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        kIntakePivotConfig.fxConfig.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
                kIntakePivotConfig.kMinPositionUnits / kIntakePivotConfig.unitToRotorRatio;

        kIntakePivotConfig.fxConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        kIntakePivotConfig.fxConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        kIntakePivotConfig.fxConfig.CurrentLimits.SupplyCurrentLimit = 40.0;
    }
}
