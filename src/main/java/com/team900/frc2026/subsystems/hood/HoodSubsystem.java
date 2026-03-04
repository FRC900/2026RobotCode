package com.team900.frc2026.subsystems.hood;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import com.team900.lib.subsystems.CanCoderInputsAutoLogged;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoder;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import com.team900.lib.subsystems.TalonFXIO;

import edu.wpi.first.math.MathUtil;

public class HoodSubsystem
        extends ServoMotorSubsystemWithCanCoder<
                MotorInputsAutoLogged, TalonFXIO, CanCoderInputsAutoLogged, CanCoderIO> {
    private final RobotState state = RobotState.getInstance();
    private TalonFXIO motorIO;
    
        public HoodSubsystem(
                ServoMotorSubsystemWithCanCoderConfig c, TalonFXIO motorIO, CanCoderIO cancoderIO) {
            super(c, new MotorInputsAutoLogged(), motorIO, new CanCoderInputsAutoLogged(), cancoderIO);
            this.positionSetpointUnits = HoodConstants.kHoodStowTrenchPositionRadians;
            this.motorIO = motorIO;

        // Update frequency for feedback.
        cancoderIO.updateFrequency(500);
    }

    // Updates robot state with current Hood angle
    @Override
    public void periodic() {
        super.periodic();
        state.setHoodRotations(inputs.unitPosition);
        state.setHoodRPS(inputs.velocityUnitsPerSecond);
    }

    public boolean isStowed() {
        // Returns true if Hood is in stowed position
        return MathUtil.isNear(
                HoodConstants.kHoodStowTrenchPositionRadians,
                getCurrentPosition(),
                HoodConstants.kHoodToleranceRadians);
    }

    public void setPositionRadians(double radians){
        double safeSetpoint = constrainSetpoint(radians);
        motorIO.setPositionSetpoint(safeSetpoint,0.0);
    }

    public void setPositionRadians(double radians, double velocityRadPerSec){
        double safeSetpoint = constrainSetpoint(radians);
        motorIO.setPositionSetpoint(safeSetpoint, velocityRadPerSec);

    }




    private double constrainSetpoint(double desiredRad) {
        double min = HoodConstants.kHoodMinPositionRadians;
        double max = HoodConstants.kHoodMaxPositionRadians;

        // Already in range
        if (desiredRad >= min && desiredRad <= max) {
            return desiredRad;
        }

        // If out of range, go to nearest limit
        return desiredRad < min ? min : max;
    }
}
