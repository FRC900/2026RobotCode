package com.team900.frc2026.controlboard;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import java.util.Optional;
import java.util.function.Consumer;

public class ModalControls {
    private static Optional<ModalControls> instance = Optional.empty();

    public enum Mode {
        INTAKE_AND_SHOOT,
        INTAKE,
        SHOOT,
        CLIMB
    }

    private Mode currentMode = Mode.INTAKE_AND_SHOOT;
    private Consumer<Mode> stateChangeconsumer;

    private Trigger shootTrigger;

    public static ModalControls getInstance() {
        if (instance.isEmpty()) {
            instance = Optional.of(new ModalControls());
        }
        return instance.get();
    }

    public Mode getMode() {
        return currentMode;
    }

    public void configureBindings() {
        shootTrigger = ControlBoard.getInstance().shoot();
    }

    public void setMode(Mode mode) {
        this.currentMode = mode;
    }

    private void maybeTriggerStateChangeConsumer(Mode newMode) {
        if (this.currentMode != newMode && this.stateChangeconsumer != null) {
            this.stateChangeconsumer.accept(newMode);
        }
    }

    private Trigger modeSpecific(Trigger trigger, Mode mode) {
        return trigger.and(new Trigger(() -> this.currentMode == mode));
    }

    public void forceSetMode(Mode mode) {
        maybeTriggerStateChangeConsumer(mode);
        setMode(mode);
    }

    public Trigger shoot() {
        return shootTrigger;
    }
}
