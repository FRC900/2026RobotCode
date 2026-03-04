// Copyright (c) 2025 FRC 1533
// http://github.com/triplestrange
//
// Use of this source code is governed by an MIT-style
// license that can be found in the LICENSE file at
// the root directory of this project.

package com.team900.frc2026.subsystems.drive;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.LinearAcceleration;
import java.util.Queue;

/** IO implementation for Pigeon 2. */
public class GyroIOPigeon2 implements GyroIO {
    private final Pigeon2 pigeon =
            new Pigeon2(
                    CompTunerConstants.DrivetrainConstants.Pigeon2Id,
                    new CANBus(CompTunerConstants.DrivetrainConstants.CANBusName));

    private final StatusSignal<Angle> yaw = pigeon.getYaw();
    private final StatusSignal<Angle> roll = pigeon.getRoll();
    private final StatusSignal<Angle> pitch = pigeon.getPitch();

    private final StatusSignal<LinearAcceleration> accelX = pigeon.getAccelerationX();
    private final StatusSignal<LinearAcceleration> accelY = pigeon.getAccelerationY();

    private final Queue<Double> yawPositionQueue;
    private final Queue<Double> yawVelocityQueue;

    private final Queue<Double> pitchPositionQueue;
    private final Queue<Double> pitchVelocityQueue;

    private final Queue<Double> rollPositionQueue;
    private final Queue<Double> rollVelocityQueue;

    private final Queue<Double> timestampQueue;

    private final Queue<Double> accelXQueue;
    private final Queue<Double> accelYQueue;

    private final StatusSignal<AngularVelocity> yawVelocity = pigeon.getAngularVelocityZWorld();
    private final StatusSignal<AngularVelocity> pitchVelocity = pigeon.getAngularVelocityXWorld();
    private final StatusSignal<AngularVelocity> rollVelocity = pigeon.getAngularVelocityYWorld();

    public GyroIOPigeon2() {
        pigeon.getConfigurator().apply(new Pigeon2Configuration());
        pigeon.getConfigurator().setYaw(0.0);

        yaw.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);
        pitch.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);
        roll.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);

        yawVelocity.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);
        pitchVelocity.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);
        rollVelocity.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);

        accelX.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);
        accelY.setUpdateFrequency(DriveConstants.ODOMETRY_FREQUENCY);

        pigeon.optimizeBusUtilization();

        timestampQueue = PhoenixOdometryThread.getInstance().makeTimestampQueue();

        yawPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(yaw);
        pitchPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(pitch);
        rollPositionQueue = PhoenixOdometryThread.getInstance().registerSignal(roll);

        yawVelocityQueue = PhoenixOdometryThread.getInstance().registerSignalVelocity(yawVelocity);
        pitchVelocityQueue =
                PhoenixOdometryThread.getInstance().registerSignalVelocity(pitchVelocity);
        rollVelocityQueue =
                PhoenixOdometryThread.getInstance().registerSignalVelocity(rollVelocity);

        accelXQueue =
                PhoenixOdometryThread.getInstance()
                        .registerSignalAcceleration(pigeon.getAccelerationX());
        accelYQueue =
                PhoenixOdometryThread.getInstance()
                        .registerSignalAcceleration(pigeon.getAccelerationY());
    }

    @Override
    public void readInputs(GyroIOInputs inputs) {
        inputs.connected =
                BaseStatusSignal.refreshAll(yaw, yawVelocity, accelX, accelY).equals(StatusCode.OK);

        inputs.yawVelocityRadPerSec = Units.degreesToRadians(yawVelocity.getValueAsDouble());

        inputs.yawPosition =
                Rotation2d.fromDegrees(
                        BaseStatusSignal.getLatencyCompensatedValueAsDouble(yaw, yawVelocity));
        inputs.rollPosition =
                Rotation2d.fromDegrees(
                        BaseStatusSignal.getLatencyCompensatedValueAsDouble(roll, rollVelocity));
        inputs.pitchPosition =
                Rotation2d.fromDegrees(
                        BaseStatusSignal.getLatencyCompensatedValueAsDouble(pitch, pitchVelocity));

        inputs.accelX = accelX.getValueAsDouble();
        inputs.accelY = accelY.getValueAsDouble();

        inputs.odometryTimestamps =
                timestampQueue.stream().mapToDouble((Double value) -> value).toArray();

        inputs.odometryYawPositions =
                yawPositionQueue.stream()
                        .map((Double value) -> Rotation2d.fromDegrees(value))
                        .toArray(Rotation2d[]::new);
        inputs.odometryRollPositions =
                rollPositionQueue.stream()
                        .map((Double value) -> Rotation2d.fromDegrees(value))
                        .toArray(Rotation2d[]::new);
        inputs.odometryPitchPositions =
                pitchPositionQueue.stream()
                        .map((Double value) -> Rotation2d.fromDegrees(value))
                        .toArray(Rotation2d[]::new);
        // rads per sec
        inputs.odometryYawVelocitys =
                yawVelocityQueue.stream().mapToDouble(Double::doubleValue).toArray();
        inputs.odometryRollVelocitys =
                rollVelocityQueue.stream().mapToDouble(Double::doubleValue).toArray();
        inputs.odometryPitchVelocitys =
                pitchVelocityQueue.stream().mapToDouble(Double::doubleValue).toArray();

        inputs.odometryAccelXs = accelXQueue.stream().mapToDouble(Double::doubleValue).toArray();
        inputs.odometryAccelYs = accelYQueue.stream().mapToDouble(Double::doubleValue).toArray();

        timestampQueue.clear();

        yawPositionQueue.clear();
        rollPositionQueue.clear();
        pitchPositionQueue.clear();

        yawVelocityQueue.clear();
        rollVelocityQueue.clear();
        pitchVelocityQueue.clear();

        accelXQueue.clear();
        accelYQueue.clear();
    }
}
