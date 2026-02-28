package com.team900.frc2026.subsystems.Hood;

import com.team254.lib.time.RobotTime;

import com.team900.frc2026.Constants;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;


public class HoodSubsystem extends SubsystemBase {
    private final HoodInputsAutoLogged inputs = new HoodInputsAutoLogged();
    private final HoodIO io;

    private double positionSetpointRad = 0.0;
    private double velocitySetpointRadPerSec = 0.0;

    public HoodSubsystem(final HoodIO io) {
        this.io = io;
    } 

    @Override
    public void periodic() {
        double timestamp = RobotTime.getTimestampSeconds();

        io.readInputs(inputs);
        Logger.processInputs("Hood", inputs);
        io.update(inputs);
    
        double clamped =
            Math.max(Constants.HoodConstants.kHoodMinPositionRadians,
            Math.min(Constants.HoodConstants.kHoodMaxPositionRadians, positionSetpointRad));
        
        positionSetpointRad = clamped;
        io.setPositionSetpoint(positionSetpointRad, velocitySetpointRadPerSec);

        Logger.recordOutput("Hood/positionRad", inputs.positionRad);
        Logger.recordOutput("Hood/latencyPeriodicSec", RobotTime.getTimestampSeconds() - timestamp);
    }

    public Command angleCommand(Supplier<Double> angleRadSupplier) {
    return run(() -> positionSetpointRad = angleRadSupplier.get())
            .withName("Hood Track Setpoint");
    }

    public Command angleDegreesCommand(Supplier<Double> angleDegSupplier) {
        return run(() ->
            positionSetpointRad = Units.degreesToRadians(angleDegSupplier.get())
        ).withName("Hood Track Setpoint (deg)");
    }

    public void resetZero() {
        io.resetZeroPoint();
        positionSetpointRad = 0.0;
    }

    public boolean atSetpoint() {
        return Math.abs(inputs.positionRad - positionSetpointRad) < Constants.HoodConstants.kHoodPositionTolerance;
    }

    public double getPositionRadians() {
        return inputs.positionRad;
    }

    public double getPositionDegrees() {
        return Math.toDegrees(inputs.positionRad);
    }

    public double getSetpointRadians() {
        return positionSetpointRad;
    }

    public double getSetpointDegrees() {
        return Math.toDegrees(positionSetpointRad);
    }
}
