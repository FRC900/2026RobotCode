package com.team900.frc2026.subsystems.ShooterBottom;

import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.BaseStatusSignal;
import com.team254.lib.loops.IStatusSignalLoop;
import com.team254.lib.subsystems.*;
import com.team254.lib.time.RobotTime;
import com.team900.frc2026.ShooterConstants;
import com.team900.frc2026.Constants;
import com.team900.frc2026.Robot;
import com.team900.frc2026.RobotState;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;

public class ShooterBottom extends ServoMotorSubsystem<MotorInputsAutoLogged, MotorIO>
        implements IStatusSignalLoop {

    private final RobotState robotState;
    private final ServoMotorSubsystemConfig leadconfig;

    private ShooterBottomSensorInputsAutoLogged inputsSensors =
            new ShooterBottomSensorInputsAutoLogged();

    private ShooterBottomSensorIO ioSensors;

    private AtomicBoolean ballEntered = new AtomicBoolean(false);
    private AtomicBoolean ballExited = new AtomicBoolean(false);
    private AtomicBoolean didStopShooter = new AtomicBoolean(false);
    private Debouncer bannerDebounce =
            new Debouncer(
                    Constants.SensorConstants.kShooterDebounceTime, Debouncer.DebounceType.kRising);

    public ShooterBottom(
            ServoMotorSubsystemConfig leadConfig,
            MotorIO leadIO,
            final ShooterBottomSensorIO sensorIO,
            RobotState state) {

        super(leadConfig, new MotorInputsAutoLogged(), leadIO);

        leadconfig = leadConfig;

        robotState = state;
        ioSensors = sensorIO;
    }

    @Override
    public void periodic() {
        super.periodic();
        double timestamp = RobotTime.getTimestampSeconds();

        Logger.processInputs("BottomShooter", inputsSensors);
    }

    public Command waitForCurrentSpike(double ampsToWaitFor) {
        return new WaitUntilCommand(() -> inputs.currentStatorAmps >= ampsToWaitFor);
    }

    public Command waitForCurrentDrop(double ampsToWaitFor) {
        return new WaitUntilCommand(() -> inputs.currentStatorAmps <= ampsToWaitFor);
    }

    public void setTeleopDefaultCommand() {
        setDefaultCommand(dutyCycleCommand(() -> 0.0).withName("Zero shooter RPS"));
    }

    public Command defaultCommand() {
        return dutyCycleCommand(() -> 0.0);
    }


    public Command velocitySetpointCommand(DoubleSupplier setpoint) {
        return runEnd(
                        () -> {
                            double vel = setpoint.getAsDouble();
                            Logger.recordOutput("velocitySetpointCommand/vel", vel);
                            setVelocitySetpointImpl(vel);
                        },
                        () -> {
                            setOpenLoopDutyCycleImpl(0.0);
                        })
                .withName(getName() + " Velocity Command");
    }



    private void setVelocitySetpointImpl(double unitsPerSecond) {
        
        ioSensors.setFlywheelSpeed(
                unitsPerSecond * ShooterConstants.kBottomRollerSpeedupFactor);
    }


    public Command runUntilBanner(DoubleSupplier velocitySupplier) {
        return Commands.runOnce(
                        () -> {
                            didStopShooter.set(false);
                            ballEntered.set(true);
                        })
                .andThen(
                        new ConditionalCommand(
                                        Commands.none(),
                                        this.velocitySetpointCommand(velocitySupplier)
                                                .until(didStopShooter::get),
                                        this::hasBall)
                                .finallyDo(
                                        () -> {
                                            ballEntered.set(false);
                                        }));

        // Run until banner sensor is triggered
    }

    public AngularVelocity getCurrentWheelSpeed() {
        return inputsSensors.wheelVelocity;
    }

    public boolean hasBall() {
        // If shooter has ball
        return bannerDebounce.calculate(ioSensors.getBanner().get());
    }

    @Override
    public void onLoop() {
        ioSensors.readInputs(inputsSensors);
    }

    public void resetSimState() {
    if (ioSensors instanceof ShooterBottomSensorIOSim) {
        ((ShooterBottomSensorIOSim) ioSensors).resetTalon();
    }
 
}

    @Override
    public List<BaseStatusSignal> getStatusSignals() {
        return new ArrayList<>();
    }
}
