package com.team900.frc2026.subsystems.superstructure;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Function;

import com.team900.frc2026.Constants;
import com.team900.frc2026.RobotContainer;
import com.team900.frc2026.subsystems.shooter.ShooterConstants;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public enum SuperstructureState {
    INTAKE_AND_SHOOT(
        //deploy intake and intake and it goes straight through and is scored or passed
    ),
    READYING_SHOOTER(
        //spins up shooter and exits once spun up to speed. Intake stowed
    ),
    READYING_SHOOTER_AND_INTAKE(
        //spins up shooter and exits once spun up to speed. Is intaking
    ),
    SHOOT(
        //empties all balls and scores or passes. Intake stowed
    ),
    INTAKE(
        //deploy and intakes
    ),
    IDLE_AND_DRIVING(
        //driving around. Still autoaims. Intake stowed
    );


    private final Function<RobotContainer, Command> commandSupplier;
    
    SuperstructureState(Function<RobotContainer, Command> commandSupplier) {
        this.commandSupplier = commandSupplier;
    }

    SuperstructureState() {
        this.commandSupplier = null;
    }

    
    public Command getCommand(RobotContainer container) {
        if (commandSupplier == null) {
            return Commands.none();
        }
        return commandSupplier.apply(container);
    }

    
    public Set<SuperstructureState> allowedNextStates() {
        Set<SuperstructureState> all = EnumSet.allOf(SuperstructureState.class);
        switch (this) {
            case INTAKE_AND_SHOOT:
                return EnumSet.of(READYING_SHOOTER);
            case READYING_SHOOTER:
                all.removeAll(EnumSet.of(INTAKE_AND_SHOOT,READYING_SHOOTER,READYING_SHOOTER_AND_INTAKE,SHOOT));
            case READYING_SHOOTER_AND_INTAKE:
                all.removeAll(EnumSet.of(INTAKE_AND_SHOOT,READYING_SHOOTER,READYING_SHOOTER_AND_INTAKE,SHOOT));
            case SHOOT:
                return EnumSet.of(READYING_SHOOTER,READYING_SHOOTER_AND_INTAKE,SHOOT);
            case INTAKE:
                return EnumSet.of(INTAKE_AND_SHOOT,SHOOT,INTAKE,IDLE_AND_DRIVING);
            case IDLE_AND_DRIVING:
                return EnumSet.of(INTAKE,INTAKE_AND_SHOOT,SHOOT,INTAKE,IDLE_AND_DRIVING);
            default:
                return all;
        }
    }




}
 