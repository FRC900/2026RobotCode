package com.team900.frc2026.subsystems.intake;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import com.team900.lib.subsystems.CanCoderInputsAutoLogged;
import com.team900.lib.subsystems.MotorIO;
import com.team900.lib.subsystems.MotorInputsAutoLogged;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoder;
import com.team900.lib.subsystems.ServoMotorSubsystemWithCanCoderConfig;
import org.littletonrobotics.junction.Logger;

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
        this.setMotionMagicConfigCommand(IntakePivotConstants.kIntakePivotMotionMagicConfigs);
        setDefaultCommand(
                motionMagicSetpointCommand(this::getPositionSetpointUnits)
                        .withName("Intake Pivot Hold Setpoint")
                        .ignoringDisable(true));

        cancoderIO.updateFrequency(500);
    }

    @Override
    public void periodic() {
        super.periodic();
        state.setIntakePivotRotations(inputs.unitPosition);

        // Log the CANcoder absolute position converted to mechanism rotations
        // using the configured cancoderToUnitsRatio so 90° -> 0.25 in logs.
        Logger.recordOutput(
                "Intake Pivot/CanCoderAbsoluteRotations",
                cancoderInputs.absolutePositionRotations * this.conf.cancoderToUnitsRatio);

        double midpoint =
                (IntakePivotConstants.kIntakePivotStow + IntakePivotConstants.kIntakePivotDeploy)
                        / 2.0;
        boolean isDeployed = inputs.unitPosition < midpoint;

        Logger.recordOutput("Intake Pivot/IntakePivotIsDeployed", isDeployed);
    }

    public boolean isDeployed() {
        double midpoint =
                (IntakePivotConstants.kIntakePivotStow + IntakePivotConstants.kIntakePivotDeploy)
                        / 2.0;
        return inputs.unitPosition < midpoint;
    }

}
