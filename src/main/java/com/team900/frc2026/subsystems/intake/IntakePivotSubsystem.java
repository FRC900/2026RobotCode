package com.team900.frc2026.subsystems.intake;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import com.team900.lib.subsystems.CanCoderInputsAutoLogged;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoder;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;

public class IntakePivotSubsystem
        extends ServoMotorSubsystemWithCanCoder<
                MotorInputsAutoLogged, MotorIO, CanCoderInputsAutoLogged, CanCoderIO> {
    private final RobotState state = RobotState.getInstance();

    public IntakePivotSubsystem(
            ServoMotorSubsystemWithCanCoderConfig config, MotorIO motorIO, CanCoderIO cancoderIO) {
        super(
                config,
                new MotorInputsAutoLogged(),
                motorIO,
                new CanCoderInputsAutoLogged(),
                cancoderIO);

        setDefaultCommand(
                motionMagicSetpointCommand(this::getPositionSetpointUnits)
                        .withName("Intake Pivot Hold Setpoint")
                        .ignoringDisable(true));

        cancoderIO.updateFrequency(500);
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setIntakePivotRadians(inputs.unitPosition);
    }
}
