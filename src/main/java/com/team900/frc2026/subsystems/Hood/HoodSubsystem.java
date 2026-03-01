package com.team900.frc2026.subsystems.hood;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import com.team900.lib.subsystems.CanCoderInputsAutoLogged;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoder;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import edu.wpi.first.math.MathUtil;

public class HoodSubsystem
        extends ServoMotorSubsystemWithCanCoder<
                MotorInputsAutoLogged, MotorIO, CanCoderInputsAutoLogged, CanCoderIO> {
    private final RobotState state = RobotState.getInstance();

    public HoodSubsystem(
            ServoMotorSubsystemWithCanCoderConfig c,
            MotorIO motorIO,
            CanCoderIO cancoderIO) {
        super(c, new MotorInputsAutoLogged(), motorIO, new CanCoderInputsAutoLogged(), cancoderIO);
        this.positionSetpointUnits = HoodConstants.kHoodStowTrenchPositionRadians;
        setDefaultCommand(
                motionMagicSetpointCommand(this::getPositionSetpointUnits)
                        .withName("Hood Maintain Setpoint (default)")
                        .ignoringDisable(true));

        // Update frequency for feedback.
        cancoderIO.updateFrequency(500);
    }

    // Updates robot state with current Hood angle
    @Override
    public void periodic() {
        super.periodic();
        state.setHoodRadians(inputs.unitPosition);
    }

    public boolean isStowed() {
        // Returns true if Hood is in stowed position
        return MathUtil.isNear(
                HoodConstants.kHoodStowTrenchPositionRadians,
                getCurrentPosition(),
                HoodConstants.kHoodToleranceRadians);
    }
}
