package com.team900.frc2026.simulation;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.drive.DriveConstants;
import com.team900.lib.time.RobotTime;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.wpilibj.Timer;
import lombok.Getter;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.littletonrobotics.junction.Logger;

public class SimulatedRobotState {
    TimeInterpolatableBuffer<Pose2d> fieldToRobotSimulatedTruth =
            TimeInterpolatableBuffer.createBuffer(RobotState.LOOKBACK_TIME);

    @Getter private SwerveDriveSimulation simDrive;

    private double contactStartTime = 0;
    private static final double REQUIRED_CONTACT_TIME = 0.01;
    private double lastTimestamp = 0.0;

    public SimulatedRobotState() {
        this.simDrive = new SwerveDriveSimulation(DriveConstants.mapleSimConfig, Pose2d.kZero);
    }

    // Do this after construction to avoid circular dependencies.
    public void init() {}

    public synchronized void addFieldToRobot(Pose2d pose) {
        fieldToRobotSimulatedTruth.addSample(RobotTime.getTimestampSeconds(), pose);
    }

    public synchronized Pose2d getLatestFieldToRobot() {
        var entry = fieldToRobotSimulatedTruth.getInternalBuffer().lastEntry();
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public synchronized void updateSim() {

        double delta = Timer.getFPGATimestamp() - lastTimestamp;

        var robotPose = simDrive.getSimulatedDriveTrainPose();

        // Get the positions of the fuel (both on the field and in the air)
        Pose3d[] fuelPoses = SimulatedArena.getInstance().getGamePiecesArrayByType("Fuel");
        // Publish to telemetry using AdvantageKit
        Logger.recordOutput("FieldSimulation/FuelPositions", fuelPoses);

        lastTimestamp = Timer.getFPGATimestamp();
    }
}
