package com.team900.frc2026.subsystems.TopShooter;

import com.ctre.phoenix6.BaseStatusSignal;
import com.team254.lib.loops.IStatusSignalLoop;
import com.team254.lib.subsystems.MotorIO;
import com.team254.lib.subsystems.MotorInputsAutoLogged;
import com.team254.lib.subsystems.ServoMotorSubsystem;
import com.team254.lib.subsystems.ServoMotorSubsystemConfig;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowers;
import com.team254.lib.subsystems.ServoMotorSubsystemWithFollowersConfig;
import com.team254.lib.subsystems.SimTalonFXIO;
import com.team254.lib.subsystems.TalonFXIO;
import com.team254.lib.time.RobotTime;
import com.team900.frc2026.Constants.ShooterConstants;
// import com.team900.frc2026.subsystems.ShooterTop.ShooterBottomSensorInputsAutoLogged;
import com.team900.frc2026.RobotState;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class TopShooter extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO> implements IStatusSignalLoop{

    private final RobotState state;

    private static MotorInputsAutoLogged inputsTopMotorAutoLogged = new MotorInputsAutoLogged();



    private TalonFXIO topMotorIO;





    

    public TopShooter(
            ServoMotorSubsystemConfig leadConfig,
            TalonFXIO motorIOTop,
            RobotState state) {

        super(
                leadConfig,
                inputsTopMotorAutoLogged,
                motorIOTop);

        this.state = state;
        this.topMotorIO = motorIOTop;


    }

    @Override
    public void periodic() {
        super.periodic();
        double timestamp = RobotTime.getTimestampSeconds();

        Logger.processInputs("Top", inputsTopMotorAutoLogged);


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
        topMotorIO.setOpenLoopDutyCycle(
                dutyCycle * ShooterConstants.kTopRollerSpeedupFactor);

    }

    private void setVelocitySetpointImpl(double metersPerSecond) {
        
        topMotorIO.setVelocitySetpoint(
                metersPerSecond * ShooterConstants.kTopRollerSpeedupFactor);


    }

    public double getCurrentVelocity() {
        return (inputsTopMotorAutoLogged.velocityUnitsPerSecond
                                / ShooterConstants.kTopRollerSpeedupFactor);
    }



    @Override
    public List<BaseStatusSignal> getStatusSignals() {
        return new ArrayList<>();
    }

    public void resetSimState() {
    if (topMotorIO instanceof SimTalonFXIO) {
        ((SimTalonFXIO) topMotorIO).resetSimState();
    }

}

    @Override
    public void onLoop() {

        topMotorIO.readInputs(inputsTopMotorAutoLogged);
    }


}
