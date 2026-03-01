package com.team900.frc2026.subsystems.turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.subsystems.turret.FastTurretInputsAutoLogged;
import com.team900.frc2026.subsystems.turret.TurretInputsAutoLogged;

public class TurretSubsystem extends SubsystemBase {
    private final TurretIO io;
    private final FastTurretInputsAutoLogged fastInputs = new FastTurretInputsAutoLogged();

    private final TurretInputsAutoLogged inputs = new TurretInputsAutoLogged();

    private double positionSetpointRad = 0.0;
    private double velocitySetpointRadPerSec = 0.0;
    private boolean isOpenLoop = false;
    private double openLoopDutyCycle = 0.0;

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
                Math.max(
                        TurretConstants.kTurretMinPositionRadians,
                        Math.min(TurretConstants.kTurretMaxPositionRadians, positionSetpointRad));

        if (isOpenLoop) {
            io.setOpenLoopDutyCycle(openLoopDutyCycle);
        } else {
            io.setPositionSetpoint(positionSetpointRad, velocitySetpointRadPerSec);
        }
    }

    public void setPositionRadians(double radians) {
        isOpenLoop = false;
        positionSetpointRad = radians;
        velocitySetpointRadPerSec = 0.0;
    }

    public void setPositionRadians(double radians, double velocityRadPerSec) {
        isOpenLoop = false;
        positionSetpointRad = radians;
        velocitySetpointRadPerSec = velocityRadPerSec;
    }

    public void setPositionDegrees(double degrees) {
        isOpenLoop = false;
        positionSetpointRad = Math.toRadians(degrees);
        velocitySetpointRadPerSec = 0.0;
    }

    public void setPositionDegrees(double degrees, double velocitySetpointDegPerSec) {
        isOpenLoop = false;
        positionSetpointRad = Math.toRadians(degrees);
        velocitySetpointRadPerSec = Math.toRadians(velocitySetpointDegPerSec);
    }

    public void setOpenLoop(double dutyCycle) {
        isOpenLoop = true;
        io.setOpenLoopDutyCycle(dutyCycle);
    }

    public void stop() {
        isOpenLoop = true;
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
        return Math.abs(fastInputs.positionRad - positionSetpointRad)
                < TurretConstants.toleranceRad;
    }
}
