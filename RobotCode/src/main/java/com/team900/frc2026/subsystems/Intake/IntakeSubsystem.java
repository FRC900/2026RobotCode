package com.team900.frc2026.subsystems.Intake;

import com.team254.lib.subsystems.MotorIO;
import com.team900.frc2026.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.List;

import org.littletonrobotics.junction.Logger;

import com.team254.lib.time.RobotTime;
import com.team254.lib.subsystems.*;


//L and R motor for slapdown arm motors
public class IntakeSubsystem extends SubsystemBase{
    private final RobotState state;
    private final IntakeIO io;
    private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
    
    public IntakeSubsystem(final IntakeIO io) {
        this.io = io;
        this.state = RobotState.getInstance();
        setDefaultCommand(run(this::stopAll).withName("No Intake"));
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Intake", inputs);
    }

    public void stopAll() {
        io.stopIntake();
        io.stopLRoller();
        io.stopRRoller();
        io.stopLMotor();
        io.stopRMotor();
    }

    public void extendIntake(){
        io.setLMotorDutyCycleOut(0.67);
        io.setRMotorDutyCycleOut(0.67);
    }

    public void intakeFuel(){
        io.setIntakeDutyCycleOut(0.67);
        if(inputs.lCANrangeRange && inputs.rCANrangeRange){
            io.setLRollerDutyCycleOut(-0.67);
            io.setRRollerDutyCycleOut(-0.67);
        } else {
            io.setLRollerDutyCycleOut(-0.67);
            io.setRRollerDutyCycleOut(0.67);
        }
    }

    public Command intakeFuelCommand(){
        return run(this::intakeFuel).withName("Intaking Fuel");
    }

    public Command extendIntakeCommand() {
    return run(this::extendIntake).withName("Extending Intake");
}