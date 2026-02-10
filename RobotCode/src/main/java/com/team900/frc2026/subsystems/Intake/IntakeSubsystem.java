package com.team900.frc2026.subsystems.Intake;

import com.team254.lib.subsystems.MotorIO;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import org.littletonrobotics.junction.Logger;

import com.team254.lib.time.RobotTime;
import com.team254.lib.subsystems.*;

public class IntakeSubsystem extends SubsystemBase{
    private final RobotState state;
    private final IntakeIO io;
    private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
    
    public IntakeSubsystem(final IntakeIO io) {
        this.io = io;
        this.state = RobotState.getInstance();
        setDefaultCommand(run(this::retract).withName("No Intake"));
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);
        Logger.processInputs("Intake", inputs);
    }

    public Command intake() {
        return run(() -> {
            wheelsIn(1.0);
            setIntake(1);
        }).withName("Intake intaking fuel");
    }

    public Command outtake() {
        return run(() -> {
            wheelsIn(-1.0);
            setIntake(1);
        }).withName("Intake spitting out fuel");
    }

    public Command retract() {
        return run(() -> {
            wheelsIn(0.0);
            setIntake(0);
        }).withName("Intake retracting");
    }

    public void wheelsIn(double speed) {
        intakeTalon.set(speed);
    }

    public void setIntake(int pos) {
        if(pos == 0){
            setExtended(false);
        } else {
            setExtended(true);
        }
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean Extended) {
        this.extended = Extended;
    }
}