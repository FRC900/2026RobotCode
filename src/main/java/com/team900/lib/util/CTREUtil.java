package com.team900.lib.util;

import static edu.wpi.first.units.Units.Seconds;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.*;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.S1CloseStateValue;
import com.ctre.phoenix6.signals.S1FloatStateValue;
import com.ctre.phoenix6.signals.S2CloseStateValue;
import com.ctre.phoenix6.signals.S2FloatStateValue;
import com.ctre.phoenix6.sim.CANcoderSimState;
import com.ctre.phoenix6.sim.ChassisReference;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import java.util.function.Supplier;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.motorsims.SimulatedBattery;
import org.ironmaple.simulation.motorsims.SimulatedMotorController;

public class CTREUtil {
    public static final int MAX_RETRIES = 10;

    public static StatusCode tryUntilOK(Supplier<StatusCode> function, int deviceId) {
        final int max_num_retries = 10;
        StatusCode statusCode = StatusCode.OK;
        for (int i = 0; i < max_num_retries; ++i) {
            statusCode = function.get();
            if (statusCode == StatusCode.OK) break;
        }
        if (statusCode != StatusCode.OK) {
            DriverStation.reportError(
                    "Error calling "
                            + function
                            + " on ctre device id "
                            + deviceId
                            + ": "
                            + statusCode,
                    true);
        }
        return statusCode;
    }

    public static StatusCode applyConfiguration(TalonFX motor, TalonFXConfiguration config) {
        return tryUntilOK(() -> motor.getConfigurator().apply(config), motor.getDeviceID());
    }

    public static StatusCode applyConfiguration(TalonFX motor, VoltageConfigs config) {
        return tryUntilOK(() -> motor.getConfigurator().apply(config), motor.getDeviceID());
    }

    public static StatusCode applyConfigurationNonBlocking(TalonFX motor, VoltageConfigs config) {
        return motor.getConfigurator().apply(config, 0.01);
    }

    public static StatusCode applyConfiguration(TalonFX motor, HardwareLimitSwitchConfigs config) {
        return tryUntilOK(() -> motor.getConfigurator().apply(config), motor.getDeviceID());
    }

    public static StatusCode applyConfiguration(TalonFX motor, MotionMagicConfigs config) {
        return tryUntilOK(() -> motor.getConfigurator().apply(config), motor.getDeviceID());
    }

    public static StatusCode applyConfiguration(TalonFX motor, CurrentLimitsConfigs config) {
        return tryUntilOK(() -> motor.getConfigurator().apply(config), motor.getDeviceID());
    }

    public static StatusCode applyConfiguration(CANcoder cancoder, CANcoderConfiguration config) {
        return tryUntilOK(() -> cancoder.getConfigurator().apply(config), cancoder.getDeviceID());
    }

    public static StatusCode refreshConfiguration(TalonFX motor, TalonFXConfiguration config) {
        return tryUntilOK(() -> motor.getConfigurator().refresh(config), motor.getDeviceID());
    }

    public static CANdiConfiguration createCandiConfiguration() {
        CANdiConfiguration candiConfiguration = new CANdiConfiguration();
        candiConfiguration.DigitalInputs.S1CloseState = S1CloseStateValue.CloseWhenNotHigh;
        candiConfiguration.DigitalInputs.S1FloatState = S1FloatStateValue.PullLow;
        candiConfiguration.DigitalInputs.S2CloseState = S2CloseStateValue.CloseWhenNotHigh;
        candiConfiguration.DigitalInputs.S2FloatState = S2FloatStateValue.PullLow;
        return candiConfiguration;
    }

    public static CANdiConfiguration createCustomCandiConfiguration(
            S1CloseStateValue s1CloseState,
            S1FloatStateValue s1FloatState,
            S2CloseStateValue s2CloseState,
            S2FloatStateValue s2FloatState) {
        CANdiConfiguration candiConfiguration = new CANdiConfiguration();
        candiConfiguration.DigitalInputs.S1CloseState = s1CloseState;
        candiConfiguration.DigitalInputs.S1FloatState = s1FloatState;
        candiConfiguration.DigitalInputs.S2CloseState = s2CloseState;
        candiConfiguration.DigitalInputs.S2FloatState = s2FloatState;
        return candiConfiguration;
    }

    public static class TalonFXMotorControllerSim implements SimulatedMotorController {
        private static int instances = 0;
        public final int id;

        private final TalonFXSimState talonFXSimState;

        public TalonFXMotorControllerSim(TalonFX talonFX, boolean motorInverted) {
            this.id = instances++;

            this.talonFXSimState = talonFX.getSimState();
            talonFXSimState.Orientation =
                    motorInverted
                            ? ChassisReference.Clockwise_Positive
                            : ChassisReference.CounterClockwise_Positive;
        }

        @Override
        public Voltage updateControlSignal(
                Angle mechanismAngle,
                AngularVelocity mechanismVelocity,
                Angle encoderAngle,
                AngularVelocity encoderVelocity) {
            talonFXSimState.setRawRotorPosition(encoderAngle);
            talonFXSimState.setRotorVelocity(encoderVelocity);
            talonFXSimState.setSupplyVoltage(SimulatedBattery.getBatteryVoltage());
            return talonFXSimState.getMotorVoltageMeasure();
        }
    }

    public static class TalonFXMotorControllerWithRemoteCancoderSim
            extends TalonFXMotorControllerSim {
        private final CANcoderSimState remoteCancoderSimState;
        private final Angle encoderOffset;

        public TalonFXMotorControllerWithRemoteCancoderSim(
                TalonFX talonFX,
                boolean motorInverted,
                CANcoder cancoder,
                boolean encoderInverted,
                Angle encoderOffset) {
            super(talonFX, motorInverted);
            this.remoteCancoderSimState = cancoder.getSimState();
            this.remoteCancoderSimState.Orientation =
                    encoderInverted
                            ? ChassisReference.Clockwise_Positive
                            : ChassisReference.CounterClockwise_Positive;
            this.encoderOffset = encoderOffset;
        }

        @Override
        public Voltage updateControlSignal(
                Angle mechanismAngle,
                AngularVelocity mechanismVelocity,
                Angle encoderAngle,
                AngularVelocity encoderVelocity) {
            remoteCancoderSimState.setRawPosition(mechanismAngle.minus(encoderOffset));
            remoteCancoderSimState.setVelocity(mechanismVelocity);

            return super.updateControlSignal(
                    mechanismAngle, mechanismVelocity, encoderAngle, encoderVelocity);
        }
    }

    public static double[] getSimulationOdometryTimeStamps() {
        final double[] odometryTimeStamps =
                new double[SimulatedArena.getSimulationSubTicksIn1Period()];
        for (int i = 0; i < odometryTimeStamps.length; i++) {
            odometryTimeStamps[i] =
                    Timer.getFPGATimestamp()
                            - 0.02
                            + i * SimulatedArena.getSimulationDt().in(Seconds);
        }

        return odometryTimeStamps;
    }
}
