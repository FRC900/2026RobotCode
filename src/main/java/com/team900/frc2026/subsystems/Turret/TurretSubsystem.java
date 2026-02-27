package com.team900.frc2026.subsystems.Turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.Constants;

public class TurretSubsystem extends SubsystemBase {
    private final TurretIO io;
    private final FastTurretInputsAutoLogged fastInputs =
            new FastTurretInputsAutoLogged();

    private final TurretInputsAutoLogged inputs =
            new TurretInputsAutoLogged();

    private double positionSetpointRad = 0.0;
    private double velocitySetpointRadPerSec = 0.0;
    
    public TurretSubsystem(final TurretIO io) {
        this.io = io;
    }

    @Override
    public void periodic() {
        io.readFastInputs(fastInputs);
        io.readInputs(inputs);

        Logger.processInputs("Turret/Fast", fastInputs);
        Logger.processInputs("Turret", inputs);

        positionSetpointRad =
            Math.max(Constants.TurretConstants.kTurretMinPositionRadians,
            Math.min(Constants.TurretConstants.kTurretMaxPositionRadians, positionSetpointRad));

        io.setPositionSetpoint(positionSetpointRad, velocitySetpointRadPerSec);
    }

    public void setPositionRadians(double radians) {
        positionSetpointRad = radians;
        velocitySetpointRadPerSec = 0.0;
    }

    public void setPositionRadians(double radians, double velocityRadPerSec) {
        positionSetpointRad = radians;
        velocitySetpointRadPerSec = velocityRadPerSec;
    }

    public void setPositionDegrees(double degrees) {
        positionSetpointRad = Math.toRadians(degrees);
        velocitySetpointRadPerSec = 0.0;
    }

    public void setPositionDegrees(double degrees, double velocitySetpointDegPerSec) {
        positionSetpointRad = Math.toRadians(degrees);
        velocitySetpointRadPerSec = Math.toRadians(velocitySetpointDegPerSec);
    }

    public void setOpenLoop(double dutyCycle) {
        io.setOpenLoopDutyCycle(dutyCycle);
    }

    public void stop() {
        io.setOpenLoopDutyCycle(0.0);
    }

    public double getPositionRadians() {
        return fastInputs.positionRad;
    }

    public double getVelocityRadPerSec() {
        return fastInputs.velocityRadPerSec;
    }

    public double getVelocityDegPerSec() {
        return Math.toDegrees(fastInputs.velocityRadPerSec);
    }

    public boolean atSetpoint() {
        return Math.abs(fastInputs.positionRad - positionSetpointRad) < Constants.TurretConstants.toleranceRad;
    }

}
