package com.team900.frc2026.subsystems.turret;

import com.ctre.phoenix6.BaseStatusSignal;
import com.team900.lib.util.MathHelpers;
import edu.wpi.first.math.geometry.Rotation2d;
import java.util.Arrays;
import java.util.List;
import org.littletonrobotics.junction.AutoLog;

/**
 * The {@code TurretIO} interface defines the methods and input structures required for controlling
 * the turret subsystem. It manages the reading of inputs such as turret position, velocity, and
 * electrical signals, as well as setting control modes like open-loop duty cycle and position-based
 * setpoints.
 *
 * <p>The {@code TurretIO} interface is designed for both real-time sensor data acquisition and
 * status signal management, providing a flexible mechanism to control and monitor the turret.
 */
public interface TurretIO {
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
        public double cancoder33AbsolutePosition = 0.0;
        public double cancoder29AbsolutePosition = 0.0;
    }

    default List<BaseStatusSignal> getStatusSignals() {
        return Arrays.asList();
    }
    ;

    // Read Inputs
    default void readInputs(TurretInputs inputs) {}
    ;

    default void readFastInputs(FastTurretInputs inputs) {}
    ;

    // Set open loop duty cycle
    default void setOpenLoopDutyCycle(double dutyCycle) {}

    default void setPositionSetpoint(double radiansFromCenter, double radsPerSecond) {}
}
