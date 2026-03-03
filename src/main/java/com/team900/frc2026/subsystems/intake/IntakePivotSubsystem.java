package com.team900.frc2026.subsystems.intake;

import com.team900.lib.subsystems.*;
import com.team900.lib.util.Util;
import com.team900.frc2026.subsystems.*;
import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class IntakePivotSubsystem extends ServoMotorSubsystemWithCanCoder<
                MotorInputsAutoLogged, MotorIO, CanCoderInputsAutoLogged, CanCoderIO>{
    private final RobotState state;
    private boolean deployed = false;

    public IntakePivotSubsystem(
            final ServoMotorSubsystemWithCanCoderConfig motorConfig,
            final MotorIO motorIO,
            final CanCoderIO canCoderIO,
            RobotState state) {
        super(motorConfig, new MotorInputsAutoLogged(), motorIO, new CanCoderInputsAutoLogged(), canCoderIO);
        this.setCurrentPosition(IntakeConstants.kIntakePivotStowPositionRadians);
        this.positionSetpointUnits = IntakeConstants.kIntakePivotStowPositionRadians;
        this.state = state;
    }

    public void setAutoDefaultCommand(){
        this.getDefaultCommand().cancel();
        this.setDefaultCommand(motionMagicSetpointCommand(() -> 
            IntakeConstants.kIntakePivotDeployPositionRadians)
        .withName("Deploy Intake In Auto")
        .ignoringDisable(true));
    }

    public void setTeleopDefaultCommand(){
        this.getDefaultCommand().cancel();
        this.setDefaultCommand(
            motionMagicSetpointCommand(this::getPositionSetpointUnits)
            .withName("Intake Pivot Maintain Setpoint")
            .ignoringDisable(true));
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setIntakePivotRadians(inputs.unitPosition);
        if(!deployed) {
            deployed = inputs.unitPosition >= IntakeConstants.kIntakePivotDeployPositionRadians
            || Util.epsilonEquals(IntakeConstants.kIntakePivotDeployPositionRadians,inputs.unitPosition,0.005);
        }

        if (this.positionSetpointUnits != IntakeConstants.kIntakePivotDeployPositionRadians) {
            deployed = false;
            Logger.recordOutput("IntakePivot/fullyDeployed",deployed);
        }
    }
}   

