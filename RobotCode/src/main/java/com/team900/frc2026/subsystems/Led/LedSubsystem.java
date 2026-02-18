package com.team900.frc2026.subsystems.Led;

import com.team900.frc2026.RobotState;

public class LedSubsystem {
    private final LedIO io;
    private final Robotstate state;

    public LedSubsystem(final LedIO io, final RobotState state){
        this.io = io;
        this.state = state;
    }
}
