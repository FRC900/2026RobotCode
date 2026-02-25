package com.team900.frc2026.subsystems.ShooterTop;

import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.AutoLog;

public interface ShooterTopSensorIO {

    @AutoLog
    public class ShooterTopSensorInputs {

        public boolean shooterBannerHasPiece;
    }

    public default void readInputs(ShooterTopSensorInputs inputs) {}

    DigitalInput getBanner();
}
