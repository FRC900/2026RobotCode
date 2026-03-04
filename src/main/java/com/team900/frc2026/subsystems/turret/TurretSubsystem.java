package com.team900.frc2026.subsystems.turret;

import com.team900.lib.util.FullSubsystem;
import edu.wpi.first.math.MathUtil;
import org.littletonrobotics.junction.Logger;

public class TurretSubsystem extends FullSubsystem {
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
    }

    @Override
    public void periodicAfterScheduler() {
        if (isOpenLoop) {
            io.setOpenLoopDutyCycle(openLoopDutyCycle);
        } else {
            double safeSetpoint = constrainSetpoint(positionSetpointRad);
            io.setPositionSetpoint(safeSetpoint, velocitySetpointRadPerSec);
            Logger.recordOutput("Turret/requestedSetpointRad", positionSetpointRad);
            Logger.recordOutput("Turret/constrainedSetpointRad", safeSetpoint);
        }
    }

    //TODO: at some point check this to see if it works with 900 turret
    /**
     * Finds the best reachable angle for the turret target If the target is within limits, use it
     * directly Otherwise check if rotating 360 degrees in either direction gives an equivalent that
     * is in range And if no equivalent is in range go to the nearest limit
     */
    private double constrainSetpoint(double desiredRad) {
        double min = TurretConstants.kTurretSoftMinRadians;
        double max = TurretConstants.kTurretSoftMaxRadians;

        // Already in range
        if (desiredRad >= min && desiredRad <= max) {
            return desiredRad;
        }

        // Try adding/subtracting full rotations to find an equivalent angle in range
        double bestAngle = desiredRad;
        double bestDistance = Double.MAX_VALUE;

        // The turret range is at most ~2 full rotations, so checking ±1 rotation covers it
        for (int i = -2; i <= 2; i++) {
            double candidate = desiredRad + i * 2.0 * Math.PI;
            if (candidate >= min && candidate <= max) {
                double distance = Math.abs(candidate - fastInputs.positionRad);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestAngle = candidate;
                }
            }
        }

        // If we found a valid in-range spot, use it
        if (bestDistance < Double.MAX_VALUE) {
            return bestAngle;
        }

        // No equivalent is in range go to the nearest limit
        return MathUtil.clamp(desiredRad, min, max);
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

    public void setPositionDegrees(double degrees, double velocityDegPerSec) {
        isOpenLoop = false;
        positionSetpointRad = Math.toRadians(degrees);
        velocitySetpointRadPerSec = Math.toRadians(velocityDegPerSec);
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
