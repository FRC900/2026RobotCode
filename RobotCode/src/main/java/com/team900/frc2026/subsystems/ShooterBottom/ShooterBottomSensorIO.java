package com.team900.frc2026.subsystems.ShooterBottom;

import static edu.wpi.first.units.Units.Amps;
import static edu.wpi.first.units.Units.RotationsPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecondPerSecond;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.AutoLog;

public interface ShooterBottomSensorIO {

  

    @AutoLog
    public class ShooterBottomSensorInputs {

        public boolean bottomShooterBannerHasPiece = false;
        public AngularVelocity wheelVelocity = RotationsPerSecond.of(0);
        public AngularAcceleration wheelAcceleration = RotationsPerSecondPerSecond.of(0);
        public Current wheelAppliedCurrent = Amps.of(0);
        public Voltage wheelAppliedVoltage = Volts.of(0);
    }

    public default void readInputs(ShooterBottomSensorInputs inputs) {}

    public default void setFlywheelSpeed(AngularVelocity speed) {

    }
    

    DigitalInput getBanner();
}
