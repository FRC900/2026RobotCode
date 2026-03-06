package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import java.util.Optional;

public class ShooterSetpoint {

    static RobotState robotState = RobotState.getInstance();

    public static Optional<Double> overrideRPS = Optional.empty();

    private double shooterRPM;
    private double handoffRPM = 5000;
    private double turretRadiansFromCenter;
    private double turretFF;
    private double hoodRadians;
    private double hoodFF;
    private boolean isValid;

    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF, boolean isValid) {
        this.shooterRPM = shooterRPS;
        this.hoodRadians = hoodRadians;
        this.hoodFF = hoodFF;
        this.isValid = isValid;
    }

    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF) {
        this.shooterRPM = shooterRPS;
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

        // shooterRPS

        boolean validSetpont = true;
        double shooterRPM = ShooterConstants.kShootingRPM;

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPM, getPhi(distanceToTarget, 0.0), 0.0, validSetpont);
    }

    private static ShooterSetpoint makeShuttlingSetpoint(
            Translation3d robotToTargetTranslation, double launchSpeedMetersPerSec) {

        var robotToTargetXY =
                new Translation2d(robotToTargetTranslation.getX(), robotToTargetTranslation.getY());

        var distanceToTarget = robotToTargetXY.getNorm();

        // shooterRPS

        boolean validSetpont = true;
        double shooterRPM = ShooterConstants.kShootingRPM;

        // values for hood are placeholders rn since that depends on the lookup table
        return new ShooterSetpoint(shooterRPM, getPhi(distanceToTarget, 0), 0.0, validSetpont);
    }

    /**
     * Predicts launch angle phi (radians) given distance and forward robot velocity.
     *
     * @param r distance to target (m)
     * @param vf forward robot velocity (m/s)
     * @return phi in radians
     */
    public static double getPhi(double r, double vf) {
        return 1.3319077394113694
                - 3.7917476957e-02 * r
                + 6.5559211155e-02 * vf
                + 9.1578107241e-04 * r * r
                - 3.3448392435e-04 * r * vf
                + 4.0252360850e-04 * vf * vf
                - 5.5561511125e-05 * r * r * r
                + 2.8140998785e-05 * r * r * vf
                - 8.0052327056e-05 * r * vf * vf
                + 3.8622151398e-05 * vf * vf * vf
                - 1.3708875933e-06 * r * r * r * r
                + 1.4983176335e-05 * r * r * r * vf
                - 2.3428958040e-05 * r * r * vf * vf
                + 1.8910264589e-05 * r * vf * vf * vf
                - 4.4641776123e-06 * vf * vf * vf * vf;
    }

    /**
     * Predicts azimuthal angle theta (radians) given distance and lateral robot velocity.
     *
     * @param r distance to target (m)
     * @param vl lateral robot velocity (m/s)
     * @return theta in radians
     */
    public static double getTheta(double r, double vl) {
        return -1.8722509645831825e-06
                - 7.3510622274e-06 * r
                - 6.7834581637e-02 * vl
                + 8.9871349979e-06 * r * r
                - 2.2214896752e-05 * r * vl
                + 4.8712473326e-07 * vl * vl
                - 2.6831898409e-06 * r * r * r
                + 1.4942965759e-05 * r * r * vl
                - 2.8428821571e-07 * r * vl * vl
                - 5.5109921945e-05 * vl * vl * vl
                + 2.3075944928e-07 * r * r * r * r
                - 2.4046620763e-06 * r * r * r * vl
                + 3.7418622874e-08 * r * r * vl * vl
                - 5.3380041757e-07 * r * vl * vl * vl
                - 2.2116720819e-09 * vl * vl * vl * vl;
    }

    public double getShooterRPM() {
        return shooterRPM;
    }

    public double getShooterStage1RPM() {
        return handoffRPM;
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
