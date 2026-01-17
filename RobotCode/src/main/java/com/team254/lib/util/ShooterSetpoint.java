package com.team254.lib.util;

import java.util.Optional;

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
}
