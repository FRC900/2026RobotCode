package com.team900.frc2026.simulation;

import org.littletonrobotics.junction.Logger;

import com.team254.lib.time.RobotTime;
import com.team254.lib.util.LatchedBoolean;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class SimulatedRobotState {

    public enum BallState {
        INTAKE_AND_SHOOT,
        INTAKE,
        SHOOT, 
        CLIMB
    }

    private static final DoubleLogEntry shooterIsOn = new DoubleLogEntry(DataLogManager.getLog(), "shooterOn");

    TimeInterpolatableBuffer<Pose2d> fieldToRobotSimulatedTruth =
            TimeInterpolatableBuffer.createBuffer(RobotState.LOOKBACK_TIME);

    private BallState ballState = BallState.INTAKE;
    private LatchedBoolean shooterOn = new LatchedBoolean();
    private LatchedBoolean intakeOn = new LatchedBoolean();
    private LatchedBoolean climb = new LatchedBoolean();

    private RobotContainer container;

    public SimulatedRobotState(RobotContainer container) {
        this.container = container;
    }

    public synchronized void addFieldToRobot(Pose2d pose) {
        fieldToRobotSimulatedTruth.addSample(RobotTime.getTimestampSeconds(), pose);
    }

    public synchronized BallState getBallState() {
        return this.ballState;
    }

    public synchronized void setBallState(BallState state) {
        this.ballState = state;
    }

    public synchronized Pose2d getLatestFieldToRobot() {
        var entry = fieldToRobotSimulatedTruth.getInternalBuffer().lastEntry();
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public synchronized void updateSim() {

        boolean ShooterCurrentlyOn =
                shooterOn.update(container.getTopShooter().getCurrentVelocity() > 1.0);

        Logger.recordOutput("SimulatedRobotState/ShooterCurrentlyOn", ShooterCurrentlyOn);
        shooterIsOn.append(ShooterCurrentlyOn ? 1.0 : 0.0);
        
        switch (ballState) {
            case INTAKE_AND_SHOOT -> {
                intakeOn.update(true);
                shooterOn.update(true);
                // Intaking fuel and shooting fuel simultaneously
            }
            case INTAKE -> {
                intakeOn.update(true);
                // Intaking fuel
            }
            case SHOOT -> {
                shooterOn.update(true);
                // Shooting fuel
            }
            case CLIMB -> {
                climb.update(true);
                // Climbing
            }
        }
    }
}
