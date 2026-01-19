package com.team900.frc2026.controlboard;

import java.util.Optional;
import java.util.function.Consumer;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public class ModalControls {
    private static Optional<ModalControls> instance = Optional.empty();

    public enum Mode {
        MODEONE,
        MODETWO // Placeholder modes since I don't really know what modes we want to roll with
    }

    private Mode currentMode = Mode.MODEONE;
    private Consumer<Mode> stateChangeconsumer;

    private Trigger triggerOne;
    private Trigger triggerTwo;

    public static ModalControls getInstance() {
        if (instance.isEmpty()){
            instance = Optional.of(new ModalControls());
        }
        return instance.get();
    } 


    public Mode getMode() {
        return currentMode;
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

    public void forceSetMode(Mode mode){
        maybeTriggerStateChangeConsumer(mode);
        setMode(mode);
        
    }
}
