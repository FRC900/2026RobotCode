package com.team900.frc2026.subsystems.TopShooter;

import com.team254.lib.subsystems.MotorIO;
import com.team254.lib.subsystems.MotorInputsAutoLogged;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team254.lib.time.RobotTime;
import com.team900.frc2026.Constants;
// import com.team900.frc2026.subsystems.ShooterTop.ShooterBottomSensorInputsAutoLogged;
import com.team900.frc2026.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class TopShooter extends ServoMotorSubsystemWithFollowers<MotorInputsAutoLogged, MotorIO> {

    private final RobotState state;

    private static MotorInputsAutoLogged inputsTopMotor = new MotorInputsAutoLogged();
    private static MotorInputsAutoLogged[] inputsBottomMotor = {new MotorInputsAutoLogged()};

    private MotorIO topMotorIO;
    private MotorIO bottomMotorIO;

    public TopShooter(
            ServoMotorSubsystemWithFollowersConfig leadConfig,
            MotorIO motorIOTop,
            MotorIO[] motorIOBottom,
            RobotState state) {

        super(leadConfig, inputsTopMotor, motorIOTop, inputsBottomMotor, motorIOBottom);

        this.state = state;
        this.topMotorIO = motorIOTop;
        this.bottomMotorIO = motorIOBottom[0];
    }

    @Override
    public void periodic() {
        super.periodic();
        double timestamp = RobotTime.getTimestampSeconds();
        bottomMotorIO.readFollowerInputs(inputsBottomMotor);
        topMotorIO.readInputs(inputsTopMotor);
        Logger.processInputs(getName() + "/bottom", pickFirst(inputsBottomMotor));
        Logger.processInputs(getName() + "/top", inputsTopMotor);

        Logger.recordOutput(
                getName() + "/latencyPeriodicSec", RobotTime.getTimestampSeconds() - timestamp);
    }

    private static MotorInputsAutoLogged pickFirst(MotorInputsAutoLogged[] arr) {
        // Method so I can log inputs from follower motor
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return new MotorInputsAutoLogged();
        }
        return arr[0];
    }

    public void setTeleopDefaultCommand() {
        setDefaultCommand(dutyCycleCommand(() -> 0.0).withName("Zero shooter RPS"));
    }

    public Command defaultCommand() {
        return dutyCycleCommand(() -> 0.0).withName(getName() + " Default Command Neutral");
    }

    public Command dutyCycleCommand(DoubleSupplier setpoint) {
        return runEnd(
                        () -> {
                            setOpenLoopDutyCycleImpl(setpoint.getAsDouble());
                        },
                        () -> {
                            setOpenLoopDutyCycleImpl(0.0);
                        })
                .withName(getName() + " Duty Cycle Command");
    }

    public Command velocitySetpointCommand(DoubleSupplier setpoint) {
        return runEnd(
                        () -> {
                            double vel = setpoint.getAsDouble();
                            setVelocitySetpointImpl(vel);
                        },
                        () -> {
                            setOpenLoopDutyCycleImpl(0.0);
                        })
                .withName(getName() + " Velocity Command");
    }

    protected void setOpenLoopDutyCycleImpl(double dutyCycle) {
        Logger.recordOutput(
                getName() + "/top/API/setOpenLoopDutyCycle/dutyCycle",
                dutyCycle * Constants.ShooterConstants.kTopRollerSpeedupFactor);
        Logger.recordOutput(
                getName() + "/bottom/API/setOpenLoopDutyCycle/dutyCycle",
                dutyCycle * Constants.ShooterConstants.kBottomRollerSpeedupFactor);
        topMotorIO.setOpenLoopDutyCycle(
                dutyCycle * Constants.ShooterConstants.kTopRollerSpeedupFactor);
        bottomMotorIO.setOpenLoopDutyCycle(
                dutyCycle * Constants.ShooterConstants.kBottomRollerSpeedupFactor);
    }

    private void setVelocitySetpointImpl(double unitsPerSecond) {
        Logger.recordOutput(
                getName() + "/top/API/setVelocitySetpointImpl/UnitsPerS",
                unitsPerSecond * Constants.ShooterConstants.kTopRollerSpeedupFactor);
        Logger.recordOutput(
                getName() + "/bottom/API/setVelocitySetpointImpl/UnitsPerS",
                unitsPerSecond * Constants.ShooterConstants.kBottomRollerSpeedupFactor);
        topMotorIO.setVelocitySetpoint(
                unitsPerSecond * Constants.ShooterConstants.kTopRollerSpeedupFactor);
        bottomMotorIO.setVelocitySetpoint(
                unitsPerSecond * Constants.ShooterConstants.kBottomRollerSpeedupFactor);
    }

    public double getCurrentVelocity() {
        return ((pickFirst(inputsBottomMotor).velocityUnitsPerSecond
                                / Constants.ShooterConstants.kBottomRollerSpeedupFactor)
                        + (inputsTopMotor.velocityUnitsPerSecond
                                / Constants.ShooterConstants.kTopRollerSpeedupFactor))
                / 2.0;
    }
}
