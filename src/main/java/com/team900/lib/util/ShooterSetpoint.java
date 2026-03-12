package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
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

    private static final PolynomialModel phiShootingModel;
    private static final PolynomialModel thetaShootingModel;

    static {
        try {
            if (RobotBase.isReal()) {
                phiShootingModel =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/shooting_models/phi_shooter_model.json");
                thetaShootingModel =
                        PolynomialModel.load(
                                "/home/lvuser/deploy/shooting_models/theta_shooter_model.json");
            } else {
                phiShootingModel =
                        PolynomialModel.load(
                                "src/main/deploy/shooting_models/phi_shooter_model.json");
                thetaShootingModel =
                        PolynomialModel.load(
                                "src/main/deploy/shooting_models/theta_shooter_model.json");
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

        var robotToTargetXY =
                new Translation2d(robotToTargetTranslation.getX(), robotToTargetTranslation.getY());

        var distanceToTarget = robotToTargetXY.getNorm();

        // var tangent = targetFrameToRobot.getY();
        // var radial = targetFrameToRobot.getX();
        // var angular = robotSpeeds.omegaRadiansPerSecond;

        boolean validSetpont = true;
        double shooterRPS = ShooterConstants.kShootingRPS;

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(
                shooterRPS, Math.PI / 2. - getPhi(distanceToTarget, 0.0), 0.0, validSetpont);
    }

    /**
     * Predicts launch angle phi (radians) given distance and forward robot velocity.
     *
     * @param r distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        return phiShootingModel.evaluate(r, vf);
    }

    /**
     * Predicts azimuthal angle theta (radians) given distance and lateral robot velocity.
     *
     * @param r distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        return thetaShootingModel.evaluate(r, vl);
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
