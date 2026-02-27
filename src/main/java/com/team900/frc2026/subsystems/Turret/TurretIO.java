package com.team900.frc2026.subsystems.Turret;

import com.ctre.phoenix6.BaseStatusSignal;
import com.team254.lib.util.MathHelpers;
import com.team900.frc2026.Constants;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.geometry.Rotation2d;

import org.littletonrobotics.junction.AutoLog;

import java.util.Arrays;
import java.util.List;

public interface TurretIO {
    public final TalonFX motor;
    public final CANcoder cancoder1;
    public final CANcoder cancoder2;

    public final DutyCycleOut dutyRequest = new DutyCycleOut(0.0);
    public final PositionVoltage positionRequest = new PositionVoltage(0.0);

    public final StatusSignal<Double> motorPosition;
    public final StatusSignal<Double> motorVelocity;
    public final StatusSignal<Double> motorVoltage;
    public final StatusSignal<Double> statorCurrent;
    public final StatusSignal<Double> supplyCurrent;
    public final StatusSignal<Double> cancoder1Abs;
    public final StatusSignal<Double> cancoder2Abs;

    public static final double GEAR_RATIO = Constants.TurretConstants.kTurretGearRatio;

    public TurretIOTalonFX(int motorID, int cancoder1ID, int cancoder2ID) {
        motor = new TalonFX(motorID);
        cancoder1 = new CANcoder(cancoder1ID);
        cancoder2 = new CANcoder(cancoder2ID);

        motorPosition = motor.getPosition();
        motorVelocity = motor.getVelocity();
        motorVoltage = motor.getMotorVoltage();
        statorCurrent = motor.getStatorCurrent();
        supplyCurrent = motor.getSupplyCurrent();

        cancoder1Abs = cancoder1.getAbsolutePosition();
        cancoder2Abs = cancoder2.getAbsolutePosition();
    }

    @AutoLog
    class FastTurretInputs {
        public Rotation2d turretPositionAbsolute = MathHelpers.kRotation2dZero;
        public double positionRad = 0.0;
        public double velocityRadPerSec = 0.0;
    }

    @AutoLog
    class TurretInputs {
        public double appliedVolts = 0.0;
        public double currentStatorAmps = 0.0;
        public double currentSupplyAmps = 0.0;
        public double cancoder1AbsolutePosition = 0.0;
        public double cancoder2AbsolutePosition = 0.0;
    }

    @Override
    default List<BaseStatusSignal> getStatusSignals() {
        return Arrays.asList(
            motorPosition,
            motorVelocity,
            motorVoltage,
            statorCurrent,
            supplyCurrent,
            cancoder1Abs,
            cancoder2Abs
        );
    };

    // Read Inputs
    @Override
    default void readInputs(TurretInputs inputs) {
        BaseStatusSignal.refreshAll(
            motorVoltage,
            statorCurrent,
            supplyCurrent,
            cancoder1Abs,
            cancoder2Abs
        );

        inputs.appliedVolts = motorVoltage.getValue();
        inputs.currentStatorAmps = statorCurrent.getValue();
        inputs.currentSupplyAmps = supplyCurrent.getValue();
        inputs.cancoder1AbsolutePosition = cancoder1Abs.getValue();
        inputs.cancoder2AbsolutePosition = cancoder2Abs.getValue();
    };

    default void readFastInputs(FastTurretInputs inputs) {
        BaseStatusSignal.refreshAll(
            motorPosition,
            motorVelocity
        );

        double motorRot = motorPosition.getValue();
        double motorRPS = motorVelocity.getValue();

        double turretRot = motorRot / GEAR_RATIO;
        double turretRad = turretRot * 2.0 * Math.PI;

        double turretRadPerSec = (motorRPS / GEAR_RATIO) * 2.0 * Math.PI;

        inputs.positionRad = turretRad;
        inputs.velocityRadPerSec = turretRadPerSec;
        inputs.turretPositionAbsolute = new Rotation2d(turretRad);
    };

    // Set open loop duty cycle
    default void setOpenLoopDutyCycle(double dutyCycle) {
        motor.setControl(dutyRequest.withOutput(dutyCycle));
    }

    default void setPositionSetpoint(double radiansFromCenter, double radsPerSecond) {
        double turretRot = radiansFromCenter / (2.0 * Math.PI);
        double motorRot = turretRot * GEAR_RATIO;

        double turretRPS = radsPerSecond / (2.0 * Math.PI);
        double motorRPS = turretRPS * GEAR_RATIO;

        motor.setControl(
            positionRequest
                .withPosition(motorRot)
                .withVelocity(motorRPS)
        );
    }
}
