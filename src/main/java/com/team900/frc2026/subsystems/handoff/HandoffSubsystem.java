package com.team900.frc2026.subsystems.handoff;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystem;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

/**
 * The {@code HandoffSubsystem} controls the roller mechanism of the robot's handoff. It manages
 * the speed and direction of the handoff rollers to feed game pieces to the shooter.
 */
public class HandoffSubsystem extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO> {
    private final RobotState state = RobotState.getInstance();

    public HandoffSubsystem(
            final ServoMotorSubsystemConfig motorConfig, final MotorIO motorIO) {
        super(motorConfig, new MotorInputsAutoLogged(), motorIO);
    }

    public double getPositionRotations() {
        return inputs.unitPosition;
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setHandoffRotations(getPositionRotations());
        state.setHandoffRPS(getCurrentVelocity());
    }
}
