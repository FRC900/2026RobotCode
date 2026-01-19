package com.team900.frc2026.simulation;

import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;




public class SimulatedRobotState {

        TimeInterpolatableBuffer<Pose2d> fieldToRobotSimulatedTruth =
            TimeInterpolatableBuffer.createBuffer(RobotState.LOOKBACK_TIME);

}
