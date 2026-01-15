package com.team900.frc2026.subsystems.ShooterTop;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj.DigitalInput;

public interface ShooterTopSensorIO {

    @AutoLog

    public class ShooterTopSensorInputs {

        public boolean shooterBannerHasPiece;

    }

    public default void readInputs(ShooterTopSensorInputs inputs) {}


    DigitalInput getBanner();
}
