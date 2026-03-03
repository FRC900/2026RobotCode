package com.team900.frc2026.subsystems.superstructure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.RobotContainer;

import edu.wpi.first.wpilibj.Filesystem;
import java.util.Set;

import com.team900.frc2026.RobotContainer;

public class SuperstructureStateMachine {

    Map<SuperstructureState,List<StateTransition>> graph = new HashMap<>();
    private final Set<SuperstructureState> states = new HashSet<>();
    private final List<StateTransition> transitions = new ArrayList<>();
    private SuperstructureState currentState;
    private SuperstructureState desiredState;
    private SuperstructureState futureDesiredState;
    private final RobotContainer container;
    private boolean transitioning = false;
    private List<StateTransition>[][] precomputedPaths;
    private Map<String, Double> transitionCostMap = new HashMap<>();


    private String getTransitionKey(SuperstructureState from, SuperstructureState to) {
        return from.name() + "->" + to.name();
    }

    public double getTransitionCost(SuperstructureState from, SuperstructureState to) {
        return transitionCostMap.getOrDefault(getTransitionKey(from, to), 1.0);
    }

        private void loadTransitionCosts() {
        transitionCostMap.clear();
        File costFile = new File(Filesystem.getDeployDirectory(), "transition_costs.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(costFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String key = parts[0] + "->" + parts[1];
                    double duration = Double.parseDouble(parts[2]);
                    transitionCostMap.put(key, duration);
                    Logger.recordOutput("Using Transition Costs", true);
                }
            }
        } catch (IOException e) {
            Logger.recordOutput(
                    "Superstructure/Error", "Failed to load transition costs: " + e.getMessage());
            Logger.recordOutput("Using Transition Costs", false);
        }
    }


    public SuperstructureStateMachine(RobotContainer container) {
        this.currentState = null;
        this.desiredState = null;
        this.futureDesiredState = null;
        this.container = container;
        loadTransitionCosts();
        autoGenerateTransitions();
        precomputeAllPaths();
    }

    public void setTransitioning(boolean transitioning) {
        this.transitioning = transitioning;
    }

    public void addState(SuperstructureState state) {
        states.add(state);
    }

    public void addTransition(StateTransition transition) {
        states.add(transition.getFromState());
        states.add(transition.getToState());
        transitions.add(transition);
    }
    
    public SuperstructureState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SuperstructureState state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State not registered: " + state);
        }
        this.currentState = state;
    }

    public SuperstructureState getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(SuperstructureState state) {
        setDesiredState(state, true, true);
    }

    public void setDesiredState(SuperstructureState state, boolean setFuture, boolean wipeFuture){
        if (currentState == null && desiredState != null){
            if (setFuture) setFutureDesiredState(state);
            return;
        }
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State not registered: " + state);
        }

        
        desiredState = state;
    }

}
