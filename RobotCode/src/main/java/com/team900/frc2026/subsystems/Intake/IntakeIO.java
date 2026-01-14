public interface IntakeIO {

    @Autolog
    class IntakeIOInputs{

    }

    default void updateInputs(IntakeIOInputs inputs) {}

    default void runVolts(double volts) {}

    default void setPID(Gains gains) {}

    default void stop() {}

    defualt void zero() {}
}
