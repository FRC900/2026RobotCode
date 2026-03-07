package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
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

        //     var tangent = targetFrameToRobot.getY();
        //     var angular = robotSpeeds.omegaRadiansPerSecond;
        //     var distanceToTarget = robotToTargetXY.getNorm();
        //     var turretFF = -(angular + tangent / distanceToTarget);

        boolean validSetpont = true;
        double shooterRPS = ShooterConstants.kShootingRPS;

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPS, getPhi(distanceToTarget, 0.0), 0.0, validSetpont);
    }

    private static ShooterSetpoint makeShuttlingSetpoint(
            Translation3d robotToTargetTranslation, double launchSpeedMetersPerSec) {

        var robotToTargetXY =
                new Translation2d(robotToTargetTranslation.getX(), robotToTargetTranslation.getY());

        var distanceToTarget = robotToTargetXY.getNorm();

        // shooterRPS

        boolean validSetpont = true;
        double shooterRPS = ShooterConstants.kShootingRPS;

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPS, getPhi(distanceToTarget, 0), 0.0, validSetpont);
    }

    /**
     * Predicts launch angle phi (radians) given distance and forward robot velocity.
     *
     * @param r distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        return  1.3181648193362525
            - 3.8557030940e-02 * r
            + 6.5172357701e-02 * vf
            + 1.0370934929e-03 * r * r
            - 3.1174424260e-04 * r * vf
            + 4.6050836990e-04 * vf * vf
            - 4.4489041044e-05 * r * r * r
            - 6.0106609748e-05 * r * r * vf
            - 2.7244410203e-05 * r * vf * vf
            + 2.6662042308e-05 * vf * vf * vf
            - 2.4672194937e-06 * r * r * r * r
            + 2.3539741491e-05 * r * r * r * vf
            - 3.1134809372e-05 * r * r * vf * vf
            + 2.3985434850e-05 * r * vf * vf * vf
            - 7.2560025432e-06 * vf * vf * vf * vf;
    }

    /**
     * Predicts azimuthal angle theta (radians) given distance and lateral robot velocity.
     *
     * @param r distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        return  1.0672480382571276e-07
            + 1.0236363213e-06 * r
            - 6.7838471178e-02 * vl
            - 2.1658142815e-06 * r * r
            - 2.5259903973e-05 * r * vl
            + 2.8523417706e-08 * vl * vl
            + 7.5185841520e-07 * r * r * r
            + 1.6795875603e-05 * r * r * vl
            + 6.9714925311e-09 * r * vl * vl
            - 5.5151143377e-05 * vl * vl * vl
            - 7.0034801371e-08 * r * r * r * r
            - 2.4279336961e-06 * r * r * r * vl
            - 6.9442822955e-09 * r * r * vl * vl
            - 5.7258261252e-07 * r * vl * vl * vl
            + 1.8639516443e-09 * vl * vl * vl * vl;
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
