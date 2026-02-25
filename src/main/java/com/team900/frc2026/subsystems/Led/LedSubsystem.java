package com.team900.frc2026.subsystems.Led;

import com.team900.frc2026.RobotState;

import com.team900.lib.util.Util;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import java.util.function.Supplier;
import org.littletonrobotics.junction.Logger;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LedSubsystem extends SubsystemBase{
    private final LedIO io;
    private final RobotState state;

    public LedSubsystem(final LedIO io, final RobotState state){
        this.io = io;
        this.state = state;
    }

    @Override
    public void periodic() {
        super.periodic();

        state.setLedState(getCurrentState());
        Logger.recordOutput(
            "LED/currentCommand",
            (getCurrentCommand()==null) ? "Default" :getCurrentCommand().getName()
        );
    }

    public LedState getCurrentState() {
        return io.getCurrentState();
    }

    public Command commandSolidColor(LedState state){
        return run(()->setSolidColor(state)).ignoringDisable(true).withName("Led Solid Color");
    }

    public Command commandSolidColor(Supplier<LedState> state){
        return run(()->setSolidColor(state.get())).ignoringDisable(true).withName("Led Solid Color");
    }

    public Command commandSolidPattern(LedState[] state){
        return run(()->setSolidPattern(state)).ignoringDisable(true).withName("Led Solid Pattern");
    }

    public Command commandBlinkingState(LedState stateOne, LedState stateTwo, double durationOne, double durationTwo){
        return new SequentialCommandGroup(
            Commands.runOnce(()->setSolidColor(stateOne)),
            new WaitCommand(durationOne),
            Commands.runOnce(()->setSolidColor(stateTwo)),
            new WaitCommand(durationTwo))
            .repeatedly()
            .ignoringDisable(true)
            .withName("Blinking Led");
    }

    private void setSolidColor(LedState state) {
        io.writePixels(state);
    }

    private void setSolidPattern(LedState[] states) {
        io.writePixels(states);
    }

}
