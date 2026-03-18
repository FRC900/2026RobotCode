package com.team900.frc2026.subsystems.intake;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystem;
import com.team900.lib.subsystems.ServoMotorSubsystemConfig;

/**
 * The {@code IntakeRollerSubsystem} controls the roller mechanism of the robot's intake. It manages
 * the speed and direction of the intake rollers to collect and feed game pieces.
 */
public class IntakeRollerSubsystem extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO> {
    private final RobotState state = RobotState.getInstance();
    public MotorIO motorIO;

    public IntakeRollerSubsystem(
            final ServoMotorSubsystemConfig motorConfig, final MotorIO motorIO) {
        super(motorConfig, new MotorInputsAutoLogged(), motorIO);
        this.motorIO = motorIO;
    }

    public double getPositionRotations() {
        return inputs.unitPosition;
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setIntakeRollerRotations(getPositionRotations());
        state.setIntakeRollerRPS(getCurrentVelocity());
    }

    public boolean isStalled() {
        return inputs.velocityUnitsPerSecond < 5
                && inputs.currentStatorAmps > IntakeRollerConstants.stallCurrent;
    }
}
