package com.team900.frc2026.subsystems.Hood;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.team900.frc2026.Robot;
import com.team254.lib.util.CTREUtil; 

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Current;
import com.team900.frc2026.Constants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;

/**
 * Hardware implementation of the HoodIO interface for the hood subsystem.
 * This class handles the hardware interaction with the TalonFX motor
 * controller, managing
 * the hood's position, velocity, applied voltage, and current. It also provides
 * methods
 * to set control modes and reset the hood's position to a zero point.
 */

public class HoodIOHardware implements HoodIO {
    protected final TalonFX hoodMotor = new TalonFX(Constants.HoodConstants.kHoodTalonCanID.getDeviceNumber(),
            Constants.HoodConstants.kHoodTalonCanID.getBus());
    private PositionVoltage positionControl = new PositionVoltage(0);
    private DutyCycleOut dutyCycleOut = new DutyCycleOut(0);
    private final StatusSignal<Angle> positionSignal = hoodMotor.getPosition();
    private final StatusSignal<AngularVelocity> velocitySignal = hoodMotor.getVelocity();
    private final StatusSignal<Voltage> voltsSignal = hoodMotor.getMotorVoltage();
    private final StatusSignal<Current> currentStatorSignal = hoodMotor.getStatorCurrent();
    private final StatusSignal<Current> currentSupplySignal = hoodMotor.getSupplyCurrent();
    private final double kV = 0.116;

    public HoodIOHardware() {
        var config = new TalonFXConfiguration();
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        config.Audio.BeepOnBoot = false;
        config.Audio.BeepOnConfig = false;

        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold = Constants.HoodConstants.kHoodRotorMaxPosition
                - Constants.HoodConstants.kHoodPositionTolerance;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold = 0.7;

        config.Slot0.kS = 0.18;
        config.Slot0.kP = 8.0;
        config.Slot0.kD = 0.1;
        config.Slot0.kV = kV;
        config.Slot0.kA = 0.0001 * 12.0;

        if (Robot.isReal()) {
            config.CurrentLimits.StatorCurrentLimit = 50.0;
            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.ClosedLoopRamps = Constants.makeDefaultClosedLoopRampConfig();
            config.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.01;
            config.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        }

        config.MotionMagic.MotionMagicAcceleration = 1000.0;
        config.MotionMagic.MotionMagicCruiseVelocity = 100.0;
        CTREUtil.applyConfiguration(hoodMotor, config);
        hoodMotor.setPosition(0.0);
        BaseStatusSignal.setUpdateFrequencyForAll(50, positionSignal, velocitySignal, voltsSignal, currentStatorSignal,
                currentSupplySignal);
        hoodMotor.optimizeBusUtilization();
    }

    @Override
    public void readInputs(HoodInputs inputs) {
        BaseStatusSignal.refreshAll(positionSignal, velocitySignal, voltsSignal, currentStatorSignal,
                currentSupplySignal);
        inputs.positionRad = Units
                .rotationsToRadians(positionSignal.getValueAsDouble() * Constants.HoodConstants.kHoodGearRatio);
        inputs.positionRotations = positionSignal.getValueAsDouble();
        inputs.velocityRadPerSec = Units
                .rotationsToRadians(velocitySignal.getValueAsDouble() * Constants.HoodConstants.kHoodGearRatio);
        inputs.appliedVolts = voltsSignal.getValueAsDouble();
        inputs.currentStatorAmps = currentStatorSignal.getValueAsDouble();
        inputs.currentSupplyAmps = currentSupplySignal.getValueAsDouble();
    }

    @Override
    public void setNeutralMode(NeutralModeValue mode) {
        hoodMotor.setNeutralMode(mode);
    }

    @Override
    public void setPositionSetpoint(double radiansFromCenter, double radsPerSec) {
        double setpointRotations = Units.radiansToRotations(radiansFromCenter) / Constants.HoodConstants.kHoodGearRatio;
        double setpointRotor = MathUtil.clamp(
                setpointRotations,
                Constants.HoodConstants.kHoodRotorMinPosition + Constants.HoodConstants.kHoodPositionTolerance,
                Constants.HoodConstants.kHoodRotorMaxPosition - Constants.HoodConstants.kHoodPositionTolerance);
        double rotationsPerSec = Units.radiansToRotations(radsPerSec) / Constants.HoodConstants.kHoodGearRatio;
        hoodMotor.setControl(positionControl.withPosition(setpointRotor).withVelocity(rotationsPerSec));
        Logger.recordOutput("Hood/IO/setPositionSetpoint/radiansFromCenter", radiansFromCenter);
        Logger.recordOutput("Hood/IO/setPositionSetpoint/setpointRotor", setpointRotor);
    }

    @Override
    public void setDutyCycleOut(double percentOutput) {
        hoodMotor.setControl(dutyCycleOut.withOutput(percentOutput));
    }

    @Override
    public void resetZeroPoint() {
        hoodMotor.setPosition(0.0);
    }
}
