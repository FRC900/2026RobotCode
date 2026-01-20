package com.team900.frc2026.simulation;

import com.team254.lib.time.RobotTime;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;




public class SimulatedRobotState {

    public enum BallState {
        STATE_ONE,
        STATE_TWO
    }

    TimeInterpolatableBuffer<Pose2d> fieldToRobotSimulatedTruth =
        TimeInterpolatableBuffer.createBuffer(RobotState.LOOKBACK_TIME);

    private BallState ballState = BallState.STATE_ONE;

    private RobotContainer container;

    public SimulatedRobotState(RobotContainer container){
        this.container = container;
    }

    synchronized public void addFieldToRobot(Pose2d pose){
        fieldToRobotSimulatedTruth.addSample(RobotTime.getTimestampSeconds(), pose);
    }

    synchronized public BallState getBallState() {
        return this.ballState;
    }

    synchronized public void setBallState(BallState state) {
        this.ballState = state;
    }

    synchronized public Pose2d getLatestFieldToRobot() {
        var entry = fieldToRobotSimulatedTruth.getInternalBuffer().lastEntry();
        if (entry == null) {
        return null;
        }
        return entry.getValue();
    }

    synchronized public void updateSim() {
        
    }


}
