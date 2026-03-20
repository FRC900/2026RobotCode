package com.team900.frc2026.subsystems.hood;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import com.team900.lib.subsystems.CanCoderInputsAutoLogged;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoder;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import com.team900.lib.subsystems.TalonFXIO;
import com.team900.lib.util.CurrentSpikeDetector;
import edu.wpi.first.math.MathUtil;

public class HoodSubsystem
        extends ServoMotorSubsystemWithCanCoder<
                MotorInputsAutoLogged, TalonFXIO, CanCoderInputsAutoLogged, CanCoderIO> {
    private final RobotState state = RobotState.getInstance();
    private TalonFXIO motorIO;

    private final CurrentSpikeDetector spikeDetector =
            new CurrentSpikeDetector(HoodConstants.kZeroingAmps, HoodConstants.kZeroingSeconds);

    public HoodSubsystem(
            ServoMotorSubsystemWithCanCoderConfig c, TalonFXIO motorIO, CanCoderIO cancoderIO) {
        super(c, new MotorInputsAutoLogged(), motorIO, new CanCoderInputsAutoLogged(), cancoderIO);
        this.positionSetpointUnits = HoodConstants.kHoodStowTrenchPositionRadians;
        this.motorIO = motorIO;

        // Update frequency for feedback.
        cancoderIO.updateFrequency(500);
    }

    // Updates robot state with current Hood angle
    @Override
    public void periodic() {
        super.periodic();
        state.setHoodRotations(inputs.unitPosition);
        state.setHoodRPS(inputs.velocityUnitsPerSecond);
        state.updateHoodHasZero(spikeDetector.update(inputs.currentStatorAmps));
    }

    public boolean isStowed() {
        // Returns true if Hood is in stowed position
        return MathUtil.isNear(
                HoodConstants.kHoodStowTrenchPositionRadians,
                getCurrentPosition(),
                HoodConstants.kHoodToleranceRadians);
    }

    public void setPositionRadians(double radians) {
        double safeSetpoint = constrainSetpoint(radians);
        motorIO.setPositionSetpoint(safeSetpoint, 0.0);
    }

    public void setPositionRadians(double radians, double velocityRadPerSec) {
        double safeSetpoint = constrainSetpoint(radians);
        motorIO.setPositionSetpoint(safeSetpoint, velocityRadPerSec);
        Logger.recordOutput(getName() + "/API/setPositionSetpointImp/Radians", radians);
        Logger.recordOutput(getName() + "/API/setPositionSetpointImp/SafeSetpoint", safeSetpoint);
        Logger.recordOutput(
                getName() + "/API/setPositionSetpointImp/velocityRadPerSec", velocityRadPerSec);
        Logger.recordOutput(
                getName() + "/API/setPositionSetpointImp/currentPosition", getCurrentPosition());
    }

    private double constrainSetpoint(double desiredRad) {
        double min = HoodConstants.kHoodRotorMinPosition;
        double max = HoodConstants.kHoodRotorMaxPosition;

        // Already in range
        if (desiredRad >= min && desiredRad <= max) {
            return desiredRad;
        }

        // If out of range, go to nearest limit
        return desiredRad < min ? min : max;
    }

    public void disableSoftLimits() {
        motorIO.setEnableSoftLimits(true, false);
    }

    public void enableSoftLimits() {
        motorIO.setEnableSoftLimits(true, true);
    }
}
