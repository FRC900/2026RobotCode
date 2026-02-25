package com.team900.frc2026.subsystems.Led;

public interface LedIO{
    class LedInputs{}

    default void updateInputs(LedIO.LedInputs inputs) {}

    default void update(final LedIO.LedInputs inputs) {}

    LedState getCurrentState();

    default void writePixels(LedState states){}

    default void writePixels(LedState[] states){}
}
