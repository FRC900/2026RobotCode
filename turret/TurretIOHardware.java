package com.team900.frc2026.subsystems.turret;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Rotation;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.team900.frc2026.Constants;
import com.team900.frc2026.Robot;
import com.team900.lib.util.CTREUtil;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import java.util.Arrays;
import java.util.List;
import org.littletonrobotics.junction.Logger;

/**
 * The {@code TurretIOHardware} class implements the {@link TurretIO} interface and provides
 * hardware-level control and monitoring for the turret subsystem. It utilizes the TalonFX motor
 * controller and CANCoder sensors to manage turret positioning and movement.
 */
public class TurretIOHardware implements TurretIO {

    protected final TalonFX talon =
            new TalonFX(
                    TurretConstants.kTurretTalonCanID.getDeviceNumber(),
                    TurretConstants.kTurretTalonCanID.getBus());
    protected final CANcoder canCoder33To1 =
            new CANcoder(
                    TurretConstants.kTurret33To1CANCoder.getDeviceNumber(),
                    TurretConstants.kTurret33To1CANCoder.getBus());
    protected final CANcoder canCoder29To1 =
            new CANcoder(
                    TurretConstants.kTurret29To1CANCoder.getDeviceNumber(),
                    TurretConstants.kTurret29To1CANCoder.getBus());
    private final DutyCycleOut dutyCycleControl = new DutyCycleOut(0);
    private final PositionVoltage positionVoltageControl = new PositionVoltage(0.0).withSlot(0);
    private final StatusSignal<Angle> positionSignal = talon.getPosition();
    private final StatusSignal<AngularVelocity> velocitySignal = talon.getVelocity();
    private final StatusSignal<Voltage> voltsSignal = talon.getMotorVoltage();
    private final StatusSignal<Current> currentStatorSignal = talon.getStatorCurrent();
    private final StatusSignal<Current> currentSupplySignal = talon.getSupplyCurrent();
    private final StatusSignal<Angle> cancoder33AbsolutePosition = canCoder33To1.getPosition();
    private final StatusSignal<AngularVelocity> cancoder33Velocity = canCoder33To1.getVelocity();
    private boolean cancoderOffset = false;
    private final StatusSignal<Angle> cancoder29AbsolutePosition = canCoder29To1.getPosition();

    public TurretIOHardware() {

        var cancoderConfig = new CANcoderConfiguration();
        cancoderConfig.MagnetSensor.SensorDirection = SensorDirectionValue.Clockwise_Positive;
        cancoderConfig.MagnetSensor.MagnetOffset =
                -1.0 * TurretConstants.k33To1TurretCancoderOffset;
        CTREUtil.applyConfiguration(canCoder33To1, cancoderConfig);

        cancoderConfig = new CANcoderConfiguration();
        cancoderConfig.MagnetSensor.SensorDirection =
                SensorDirectionValue.CounterClockwise_Positive;
        cancoderConfig.MagnetSensor.MagnetOffset =
                -1.0 * TurretConstants.k29To1TurretCancoderOffset;
        CTREUtil.applyConfiguration(canCoder29To1, cancoderConfig);

        var config = new TalonFXConfiguration();
        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.SoftwareLimitSwitch.ForwardSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ReverseSoftLimitEnable = true;
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
                1.0 / TurretConstants.kTurretGearRatio;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
                -1.0 / TurretConstants.kTurretGearRatio;

        if (Robot.isReal()) {
            config.CurrentLimits.StatorCurrentLimit = 150.0;
            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.ClosedLoopRamps = Constants.makeDefaultClosedLoopRampConfig();
            config.ClosedLoopRamps.VoltageClosedLoopRampPeriod = 0.01;
            config.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        }

        config.Slot0.kS = 0.18;
        config.Slot0.kP = 6.0;
        config.Slot0.kD = 0.1;
        config.Slot0.kV = 0.120;
        config.Slot0.kA = 0.0001 * 12.0;

        config.MotionMagic.MotionMagicJerk = 0.0;
        config.MotionMagic.MotionMagicAcceleration = 900.0;
        config.MotionMagic.MotionMagicCruiseVelocity = 90.0;
        CTREUtil.applyConfiguration(talon, config);
        BaseStatusSignal.setUpdateFrequencyForAll(
                50,
                voltsSignal,
                currentStatorSignal,
                currentSupplySignal,
                cancoder29AbsolutePosition);
        BaseStatusSignal.setUpdateFrequencyForAll(
                250,
                cancoder33AbsolutePosition,
                cancoder33Velocity,
                positionSignal,
                velocitySignal);
        talon.optimizeBusUtilization();
    }

    public List<BaseStatusSignal> getStatusSignals() {
        // Only read position and velocity at 250 hz
        return Arrays.asList(
                positionSignal, velocitySignal, cancoder33AbsolutePosition, cancoder33Velocity);
    }

    public void readFastInputs(FastTurretInputs inputs) {
        double talonPosition =
                BaseStatusSignal.getLatencyCompensatedValue(positionSignal, velocitySignal)
                        .in(Radians);
        double kGearRatio = TurretConstants.kTurretGearRatio;
        inputs.positionRad = Units.rotationsToRadians(talonPosition * kGearRatio);
        inputs.velocityRadPerSec =
                Units.rotationsToRadians(velocitySignal.getValueAsDouble() * kGearRatio);
        inputs.turretPositionAbsolute =
                Rotation2d.fromRotations(
                        BaseStatusSignal.getLatencyCompensatedValue(
                                        cancoder33AbsolutePosition, cancoder33Velocity)
                                .in(Rotation));
    }

    @Override
    public void readInputs(TurretInputs inputs) {
        BaseStatusSignal.refreshAll(
                voltsSignal, currentStatorSignal, currentSupplySignal, cancoder29AbsolutePosition);
        if (!cancoderOffset && cancoder33AbsolutePosition != null) {
            talon.setPosition(getTurretAngleOffset());
            cancoderOffset = true;
        }
        Logger.recordOutput("Turret/IO/cancoderOffset", cancoderOffset);
        // TODO: check units for this john
        inputs.cancoder33AbsolutePosition = cancoder33AbsolutePosition.getValueAsDouble();
        inputs.cancoder29AbsolutePosition = cancoder29AbsolutePosition.getValueAsDouble();
        inputs.appliedVolts = voltsSignal.getValueAsDouble();
        inputs.currentStatorAmps = currentStatorSignal.getValueAsDouble();
        inputs.currentSupplyAmps = currentSupplySignal.getValueAsDouble();
    }

    @Override
    public void setOpenLoopDutyCycle(double dutyCycle) {
        talon.setControl(dutyCycleControl.withOutput(dutyCycle));
        Logger.recordOutput("Turret/IO/setOpenLoopDutyCycle/dutyCycle", dutyCycle);
    }

    private double getTurretAngleOffset() {
        BaseStatusSignal.waitForAll(10.0, cancoder33AbsolutePosition, cancoder29AbsolutePosition);
        double cancoder33To1 = cancoder33AbsolutePosition.getValueAsDouble();
        // Make cancoder3to1 read true value.
        double cancoder29To1 = cancoder29AbsolutePosition.getValueAsDouble() * (42.0 / 18.0);

        double offset = cancoder33To1 - cancoder29To1;
        while (Math.abs(offset) > 0.5) {
            if (offset > 0.5) {
                cancoder33To1 += 1.0;
            } else if (offset < -0.5) {
                cancoder33To1 -= 1.0;
            }
            offset = cancoder33To1 - cancoder29To1;
        }
        return cancoder33To1 / TurretConstants.kTurretGearRatio;
    }

    @Override
    public void setPositionSetpoint(double radiansFromCenter, double radsPerSecond) {
        double setpointRadians =
                MathUtil.clamp(
                        radiansFromCenter,
                        TurretConstants.kTurretMinPositionRadians,
                        TurretConstants.kTurretMaxPositionRadians);
        double setpointRotations = Units.radiansToRotations(setpointRadians);
        double setpointRotor = setpointRotations / TurretConstants.kTurretGearRatio;
        double ffVel = Units.radiansToRotations(radsPerSecond) / TurretConstants.kTurretGearRatio;
        talon.setControl(positionVoltageControl.withPosition(setpointRotor).withVelocity(ffVel));
        Logger.recordOutput("Turret/IO/setPositionSetpoint/radiansFromCenter", radiansFromCenter);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/radsPerSecond", radsPerSecond);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/ffVel", ffVel);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/setpointRotor", setpointRotor);
        Logger.recordOutput(
                "Turret/IO/setPositionSetpoint/radsPerSecondRotor",
                radsPerSecond / TurretConstants.kTurretGearRatio);
    }
}
