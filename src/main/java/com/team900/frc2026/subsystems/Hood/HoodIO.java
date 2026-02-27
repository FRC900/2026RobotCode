package com.team900.frc2026.subsystems.Hood;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.team900.frc2026.Constants;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;

import org.littletonrobotics.junction.AutoLog;

import java.util.Arrays;
import java.util.List;

public interface HoodIO {
    // TalonFX motor
    public final TalonFX motor;

    // CANcoder for absolute hood position
    public final CANcoder canCoder;

    // Phoenix control requests
    public final DutyCycleOut dutyRequest = new DutyCycleOut(0.0);
    public final PositionVoltage positionRequest = new PositionVoltage(0.0);

    // Status signals
    public final StatusSignal<Double> position;
    public final StatusSignal<Double> velocity;
    public final StatusSignal<Double> appliedVolts;
    public final StatusSignal<Double> statorCurrent;
    public final StatusSignal<Double> supplyCurrent;
    public final StatusSignal<Double> absolutePosition;  // new CANcoder signal

    public static final double GEAR_RATIO = Constants.HoodConstants.kHoodGearRatio;

    // Constructor
    public HoodIOTalonFX(int motorID, int canCoderID) {
        motor = new TalonFX(motorID);
        canCoder = new CANcoder(canCoderID);

        position = motor.getPosition();
        velocity = motor.getVelocity();
        appliedVolts = motor.getMotorVoltage();
        statorCurrent = motor.getStatorCurrent();
        supplyCurrent = motor.getSupplyCurrent();
        absolutePosition = canCoder.getAbsolutePosition(); // CANcoder signal
    }

    @Override
    default List<BaseStatusSignal> getStatusSignals() {
        return Arrays.asList(
                position,
                velocity,
                appliedVolts,
                statorCurrent,
                supplyCurrent,
                absolutePosition  // include CANcoder
        );
    }

    @Override
    default void readInputs(HoodInputs inputs) {
        BaseStatusSignal.refreshAll(
                position,
                velocity,
                appliedVolts,
                statorCurrent,
                supplyCurrent,
                absolutePosition  // refresh CANcoder
        );

        double motorRot = position.getValue();
        double motorRPS = velocity.getValue();

        double hoodRot = motorRot / GEAR_RATIO;
        double hoodRad = hoodRot * 2.0 * Math.PI;
        double hoodRadPerSec = (motorRPS / GEAR_RATIO) * 2.0 * Math.PI;

        inputs.positionRotations = hoodRot;
        inputs.positionRad = hoodRad;
        inputs.velocityRadPerSec = hoodRadPerSec;
        inputs.appliedVolts = appliedVolts.getValue();
        inputs.currentStatorAmps = statorCurrent.getValue();
        inputs.currentSupplyAmps = supplyCurrent.getValue();

        // CANcoder absolute hood angle in radians
        double hoodAbsRot = absolutePosition.getValue();
        inputs.absolutePositionRad = hoodAbsRot * 2.0 * Math.PI;
    }

    @Override
    default void update(HoodInputs inputs) {
        readInputs(inputs);
    }

    @Override
    default void setNeutralMode(NeutralModeValue neutralMode) {
        motor.setNeutralMode(neutralMode);
    }

    @Override
    default void setPositionSetpoint(double radiansFromCenter, double radsPerSec) {
        double hoodRot = radiansFromCenter / (2.0 * Math.PI);
        double motorRot = hoodRot * GEAR_RATIO;

        double hoodRPS = radsPerSec / (2.0 * Math.PI);
        double motorRPS = hoodRPS * GEAR_RATIO;

        motor.setControl(
                positionRequest
                        .withPosition(motorRot)
                        .withVelocity(motorRPS)
        );
    }

    @Override
    default void setDutyCycleOut(double percentOutput) {
        motor.setControl(dutyRequest.withOutput(percentOutput));
    }

    @Override
    default void resetZeroPoint() {
        motor.setPosition(0.0);
    }
}
