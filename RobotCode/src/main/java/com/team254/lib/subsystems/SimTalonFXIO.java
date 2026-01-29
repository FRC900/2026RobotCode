package com.team254.lib.subsystems;

import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.sim.ChassisReference;
import com.team254.lib.time.RobotTime;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class SimTalonFXIO extends TalonFXIO {
    protected DCMotorSim sim;
    private Notifier simNotifier = null;
    private double lastUpdateTimestamp = 0.0;
    private Optional<Double> overrideRPS = Optional.empty();
    private Optional<Double> overridePos = Optional.empty();

    // Used to handle mechanisms that wrap.
    private boolean invertVoltage = false;

    protected AtomicReference<Double> lastRotations = new AtomicReference<>((double) 0.0);
    protected AtomicReference<Double> lastRPS = new AtomicReference<>((double) 0.0);

    protected double getSimRatio() {
        return config.unitToRotorRatio;
    }

    public SimTalonFXIO(ServoMotorSubsystemConfig config) {
        this(
                config,
                new DCMotorSim(
                        LinearSystemId.createDCMotorSystem(
                                DCMotor.getKrakenX60Foc(1),
                                config.momentOfInertia,
                                1.0 / config.unitToRotorRatio),
                        DCMotor.getKrakenX60Foc(1),
                        0.001,
                        0.001));
    }

    public SimTalonFXIO(ServoMotorSubsystemConfig config, DCMotorSim sim) {
        super(config);
        this.sim = sim;
        talon.getSimState().Orientation =
                (config.fxConfig.MotorOutput.Inverted == InvertedValue.Clockwise_Positive)
                        ? ChassisReference.Clockwise_Positive
                        : ChassisReference.CounterClockwise_Positive;
        /* Run simulation at a faster rate so PID gains behave more reasonably */
        simNotifier =
                new Notifier(
                        () -> {
                            updateSimState();
                        });
        simNotifier.startPeriodic(0.005);

        this.lastUpdateTimestamp = RobotTime.getTimestampSeconds();
    }

    // Need to use rad of the mechanism itself.
    public void setPositionRad(double rad) {
        sim.setAngle(
                (config.fxConfig.MotorOutput.Inverted == InvertedValue.Clockwise_Positive
                                ? -1.0
                                : 1.0)
                        * rad);
        Logger.recordOutput(config.name + "/Sim/setPositionRad", rad);
    }

    @Override
    public void setVelocitySetpoint(double unitsPerSecond) {
        // Convert mechanism units/sec to rotor RPS
        double rotorRPS = unitsPerSecond * config.unitToRotorRatio;
        
        // Simple feedforward: estimate voltage needed
        // For flywheel: V = kV * velocity
        double kV = 12.0 / DCMotor.getKrakenX60Foc(1).freeSpeedRadPerSec; // volts per rad/s
        double targetRadPerSec = Units.rotationsToRadians(rotorRPS);
        double voltage = kV * targetRadPerSec;
        
        // Clamp to battery voltage
        voltage = Math.max(-12.0, Math.min(12.0, voltage));
        
        talon.setVoltage(addFriction(voltage, 0.25));
        
        Logger.recordOutput(config.name + "/Sim/VelocitySetpoint/TargetRPS", rotorRPS);
        Logger.recordOutput(config.name + "/Sim/VelocitySetpoint/CommandedVoltage", voltage);
    }

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

    public Supplier<SimCanCoderIO.SimCanCoderState> getSupplierForCancoder(
            ServoMotorSubsystemWithCanCoderConfig c) {
        return () -> {
            var a = new SimCanCoderIO.SimCanCoderState();
            a.positionRotations = lastRotations.get() * c.getCanCodertoRotorRatio();
            a.velocityRotations = lastRPS.get() * c.getCanCodertoRotorRatio();
            return a;
        };
    }

    public void setInvertVoltage(boolean invertVoltage) {
        this.invertVoltage = invertVoltage;
    }

    

    protected void updateSimState() {

        Logger.recordOutput(config.name + "/Sim/IsRunning", true);
        var simState = talon.getSimState();
        double rawMotorVoltage = simState.getMotorVoltage();
        Logger.recordOutput(config.name + "/Sim/SimulatorRawMotorVoltage", rawMotorVoltage);

        double simVoltage = addFriction(simState.getMotorVoltage(), 0.25);
        simVoltage = (invertVoltage) ? -simVoltage : simVoltage;
        sim.setInput(simVoltage);
        Logger.recordOutput(config.name + "/Sim/SimulatorVoltage", simVoltage);

        double timestamp = RobotTime.getTimestampSeconds();
        sim.update(timestamp - lastUpdateTimestamp);
        lastUpdateTimestamp = timestamp;

        overridePos.ifPresent(aDouble -> sim.setAngle(aDouble));

        // Find current state of sim in radians from 0 point
        double simPositionRads = sim.getAngularPositionRad();
        Logger.recordOutput(config.name + "/Sim/SimulatorPositionRadians", simPositionRads);

        // Mutate rotor position
        double rotorPosition = Units.radiansToRotations(simPositionRads) / getSimRatio();
        lastRotations.set(rotorPosition);
        simState.setRawRotorPosition(rotorPosition);
        Logger.recordOutput(config.name + "/Sim/setRawRotorPosition", rotorPosition);

        // Mutate rotor vel
        double rotorVel =
                Units.radiansToRotations(sim.getAngularVelocityRadPerSec()) / getSimRatio();
        lastRPS.set(rotorVel);
        simState.setRotorVelocity(overrideRPS.isEmpty() ? rotorVel : overrideRPS.get());
        Logger.recordOutput(
                config.name + "/Sim/SimulatorVelocityRadS", sim.getAngularVelocityRadPerSec());
    }

    public void overrideRPS(Optional<Double> rps) {
        overrideRPS = rps;
    }

    // This is the position in radians of the mechanism.
    public void overridePos(Optional<Double> pos) {
        overridePos = pos;
    }

    public double getIntendedRPS() {
        return lastRPS.get();
    }
}
