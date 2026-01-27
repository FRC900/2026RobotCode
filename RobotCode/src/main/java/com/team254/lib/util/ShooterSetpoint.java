package com.team254.lib.util;

import java.util.Optional;
import java.util.function.Supplier;

import com.team900.frc2026.Constants;
import com.team900.frc2026.RobotState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

public class ShooterSetpoint {

    public static Optional<Double> overrideRPS = Optional.empty();

    private double shooterRPS;
    private double shooterStage1RPS = 14.4;
    private double turretRadiansFromCenter;
    private double turretFF;
    private double hoodRadians;
    private double hoodFF;
    private boolean isValid;

    public ShooterSetpoint(
            double shooterRPS,
            double turretRadiansFromCenter,
            double turretFF,
            double hoodRadians,
            double hoodFF,
            boolean isValid) {
        this.shooterRPS = shooterRPS;
        this.turretRadiansFromCenter = turretRadiansFromCenter;
        this.turretFF = turretFF;
        this.hoodRadians = hoodRadians;
        this.hoodFF = hoodFF;
        this.isValid = isValid;
    }

    public ShooterSetpoint(
            double shooterRPS,
            double turretRadiansFromCenter,
            double turretFF,
            double hoodRadians,
            double hoodFF) {
        this.shooterRPS = shooterRPS;
        this.turretRadiansFromCenter = turretRadiansFromCenter;
        this.turretFF = turretFF;
        this.hoodRadians = hoodRadians;
        this.hoodFF = hoodFF;
        this.isValid = true;
    }

    public static void clearOverrideRPS() {
        overrideRPS = Optional.empty();
    }

    public static void setOverrideRPS(double rps) {
        overrideRPS = Optional.of(rps);
    }

    public boolean getIsValid() {
        return this.isValid;
    }

    // if (overrideRPS.isPresent()) {
    // launchSpeedRPS = overrideRPS.get();
    // }

    public double getShooterRPS() {
        return shooterRPS;
    }

    public double getShooterStage1RPS() {
        return shooterStage1RPS;
    }

    public double getTurretRadiansFromCenter() {
        return turretRadiansFromCenter;
    }

    public double getTurretFF() {
        return turretFF;
    }

    public double getHoodRadians() {
        return hoodRadians;
    }

    public double getHoodFF() {
        return hoodFF;
    }

    
   private static ShooterSetpoint fromSpeakerTarget(Translation3d target, RobotState robotState) {
        // turret
        Pose2d fieldToRobot;
        final boolean kUsePrediction = true;
        final double kPredictionLookaheadTime = 0.05;
        if (kUsePrediction) {
            fieldToRobot = robotState.getPredictedFieldToRobot(kPredictionLookaheadTime);
        } else {
            fieldToRobot = robotState.getLatestFieldToRobot().getValue();
        }
        Rotation2d robotToTargetRotation;
        Translation3d robotToTargetTranslation;
        double pitchAngleRads;

        var distanceToTarget = new Translation2d(
                target.getX() - fieldToRobot.getX(),
                target.getY() - fieldToRobot.getY()).getNorm();

        double launchSpeedRPS = 0.0;
        if (distanceToTarget < Constants.ShooterConstants.kShooterTopMaxShortRangeDistance) {
            launchSpeedRPS = Constants.ShooterConstants.kShooterTopRPSShortRange;
        } else if (distanceToTarget > Constants.ShooterConstants.kShooterTopMinLongRangeDistance) {
            launchSpeedRPS = Constants.ShooterConstants.kShooterTopRPSLongRange;
        } else {
            var x = (distanceToTarget - Constants.ShooterConstants.kShooterTopMaxShortRangeDistance) /
                    (Constants.ShooterConstants.kShooterTopMinLongRangeDistance
                            - Constants.ShooterConstants.kShooterTopMaxShortRangeDistance);
            launchSpeedRPS = Util.interpolate(Constants.ShooterConstants.kShooterTopRPSShortRange,
                    Constants.ShooterConstants.kShooterTopRPSLongRange, x);
        }

        // if (overrideRPS.isPresent()) {
        // launchSpeedRPS = overrideRPS.get();
        // }

        double launchSpeedMetersPerSec = Constants.ShooterConstants.kRingLaunchVelMetersPerSecPerRotPerSec *
                launchSpeedRPS;

        boolean kUseMotionCompensation = true;
        if (kUseMotionCompensation) {
            var vRobot = robotState.getLatestMeasuredFieldRelativeChassisSpeeds();
            var vShot = launchSpeedMetersPerSec;

            // Solve quadratic equation to obtain time of flight of ring.
            // a = vx^2+vy^2-shot_vel^2
            // b = -2*((tx-rx)*vx+(ty-ry)*vy)
            // c = (tx-rx)^2+(ty-ry)^2+dz^2
            var a = vRobot.vxMetersPerSecond * vRobot.vxMetersPerSecond +
                    vRobot.vyMetersPerSecond * vRobot.vyMetersPerSecond -
                    vShot * vShot;
            if (Math.abs(a) < Util.kEpsilon) {
                // Not a quadratic equation, cheat a little bit to make it one.
                vShot = 1.01 * vShot;
            }
            Translation3d d = target.minus(
                    new Translation3d(fieldToRobot.getX(), fieldToRobot.getY(), Constants.ShooterConstants.kBallReleaseHeight));
            var b = -2.0 * (d.getX() * vRobot.vxMetersPerSecond +
                    d.getY() * vRobot.vyMetersPerSecond);
            var c = d.getX() * d.getX() + d.getY() * d.getY() + d.getZ() * d.getZ();

            var discriminant = b * b - 4.0 * a * c;
            if (discriminant < 0.0) {
                discriminant = 0.0;
            }
            var t = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            var shot = new Translation3d((d.getX() - vRobot.vxMetersPerSecond * t) / t,
                    (d.getY() - vRobot.vyMetersPerSecond * t) / t,
                    (d.getZ() / t));
            robotToTargetRotation = new Rotation2d(shot.getX(), shot.getY());
            var xyVel = Math.sqrt(shot.getX() * shot.getX() + shot.getY() * shot.getY());
            pitchAngleRads = Math.atan2(shot.getZ(), xyVel);

            boolean kUseGravityCompensation = true;
            final double kG = -9.81;
            if (kUseGravityCompensation) {
                boolean kUseLiftCompensation = true;
                var drop = 0.5 * t * t * kG;
                if (kUseLiftCompensation) {
                    // But, v here is (distance / t), so v^2 * t^2 just becomes distance^2, which is
                    // c.
                    // That's neat, huh.
                    drop += 0.5 * Constants.ShooterConstants.kRingLaunchLiftCoeff * c;
                }
                pitchAngleRads = Math.atan2((d.getZ() - drop) / t, xyVel);
                vShot = Math.sqrt((d.getZ() - drop) * (d.getZ() - drop) / (t * t) + xyVel * xyVel);
            }
            launchSpeedMetersPerSec = vShot;
            robotToTargetTranslation = d;
        } else {
            robotToTargetRotation = new Rotation2d(
                    target.getX() - fieldToRobot.getX(),
                    target.getY() - fieldToRobot.getY());
            var differential_height = target.getZ() - Constants.ShooterConstants.kBallReleaseHeight;
            pitchAngleRads = Math.atan2(differential_height, distanceToTarget);
            robotToTargetTranslation = new Translation3d(
                    target.getX() - fieldToRobot.getX(),
                    target.getY() - fieldToRobot.getY(), differential_height);
        }
        return makeSetpoint(robotState, robotToTargetRotation, robotToTargetTranslation, pitchAngleRads,
                launchSpeedMetersPerSec);
    }

    
    private static ShooterSetpoint makeSetpoint(RobotState robotState, Rotation2d robotToTargetRotation,
            Translation3d robotToTargetTranslation,
            double pitchAngleRads, double launchSpeedMetersPerSec) {
        // turret
        Rotation2d turretRotationRobotFrame = robotToTargetRotation
                .minus(robotState.getLatestFieldToRobot().getValue().getRotation());
        Rotation2d turretRotationTurretFrame = turretRotationRobotFrame
                .rotateBy(MathHelpers.kRotation2dPi).rotateBy(Constants.ShooterConstants.kTurretToShotCorrection);

        // hood
        var hoodZeroedAngle = Rotation2d.fromDegrees(Constants.HoodConstants.kHoodZeroedAngleDegrees);
        double hoodAngle = hoodZeroedAngle.getRadians() - pitchAngleRads;

        // Feedfowards
        var robotSpeeds = robotState.getLatestMeasuredFieldRelativeChassisSpeeds();

        var robotToTargetXY = new Translation2d(robotToTargetTranslation.getX(), robotToTargetTranslation.getY());

        // In this frame, x = radial component (positive towards goal)
        // y = tangential component (positive means turret needs negative lead)
        var targetFrameToRobot = new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond)
                .rotateBy(
                        robotToTargetXY.getAngle());

        var tangent = targetFrameToRobot.getY();
        var angular = robotSpeeds.omegaRadiansPerSecond;
        var distanceToTarget = robotToTargetXY.getNorm();
        var turretFF = -(angular + tangent / distanceToTarget);
        // This is the deriative of atan2 accounting for the frame that the hood is
        // defined in.
        var hoodFF = targetFrameToRobot.getX() * -robotToTargetTranslation.getZ() /
                (distanceToTarget * distanceToTarget +
                        robotToTargetTranslation.getZ() * robotToTargetTranslation.getZ());

        boolean validSetpont = true;
        double shooterRPS = launchSpeedMetersPerSec / Constants.ShooterConstants.kRingLaunchVelMetersPerSecPerRotPerSec;
        if (shooterRPS > Constants.ShooterConstants.kShooterTopRPSCap) {
            shooterRPS = Constants.ShooterConstants.kShooterTopRPSCap;
            validSetpont = false;
        }

        return new ShooterSetpoint(shooterRPS,
                turretRotationTurretFrame.getRadians(),
                turretFF,
                hoodAngle,
                hoodFF, validSetpont);
    }


    public static Supplier<ShooterSetpoint> speakerSetpointSupplier(RobotState robotState) {
        return speakerSetpointSupplier(SpeakerTargetFactory.generate(robotState), robotState);
    }

    public static Supplier<ShooterSetpoint> speakerSetpointSupplier(Translation3d targetPoint,
            RobotState robotState) {
        return Util.memoizeByIteration(robotState.getIterationSupplier(),
                () -> fromSpeakerTarget(targetPoint, robotState));
    }
}
