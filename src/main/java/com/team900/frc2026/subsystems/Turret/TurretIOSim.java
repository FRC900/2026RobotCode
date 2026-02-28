package com.team900.frc2026.subsystems.Turret;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import org.littletonrobotics.junction.Logger;

public class TurretIOSim implements TurretIO {

    private double simulatedPositionRad = 0.0;
    private double simulatedVelocityRadPerSec = 0.0;
    private double appliedDutyCycle = 0.0;

    private final TurretInputs inputs = new TurretInputs();
    private final FastTurretInputs fastInputs = new FastTurretInputs();

    protected double addFriction(double motorVoltage, double frictionVoltage) {
        if (Math.abs(motorVoltage) < frictionVoltage) {
            motorVoltage = 0.0;
        } else if (motorVoltage > 0.0) {
            motorVoltage -= frictionVoltage;
        } else {
            motorVoltage += frictionVoltage;
        }
        return motorVoltage;
    }
    
    @Override
    public void readInputs(TurretInputs inputs) {
        double appliedVoltsWithoutFriction = appliedDutyCycle * 12.0;
        double appliedVoltsWithFriction = addFriction(appliedVoltsWithoutFriction, 0.25);
        inputs.appliedVolts = appliedVoltsWithFriction;

        inputs.currentStatorAmps = Math.abs(appliedDutyCycle) * 10.0;
        inputs.currentSupplyAmps = Math.abs(appliedDutyCycle) * 5.0;
        inputs.cancoder1AbsolutePosition = Units.radiansToRotations(simulatedPositionRad);
        inputs.cancoder2AbsolutePosition = Units.radiansToRotations(simulatedPositionRad);

        // Logging
        Logger.recordOutput("Turret/Sim/AppliedVolts", inputs.appliedVolts);
        Logger.recordOutput("Turret/Sim/CurrentStator", inputs.currentStatorAmps);
        Logger.recordOutput("Turret/Sim/PositionRotations", inputs.cancoder1AbsolutePosition);
    }

    @Override
    public void readFastInputs(FastTurretInputs inputs) {
        inputs.positionRad = simulatedPositionRad;
        inputs.velocityRadPerSec = simulatedVelocityRadPerSec;
        inputs.turretPositionAbsolute = Rotation2d.fromRadians(simulatedPositionRad);

        // Logging
        Logger.recordOutput("Turret/Sim/FastPositionRad", inputs.positionRad);
        Logger.recordOutput("Turret/Sim/FastVelocityRadPerSec", inputs.velocityRadPerSec);
    }

    @Override
    public void setOpenLoopDutyCycle(double dutyCycle) {
        appliedDutyCycle = dutyCycle;

        simulatedVelocityRadPerSec = dutyCycle * 5.0;
        simulatedPositionRad += simulatedVelocityRadPerSec * 0.02;

        Logger.recordOutput("Turret/Sim/SetDutyCycle", dutyCycle);
        Logger.recordOutput("Turret/Sim/SimulatedPositionRad", simulatedPositionRad);
        Logger.recordOutput("Turret/Sim/SimulatedVelocityRadPerSec", simulatedVelocityRadPerSec);
    }

    @Override
    public void setPositionSetpoint(double radiansFromCenter, double radsPerSecond) {
        simulatedPositionRad = radiansFromCenter;
        simulatedVelocityRadPerSec = radsPerSecond;

        Logger.recordOutput("Turret/Sim/SetPositionRad", radiansFromCenter);
        Logger.recordOutput("Turret/Sim/SetVelocityRadPerSec", radsPerSecond);
    }
}
