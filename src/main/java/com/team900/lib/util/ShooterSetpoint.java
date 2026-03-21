package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
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
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/30_RPS/phi_model.json");
                thetaShootingModelClose =
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/30_RPS/theta_model.json");
                phiShootingModelFar =
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/34_RPS/phi_model.json");
                thetaShootingModelFar =
                        PolynomialModel.load(
                                "src/main/deploy/trajectories/34_RPS/theta_model.json");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load shooter polynomial models", e);
        }
    }

    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF, boolean isValid) {
        this.shooterRPS = shooterRPS;
        this.hoodRadians = hoodRadians;
        this.hoodFF = hoodFF;
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

    public static ShooterSetpoint setpointHub() {
        return makeShootingSetpoint(robotState.getLatestTranlastionRobotToHub());
    }

    private static ShooterSetpoint makeShootingSetpoint(Translation3d robotToTargetTranslation) {

        Pose2d robotPose = robotState.getLatestFieldToRobot().getValue();
        TurretAlignUtil aligner = new TurretAlignUtil(robotPose);
        Pose2d turretPose = aligner.getTurretPositionFromRobotPose();

        double turretX = turretPose.getX();
        double turretY = turretPose.getY();

        Translation2d hub =
                AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();

        // Distance from turret to hub
        double distanceToTarget = Math.hypot(hub.getX() - turretX, hub.getY() - turretY);


        boolean validSetpont = true;

        double shooterRPS;
        if (distanceToTarget < 2.24) {
            shooterRPS = ShooterConstants.kCloseShotRPS;
        } else {
            shooterRPS = ShooterConstants.kFarShotRPS;
        }

        double hoodSetpoint = getPhi(distanceToTarget, 0.0);

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPS, hoodSetpoint, 0.0, validSetpont);
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
