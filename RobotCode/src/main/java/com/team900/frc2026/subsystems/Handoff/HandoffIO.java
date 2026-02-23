package com.team900.frc2026.subsystems.Handoff;

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

import com.team254.lib.subsystems.TalonFXIO;

public interface HandoffIO {

  

    @AutoLog
    public class HandoffSensorInputs {

        public boolean handoffBannerHasPiece = false;
        public AngularVelocity velocityUnitsPerSecond = RotationsPerSecond.of(0);

        public Current currentStatorAmps = Amps.of(0);
        public Current currentSupplyAmps = Amps.of(0);
        public Voltage appliedVolts = Volts.of(0);
    }

    public default void readInputs(HandoffSensorInputs inputs) {}

    public default void setFlywheelSpeed(double speed) {

    }

    public default TalonFXIO getTalon(){
        if (this instanceof HandoffSensorIOSim){
            return (((HandoffSensorIOSim) this).getTalon());
        }

        else {
            return (((HandoffSensorIOHardware) this).getTalon());
        }

    }

    


    

    DigitalInput getBanner();
}
