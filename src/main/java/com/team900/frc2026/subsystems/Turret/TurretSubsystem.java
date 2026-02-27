package com.team900.frc2026.subsystems.Turret;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.Constants;
import com.team900.lib.subsystems.ServoMotorSubsystem;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class TurretSubsystem extends ServoMotorSubsystem<TurretMotorInputsAutoLogged,TurretMotorIO> {
    private final FastTurretInputsAutoLogged fastInputs =
            new FastTurretInputsAutoLogged();

    private final TurretInputsAutoLogged inputs =
            new TurretInputsAutoLogged();

    private double positionSetpointRad = 0.0;
    private double velocitySetpointRadPerSec = 0.0;
    private boolean isOpenLoop = false;
    private double openLoopDutyCycle = 0.0;
    
    public TurretSubsystem(ServoMotorSubsystemConfig config, TurretMotorInputsAutoLogged inputs, TurretMotorIO io) {
        super(config, inputs, io);
    }

    @Override
    public void periodic() {
        super.periodic();

        io.readFastInputs(fastInputs);
        io.readInputs(inputs);

        Logger.processInputs("Turret/Fast", fastInputs);
        Logger.processInputs("Turret", inputs);

        positionSetpointRad =
            Math.max(Constants.TurretConstants.kTurretMinPositionRadians,
            Math.min(Constants.TurretConstants.kTurretMaxPositionRadians, positionSetpointRad));

        setPositionSetpointImpl(positionSetpointRad);
    }

    public void setPositionRadians(double radians) {
        positionSetpointRad = radians;
    }

    public void setPositionDegrees(double degrees) {
        positionSetpointRad = Math.toRadians(degrees);
    }

    public void setOpenLoop(double dutyCycle) {
        setOpenLoopDutyCycleImpl(dutyCycle);
    }

    public void stop() {
        setOpenLoopDutyCycleImpl(0.0);
    }

    public double getPositionRadians() {
        return getCurrentPosition();
    }

    public double getVelocityRadPerSec() {
        return getCurrentVelocity(); 
    }

    public double getVelocityDegPerSec() {
        return Math.toDegrees(getCurrentVelocity());
    }

    public boolean atSetpoint() {
        return Math.abs(getCurrentPosition() - positionSetpointRad) < Constants.TurretConstants.toleranceRad;
    }

}
