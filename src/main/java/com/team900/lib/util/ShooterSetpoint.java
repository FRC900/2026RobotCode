package com.team900.lib.util;

import com.team900.frc2026.RobotState;
import com.team900.frc2026.subsystems.hood.HoodConstants;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.RobotBase;
import java.io.IOException;
import java.util.Optional;

public class ShooterSetpoint {

    private static final RobotState robotState = RobotState.getInstance();

    public static Optional<Double> overrideRPS = Optional.empty();

    private final double shooterRPS;
    private final double handoffRPS = 5000;
    private final double turretRadiansFromCenter;
    private final double turretFF;
    private final double hoodRadians;
    private final double hoodFF;
    private final boolean isValid;

    private static final PolynomialModel phiShootingModel;
    private static final PolynomialModel thetaShootingModel;

    static {
        try {
            String base = RobotBase.isReal() ? "/home/lvuser/deploy" : "src/main/deploy";
            phiShootingModel = PolynomialModel.load(base + "/shooting_models/phi_shooter_model.json");
            thetaShootingModel = PolynomialModel.load(base + "/shooting_models/theta_shooter_model.json");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shooter polynomial models", e);
        }
    }


public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF, double turretRadiansFromCenter, double turretFF, boolean isValid) {
    this.shooterRPS = shooterRPS;
    this.hoodRadians = hoodRadians;
    this.hoodFF = hoodFF;
    this.turretRadiansFromCenter = turretRadiansFromCenter;
    this.turretFF = turretFF;
    this.isValid = isValid;
}


    public ShooterSetpoint(double shooterRPS, double hoodRadians, double hoodFF, double turretRadiansFromCenter, double turretFF) {
        this(shooterRPS, hoodRadians, hoodFF, turretRadiansFromCenter, turretFF, true);
}

    public static void clearOverrideRPS() { overrideRPS = Optional.empty(); }
    public static void setOverrideRPS(double rps) { overrideRPS = Optional.of(rps); }
    public boolean getIsValid() { return isValid; }

  public static ShooterSetpoint setpointHub() {
    Translation2d hub = AllianceFlipUtil.apply(FieldConstants.Hub.topCenterPoint).toTranslation2d();
    Translation2d turretTranslation = robotState.getLatestFieldToTurret().getTranslation();
    double distance = hub.getDistance(turretTranslation);
    double turretRadiansFromCenter = hub.minus(turretTranslation).getAngle().getRadians();
    double hoodSetpoint = Math.PI - getPhi(distance, 0.0);
    return new ShooterSetpoint(ShooterConstants.kShootingRPS, hoodSetpoint, 0.0, turretRadiansFromCenter, 0.0);
}

    public static double getPhi(double r, double vf) { return phiShootingModel.evaluate(r, vf); }
    public static double getTheta(double r, double vl) { return thetaShootingModel.evaluate(r, vl); }

    public double getShooterRPS() { return shooterRPS; }
    public double getShooterStage1RPS() { return handoffRPS; }
    public double getTurretRadiansFromCenter() { return turretRadiansFromCenter; }
    public double getTurretFF() { return turretFF; }
    public double getHoodRadians() { return hoodRadians; }
    public double getHoodFF() { return hoodFF; }
}