package com.team900.frc2026.subsystems.Hood;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.Timer;

public class HoodIOSim implements HoodIO {

    private static final double kArmLengthMeters = 0.25;
    private static final double kArmMassKg = 2.0;
    private static final double kGearRatio = 100.0;

    private final SingleJointedArmSim sim =
        new SingleJointedArmSim(
            DCMotor.getFalcon500(1),
            kGearRatio,
            SingleJointedArmSim.estimateMOI(kArmLengthMeters, kArmMassKg),
            kArmLengthMeters,
            -Math.PI,
            Math.PI,
            true,
            0.0
        );


    private double positionSetpointRad = 0.0;
    private double velocitySetpointRadPerSec = 0.0;

    private double lastTimestamp = Timer.getFPGATimestamp();

    @Override
    public void update(HoodInputs inputs) {
        double now = Timer.getFPGATimestamp();
        double dt = now - lastTimestamp;
        lastTimestamp = now;

        double error = positionSetpointRad - sim.getAngleRads();
        double kP = 8.0;

        double volts = kP * error;

        volts = Math.max(-12.0, Math.min(12.0, volts));

        sim.setInputVoltage(volts);
        sim.update(dt);

        inputs.positionRad = sim.getAngleRads();
        inputs.positionRotations = inputs.positionRad / (2.0 * Math.PI);
        inputs.velocityRadPerSec = sim.getVelocityRadPerSec();
        inputs.appliedVolts = volts;
        inputs.currentStatorAmps = Math.abs(sim.getCurrentDrawAmps());
        inputs.currentSupplyAmps = inputs.currentStatorAmps;
    }

    @Override
    public void setPositionSetpoint(double radiansFromCenter, double radsPerSec) {
        this.positionSetpointRad = radiansFromCenter;
        this.velocitySetpointRadPerSec = radsPerSec;
    }

    @Override
    public void setDutyCycleOut(double percentOutput) {
        sim.setInputVoltage(percentOutput * 12.0);
    }

    @Override
    public void resetZeroPoint() {
        sim.setState(0.0, 0.0);
    }
}
