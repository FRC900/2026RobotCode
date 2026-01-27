package com.team900.frc2026;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntSupplier;

import com.team254.lib.util.ConcurrentTimeInterpolatableBuffer;
import com.team254.lib.util.MathHelpers;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class RobotState {

    public static final double LOOKBACK_TIME = 1.0;
    
    private final AtomicInteger iteration = new AtomicInteger(0);

    public RobotState() {
            fieldToRobot.addSample(0.0, MathHelpers.kPose2dZero);
    }


    private final ConcurrentTimeInterpolatableBuffer<Pose2d> fieldToRobot = ConcurrentTimeInterpolatableBuffer
            .createBuffer(LOOKBACK_TIME);

    private final AtomicReference<ChassisSpeeds> measuredRobotRelativeChassisSpeeds = new AtomicReference<>(
            new ChassisSpeeds());

    private final AtomicReference<ChassisSpeeds> measuredFieldRelativeChassisSpeeds = new AtomicReference<>(
            new ChassisSpeeds());

    public void incrementIterationCount() {
        iteration.incrementAndGet();
    }

    public int getIteration() {
        return iteration.get();
    }

    public IntSupplier getIterationSupplier() {
        return () -> getIteration();
    }
    
    public Map.Entry<Double, Pose2d> getLatestFieldToRobot() {
        return fieldToRobot.getLatest();
    }



    public boolean isRedAlliance() {
        return DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().equals(Optional.of(Alliance.Red));
    }


    public ChassisSpeeds getLatestMeasuredFieldRelativeChassisSpeeds() {
        return measuredFieldRelativeChassisSpeeds.get();
    }

    public Pose2d getPredictedFieldToRobot(double lookaheadTimeS) {
        var maybeFieldToRobot = getLatestFieldToRobot();
        Pose2d fieldToRobot = maybeFieldToRobot == null ? MathHelpers.kPose2dZero : maybeFieldToRobot.getValue();
        var delta = getLatestRobotRelativeChassisSpeed();
        delta = delta.times(lookaheadTimeS);
        return fieldToRobot
                .exp(new Twist2d(delta.vxMetersPerSecond, delta.vyMetersPerSecond, delta.omegaRadiansPerSecond));
    }

    public ChassisSpeeds getLatestRobotRelativeChassisSpeed() {
        return measuredRobotRelativeChassisSpeeds.get();
    }

    public Pose2d getLatestFieldToRobotCenter() {
        return fieldToRobot.getLatest().getValue().transformBy(Constants.kTurretToRobotCenter);
    }
}
