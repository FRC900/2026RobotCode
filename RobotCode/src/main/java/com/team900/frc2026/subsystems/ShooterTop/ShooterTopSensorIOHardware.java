package com.team900.frc2026.subsystems.ShooterTop;

import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterTopSensorIOHardware implements ShooterTopSensorIO {

    protected final DigitalInput shooterBanner;

    public ShooterTopSensorIOHardware(int dioPort) {

        shooterBanner = new DigitalInput(dioPort);
    }

    @Override
    public void readInputs(ShooterTopSensorInputs inputs) {

        inputs.shooterBannerHasPiece = shooterBanner.get();
    }

    @Override
    public DigitalInput getBanner() {
        return shooterBanner;
    }
}
