package com.team900.frc2026.subsystems.Intake;

import edu.wpi.first.wpilibj.DigitalInput;
import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {

    @AutoLog
    public class IntakeInputs {}

    default void updateInputs(IntakeIO.IntakeInputs inputs) {}

    @AutoLog
    class IntakeIOInputs{
        public boolean intakeConnected = false;
        public double intakeVelocityRotPerSec = 0.0;
        public double intakeAppliedVolts = 0.0;
        public double intakeSupplyAmps = 0.0;
        public double intakeStatorAmps = 0.0;
        public double intakeTempCelc = 0.0;

        public boolean rRollerConnected = false;
        public double rRollerVelocityRotPerSec = 0.0;
        public double rRollerAppliedVolts = 0.0;
        public double rRollerSupplyAmps = 0.0;
        public double rRollerStatorAmps = 0.0;
        public double rRollerTempCelc = 0.0;

        public boolean lRollerConnected = false;
        public double lRollerVelocityRotPerSec = 0.0;
        public double lRollerAppliedVolts = 0.0;
        public double lRollerSupplyAmps = 0.0;
        public double lRollerStatorAmps = 0.0;
        public double lRollerTempCelc = 0.0;

        public boolean fCANrangeConnected = false;
        public boolean fCANrangeRange = false;
    
        public boolean rCANrangeConnected = false;
        public boolean rCANrangeRange = false;

        public boolean lCANrangeConnected = false;
        public boolean lCANrangeRange = false;

        public boolean bCANrangeConnected = false;
        public boolean bCANrangeRange = false;
    }

    default void updateInputs(IntakeIOInputs inputs) {}
    default void setIntakeDutyCycleOut(double output) {}
    default void setIntakeBrakeMode(boolean enabled) {}
    default void stopIntake(int pos) {}

    default void setLRollerDutyCycleOut(double output) {}
    default void setLRollerBrakeMode(boolean enabled) {}
    default void stopLRoller() {}

    default void setRRollerDutyCycleOut(double output) {}
    default void setRRollerBrakeMode(boolean enabled) {}
    default void stopRRoller() {}

    
    default void setLMotorDutyCycleOut(double output) {}
    default void setLMotorBrakeMode(boolean enabled) {}
    default void stopLMotor() {}

    default void setRMotorDutyCycleOut(double output) {}
    default void setRMotorBrakeMode(boolean enabled) {}
    default void stopRMotor() {}
}
