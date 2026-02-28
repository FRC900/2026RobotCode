package com.team900.frc2026.subsystems.spindexer;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystem;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

public class SpindexerSubsystem extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO> {

    private final RobotState state = RobotState.getInstance();

    public SpindexerSubsystem(final ServoMotorSubsystemConfig motorConfig, final MotorIO motorIO) {
        super(motorConfig, new MotorInputsAutoLogged(), motorIO);
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setSpindexerRotations(inputs.unitPosition);
        state.setSpindexerRPS(inputs.velocityUnitsPerSecond);
    }
}
