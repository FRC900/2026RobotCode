package com.team900.frc2026;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import com.team900.lib.util.ConcurrentTimeInterpolatableBuffer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class RobotState {
    private static volatile RobotState instance;
    public static final double LOOKBACK_TIME = 1.0;

    private RobotState()    {


    }

    public void buildState() {

    }

      // Kinematic Frames
    // Robot's pose in field coordinates over time
    private final ConcurrentTimeInterpolatableBuffer<Pose2d> fieldToRobot =
            ConcurrentTimeInterpolatableBuffer.createBuffer(LOOKBACK_TIME);
    // Current robot-relative chassis speeds (measured from encoders)
    private final AtomicReference<ChassisSpeeds> measuredRobotRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Current field-relative chassis speeds (measured from encoders)
    private final AtomicReference<ChassisSpeeds> measuredFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Desired robot-relative chassis speeds (set by control systems)
    private final AtomicReference<ChassisSpeeds> desiredRobotRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    // Desired field-relative chassis speeds (set by control systems)
    private final AtomicReference<ChassisSpeeds> desiredFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());
    private final AtomicReference<ChassisSpeeds> fusedFieldRelativeChassisSpeeds =
            new AtomicReference<>(new ChassisSpeeds());

    private final AtomicInteger iteration = new AtomicInteger(0);

    private final ConcurrentTimeInterpolatableBuffer<Double> driveYawAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> driveRollAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> drivePitchAngularVelocity =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);

    private final ConcurrentTimeInterpolatableBuffer<Double> drivePitchRads =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> driveRollRads =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> accelX =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);
    private final ConcurrentTimeInterpolatableBuffer<Double> accelY =
            ConcurrentTimeInterpolatableBuffer.createDoubleBuffer(LOOKBACK_TIME);


     public void addDriveMotionMeasurements(
            double timestamp,
            double angularRollRadsPerS,
            double angularPitchRadsPerS,
            double angularYawRadsPerS,
            double pitchRads,
            double rollRads,
            double accelX,
            double accelY,
            ChassisSpeeds desiredRobotRelativeChassisSpeeds,
            ChassisSpeeds desiredFieldRelativeSpeeds,
            ChassisSpeeds measuredSpeeds,
            ChassisSpeeds measuredFieldRelativeSpeeds,
            ChassisSpeeds fusedFieldRelativeSpeeds) {
        this.driveRollAngularVelocity.addSample(timestamp, angularRollRadsPerS);
        this.drivePitchAngularVelocity.addSample(timestamp, angularPitchRadsPerS);
        this.driveYawAngularVelocity.addSample(timestamp, angularYawRadsPerS);
        this.drivePitchRads.addSample(timestamp, pitchRads);
        this.driveRollRads.addSample(timestamp, rollRads);
        this.accelY.addSample(timestamp, accelY);
        this.accelX.addSample(timestamp, accelX);
        this.desiredRobotRelativeChassisSpeeds.set(desiredRobotRelativeChassisSpeeds);
        this.desiredFieldRelativeChassisSpeeds.set(desiredFieldRelativeSpeeds);
        this.measuredRobotRelativeChassisSpeeds.set(measuredSpeeds);
        this.measuredFieldRelativeChassisSpeeds.set(measuredFieldRelativeSpeeds);
        this.fusedFieldRelativeChassisSpeeds.set(fusedFieldRelativeSpeeds);
    }

    
    public void incrementIterationCount() {
        iteration.incrementAndGet();
    }

    public static RobotState getInstance()  {
        if (instance == null)   {
            synchronized (RobotState.class) {
                if (instance == null)   {
                    instance = new RobotState();
                }
            }
        }
        return instance;
    }

}
