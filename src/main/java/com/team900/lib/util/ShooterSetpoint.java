package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import com.team900.frc2026.subsystems.turret.TurretConstants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.RobotBase;
import java.io.IOException;
import java.util.Optional;

public class ShooterSetpoint {

    static RobotState robotState = RobotState.getInstance();

    public static Optional<Double> overrideRPS = Optional.empty();

    private double shooterRPS;
    private double handoffRPS = 5000;
    private double turretRadiansFromCenter;
    private double turretFF;
    private double hoodRadians;
    private double hoodFF;
    private boolean isValid;

    private static final PolynomialModel phiShootingModelClose;
    private static final PolynomialModel thetaShootingModelClose;
    private static final PolynomialModel phiShootingModelFar;
    private static final PolynomialModel thetaShootingModelFar;

    static {
        try {
            if (RobotBase.isReal()) {
                phiShootingModelClose =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/trajectories/30_RPS/phi_model.json");
                thetaShootingModelClose =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/trajectories/30_RPS/theta_model.json");
                phiShootingModelFar =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/trajectories/34_RPS/phi_model.json");
                thetaShootingModelFar =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/trajectories/34_RPS/theta_model.json");
            } else {
                phiShootingModelClose =
                        PolynomialModel.load("src/main/deploy/trajectories/30_RPS/phi_model.json");
                thetaShootingModelClose =
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/30_RPS/theta_model.json");
                phiShootingModelFar =
                        PolynomialModel.load("src/main/deploy/trajectories/34_RPS/phi_model.json");
                thetaShootingModelFar =
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/34_RPS/theta_model.json");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load shooter polynomial models", e);
        }
    }

    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF, double turretRad, double turretFF, boolean isValid) {
        this.shooterRPS = shooterRPS;
        this.hoodRadians = hoodRadians;
        this.hoodFF = hoodFF;
        this.turretRadiansFromCenter = turretRad;
        this.turretFF = turretFF;
        this.isValid = isValid;
    }

    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF) {
        this.shooterRPS = shooterRPS;
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

    public static ShooterSetpoint makeShootingSetpoint() {

        Pose2d robotPose = robotState.getLatestFieldToRobot().getValue();
        TurretAlignUtil aligner = new TurretAlignUtil(robotPose);
        Pose2d turretPose = aligner.getTurretPositionFromRobotPose();

        double turretX = turretPose.getX();
        double turretY = turretPose.getY();

        Translation2d hub =
                AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();

        // Distance from turret to hub
        Translation2d distanceToTarget = new Translation2d(hub.getX() - turretX, hub.getY() - turretY);

        boolean validSetpont = true;

        double shooterRPS;
        if (distanceToTarget.getNorm() < 2.24) {
            shooterRPS = ShooterConstants.kCloseShotRPS;
        } else {
            shooterRPS = ShooterConstants.kFarShotRPS;
        }
        
         // Feedfowards
        var robotSpeeds = robotState.getLatestMeasuredFieldRelativeChassisSpeeds();

        var robotToTargetXY = new Translation2d(distanceToTarget.getX(), distanceToTarget.getY());

        // In this frame, x = radial component (positive towards goal)
        // y = tangential component (positive means turret needs negative lead)
        var targetFrameToRobot = new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond)
                .rotateBy(
                        robotToTargetXY.getAngle());

        var tangent = targetFrameToRobot.getY();
        var angular = robotSpeeds.omegaRadiansPerSecond;
        var turretFF = -(angular + tangent / distanceToTarget.getNorm());
        // This is the deriative of atan2 accounting for the frame that the hood is
        // defined in.
        
        ChassisSpeeds robotVelocity = RobotState.getInstance().getLatestFusedFieldRelativeChassisSpeed();
    double robotAngle = robotState.getLatestFieldToRobot().getValue().getRotation().getRadians();

        double turretVelocityX =
    robotVelocity.vxMetersPerSecond
        - robotVelocity.omegaRadiansPerSecond
            * (TurretConstants.turretOffSetFromCenterX * Math.sin(robotAngle)
                + TurretConstants.turretOffSetFromCenterY * Math.cos(robotAngle));

double turretVelocityY =
    robotVelocity.vyMetersPerSecond
        + robotVelocity.omegaRadiansPerSecond
            * (TurretConstants.turretOffSetFromCenterX * Math.cos(robotAngle)
                - TurretConstants.turretOffSetFromCenterY * Math.sin(robotAngle));

        double hoodSetpoint = getPhi(distanceToTarget.getNorm(), turretVelocityY);
        // TODO: prettu sure i can get rid of the math.pi but lets see 
                double angleRad = Math.atan2(hub.getY() - turretY, hub.getX() - turretX) + Math.PI + getTheta(distanceToTarget.getNorm(), turretVelocityX);


        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPS, hoodSetpoint, 0.0, angleRad, turretFF, validSetpont);
    }

    /**
     * Predicts launch angle phi (radians) given distance and forward robot velocity.
     *
     * @param r distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        if (r < 2.24) {
            return phiShootingModelClose.evaluate(r, vf);
        } else {
            return phiShootingModelFar.evaluate(r, vf);
        }
    }

    /**
     * Predicts azimuthal angle theta (radians) given distance and lateral robot velocity.
     *
     * @param r distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        if (r < 2.24) {
            return thetaShootingModelClose.evaluate(r, vl);
        } else {
            return thetaShootingModelFar.evaluate(r, vl);
        }
    }

    public double getShooterRPS() {
        return shooterRPS;
    }

    public double getShooterStage1RPS() {
        return handoffRPS;
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
}
