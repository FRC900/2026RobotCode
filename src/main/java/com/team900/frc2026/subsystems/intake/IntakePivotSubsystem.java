package com.team900.frc2026.subsystems.intake;

import com.team900.frc2026.RobotState;
import com.team900.lib.subsystems.CanCoderIO;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

                // Diagnostic values to help verify cancoder/motor sign and offsets on the real robot.
                // Watch these on SmartDashboard while pressing the intake toggle (L1) or running
                // the test command. They will help determine whether the sensor or motor sign
                // is flipped, or if the magnet offset needs adjustment.
                try {
                        SmartDashboard.putNumber("Intake/UnitPosition", inputs.unitPosition);
                        SmartDashboard.putNumber("Intake/RawRotorPosition", inputs.rawRotorPosition);
                        SmartDashboard.putNumber(
                                        "Intake/CanCoderAbsoluteRotations", cancoderInputs.absolutePositionRotations);
                        SmartDashboard.putBoolean("Intake/HasSetOffset", this.hasSetOffset);
                        SmartDashboard.putBoolean("Intake/IsFusedCancoder", this.conf.isFusedCancoder);
                } catch (Exception e) {
                        // Avoid throwing from periodic if SmartDashboard has issues on some platforms.
                }
    }
}
