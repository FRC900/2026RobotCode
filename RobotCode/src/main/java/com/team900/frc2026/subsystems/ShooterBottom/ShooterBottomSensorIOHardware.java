package com.team900.frc2026.subsystems.ShooterBottom;

import edu.wpi.first.wpilibj.DigitalInput;

public class ShooterBottomSensorIOHardware implements ShooterBottomSensorIO {

    protected final DigitalInput shooterBanner;

    public ShooterBottomSensorIOHardware(int dioPort) {

        shooterBanner = new DigitalInput(dioPort);

    }

    @Override
    public void readInputs(ShooterBottomSensorInputs inputs) {

        inputs.bottomShooterBannerHasPiece = shooterBanner.get();
    }

    @Override
    public DigitalInput getBanner() {
        return shooterBanner;
    }
}
