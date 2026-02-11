package com.team900.frc2026.subsystems.Hood;

import com.team254.lib.subsystems.MotorIO;
import com.team254.lib.subsystems.MotorInputsAutoLogged;
import com.team254.lib.subsystems.ServoMotorSubsystem;
import com.team254.lib.time.RobotTime;

import com.team900.frc2026.Constants;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;


public class HoodSubsystem extends SubsystemBase {
    private final HoodInputsAutoLogged inputs = new HoodInputsAutoLogged();

    private final HoodIO io;

    private double hoodSetpointRadiansFromCenter = 0.0;

    public HoodSubsystem(final HoodIO io) {
        this.io = io;
    } 

    public void setTeleopDefaultCommand() {
        this.setDefaultCommand(run(() -> {
            setPositionSetpointImpl(hoodSetpointRadiansFromCenter, 0.0);
        }).withName("Hood Maintain Setpoint (default)"));
    }

    @Override
    public void periodic() {
        double timestamp = RobotTime.getTimestampSeconds();
        io.readInputs(inputs);
        Logger.processInputs("Hood", inputs);
        io.update(inputs);

        Logger.recordOutput("Hood/positionRad", inputs.positionRad);
        Logger.recordOutput("Hood/latencyPeriodicSec", RobotTime.getTimestampSeconds() - timestamp);
    }

    private void setPositionSetpointImpl(double radiansFromCenter, double radsPerSec) {
        io.setPositionSetpoint(radiansFromCenter, radsPerSec);
        hoodSetpointRadiansFromCenter = radiansFromCenter;
    }

    public Command angleCommand(Supplier<Double> angleRadSupplier) {
    return run(() -> setPositionSetpointImpl(angleRadSupplier.get(), 0.0))
            .withName("Hood Track Setpoint");
    }

    public void resetZero() {
        io.resetZeroPoint();
        hoodSetpointRadiansFromCenter = 0.0;
    }
}
