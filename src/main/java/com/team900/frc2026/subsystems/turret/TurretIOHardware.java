package com.team900.frc2026.subsystems.turret;

import static edu.wpi.first.units.Units.Radians;
import static edu.wpi.first.units.Units.Rotation;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
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
    private final PositionTorqueCurrentFOC positionTorqueCurrentFOCControl =
            new PositionTorqueCurrentFOC(0.0).withSlot(0);

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
        // Convert from radians to rotor rotations for the TalonFX
        config.SoftwareLimitSwitch.ForwardSoftLimitThreshold =
                Units.radiansToRotations(TurretConstants.kTurretMaxPositionRadians)
                        / TurretConstants.kTurretGearRatio;
        config.SoftwareLimitSwitch.ReverseSoftLimitThreshold =
                Units.radiansToRotations(TurretConstants.kTurretMinPositionRadians)
                        / TurretConstants.kTurretGearRatio;

        if (Robot.isReal()) {
            config.CurrentLimits.StatorCurrentLimit = 150.0;
            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.ClosedLoopRamps = Constants.makeDefaultClosedLoopRampConfig();
            config.ClosedLoopRamps.TorqueClosedLoopRampPeriod = 0.01;
            config.OpenLoopRamps = Constants.makeDefaultOpenLoopRampConfig();
        }

        config.Slot0.kS = TurretConstants.COMP_GAINS.ffkS();
        config.Slot0.kP = TurretConstants.COMP_GAINS.kP();
        config.Slot0.kD = TurretConstants.COMP_GAINS.kD();
        config.Slot0.kV = TurretConstants.COMP_GAINS.ffkV();
        config.Slot0.kA = TurretConstants.COMP_GAINS.ffkA();
        // find motion magic values
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

    @Override
    public void readInputs(TurretInputs inputs) {
        BaseStatusSignal.refreshAll(
                voltsSignal, currentStatorSignal, currentSupplySignal, cancoder29AbsolutePosition);
        if (!cancoderOffset && cancoder33AbsolutePosition != null) {
            talon.setPosition(getTurretAngleOffset());
            cancoderOffset = true;
        }
        Logger.recordOutput("Turret/IO/cancoderOffset", cancoderOffset);
        inputs.cancoder33AbsolutePosition = cancoder33AbsolutePosition.getValueAsDouble();
        inputs.cancoder29AbsolutePosition = cancoder29AbsolutePosition.getValueAsDouble();
        inputs.appliedVolts = voltsSignal.getValueAsDouble();
        inputs.currentStatorAmps = currentStatorSignal.getValueAsDouble();
        inputs.currentSupplyAmps = currentSupplySignal.getValueAsDouble();

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
    public void setOpenLoopDutyCycle(double dutyCycle) {
        talon.setControl(dutyCycleControl.withOutput(dutyCycle));
        Logger.recordOutput("Turret/IO/setOpenLoopDutyCycle/dutyCycle", dutyCycle);
    }

    /**
     * Uses the CRT(Chinese Remainder Theorem) to use 2 CANCoder readings into a absolute turret pos
     *
     * <p>Each CANCoder reads rotations from 0-1 (it is fractional within one CANCoder revolution)
     *
     * <p>33:1 ratio - CANCoder wraps every 1/33rd of a turret rotation 29:1 ratio - CANCoder wraps
     * every 1/29rd of a turret rotation 29 and 33 are coprime, the combination of readings is
     * unique across 29*33 = 957 sectors.
     *
     * <p>CRT formula: x = (a1 * M1 * y1 + a2 * M2 * y2) mod M where a1, a2 are the remainders, M =
     * M1 * M2, and y1, y2 are the modular inverses.
     *
     * <p>Returns turret position in rotor rotations
     */
    private double getTurretAngleOffset() {
        BaseStatusSignal.waitForAll(10.0, cancoder33AbsolutePosition, cancoder29AbsolutePosition);

        int n1 = TurretConstants.kCRTRatio33; // 33
        int n2 = TurretConstants.kCRTRatio29; // 29

        // CANCoder, [0, 1), Convert to index of sector [0,n)
        double raw33 = cancoder33AbsolutePosition.getValueAsDouble();
        double raw29 = cancoder29AbsolutePosition.getValueAsDouble();

        // Wrap to [0,1) then convert to the index of the sector
        int a1 = (int) Math.round(((raw33 % 1.0) + 1.0) % 1.0 * n1) % n1;
        int a2 = (int) Math.round(((raw29 % 1.0) + 1.0) % 1.0 * n2) % n2;

        // CRT
        int M = n1 * n2; // 957
        int M1 = n2; // 29
        int M2 = n1; // 33
        int y1 = modInverse(M1, n1); // inverse of 29 mod 33
        int y2 = modInverse(M2, n2); // inverse of 33 mod 29

        int sector = ((a1 * M1 * y1 + a2 * M2 * y2) % M + M) % M;

        // Convert sector to turret rotations
        // Sector 0 = pos 0 (forward).
        // Each sector is 1/M of a turret rotation.
        double turretRotations = (double) sector / M;

        // Center around 0, so if turretRotations > 0.5, subtract 1 full rotation
        // so the range is [-0.5, 0.5) turret rotations instead of [0, 1)
        if (turretRotations > 0.5) {
            turretRotations -= 1.0;
        }

        Logger.recordOutput("Turret/CRT/raw33", raw33);
        Logger.recordOutput("Turret/CRT/raw29", raw29);
        Logger.recordOutput("Turret/CRT/sector", sector);
        Logger.recordOutput("Turret/CRT/turretRotations", turretRotations);

        // Return in rotor rotations (what the TalonFX encoder expects)
        return turretRotations / TurretConstants.kTurretGearRatio;
    }

    /** Computes the modular inverse of a mod m using the extended Euclidean algorithm. */
    private static int modInverse(int a, int m) {
        a = ((a % m) + m) % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        // Should never happen if a and m are coprime (29 and 33 are)
        throw new ArithmeticException("No modular inverse exists for " + a + " mod " + m);
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
        talon.setControl(
                positionTorqueCurrentFOCControl.withPosition(setpointRotor).withVelocity(ffVel));
        Logger.recordOutput("Turret/IO/setPositionSetpoint/radiansFromCenter", radiansFromCenter);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/radsPerSecond", radsPerSecond);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/ffVel", ffVel);
        Logger.recordOutput("Turret/IO/setPositionSetpoint/setpointRotor", setpointRotor);
        Logger.recordOutput(
                "Turret/IO/setPositionSetpoint/radsPerSecondRotor",
                radsPerSecond / TurretConstants.kTurretGearRatio);
    }
}
