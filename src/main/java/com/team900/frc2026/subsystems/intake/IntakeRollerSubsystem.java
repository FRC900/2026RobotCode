package com.team900.frc2026.subsystems.intake;

import com.team900.lib.subsystems.*;
import com.team900.frc2026.RobotState;

public class IntakeRollerSubsystem extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO>{
    private final RobotState state;
    public MotorIO motorIO;

    public IntakeRollerSubsystem(final ServoMotorSubsystemConfig motorConfig, final MotorIO motorIO, RobotState state){
        super(motorConfig, new MotorInputsAutoLogged(), motorIO);
        this.state = state;
        this.motorIO = motorIO;
    }

    public double getPositionRotations(){
        return inputs.unitPosition;
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setIntakeRollerRotations(getPositionRotations());
        state.setIntakeRollerRPS(getCurrentVelocity());
    }
}