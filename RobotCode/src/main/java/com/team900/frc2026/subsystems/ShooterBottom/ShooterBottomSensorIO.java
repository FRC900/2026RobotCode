package com.team900.frc2026.subsystems.ShooterBottom;

import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.AutoLog;

public interface ShooterBottomSensorIO {

    @AutoLog
    public class ShooterBottomSensorInputs {

        public boolean shooterBannerHasPiece;
    }

    public default void readInputs(ShooterBottomSensorInputs inputs) {}

    DigitalInput getBanner();
}
