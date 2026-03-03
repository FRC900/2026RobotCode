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
import java.util.Optional;
import java.util.Set;

import org.littletonrobotics.junction.Logger;

import com.team900.frc2026.RobotContainer;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;

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
    private double futureDesiredStateTime = 0;


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

    private void setFutureDesiredState(SuperstructureState state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State not registered: " + state);
        }
        futureDesiredState = state;
        futureDesiredStateTime = Timer.getFPGATimestamp();
    }

    public void wipeFutureDesiredState() {
        futureDesiredState = null;
    }

    public void autoGenerateTransitions() {
        for (SuperstructureState from : SuperstructureState.values()) {
            for (SuperstructureState to : from.allowedNextStates()) {
                double cost = getTransitionCost(from, to);
                addTransition(
                        new StateTransition(from, to, () -> to.getCommand(container), cost, false));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void precomputeAllPaths() {
        int numStates = SuperstructureState.values().length;
        precomputedPaths = new ArrayList[numStates][numStates];

        for (SuperstructureState from : SuperstructureState.values()) {
            for (SuperstructureState to : SuperstructureState.values()) {
                if (from.equals(to)) {
                    precomputedPaths[from.ordinal()][to.ordinal()] = new ArrayList<>();
                } else {
                    precomputedPaths[from.ordinal()][to.ordinal()] =
                            computeTransitionPath(from, to);
                }
            }
        }
    }


     public List<StateTransition> computeTransitionPath(
            SuperstructureState from, SuperstructureState to) {
        graph = new HashMap<>();
        for (SuperstructureState state : states) {
            graph.put(state, new ArrayList<>());
        }
        for (StateTransition t : transitions) {
            if (!t.hasCollision()) {
                graph.get(t.getFromState()).add(t);
            }
        }
        AStarSolver<SuperstructureState> solver = new AStarSolver<>();
        List<SuperstructureState> statePath =
                solver.solve(
                        from,
                        to,
                        (current, goal) -> current != null && current.equals(goal) ? 0 : 1,
                        state -> {
                            List<AStarSolver.Edge<SuperstructureState>> neighbors =
                                    new ArrayList<>();
                            for (StateTransition t : graph.get(state)) {
                                neighbors.add(
                                        new AStarSolver.Edge<>(
                                                t.getToState(), t.getTransitionTime()));
                            }
                            return neighbors;
                        });
        if (statePath == null) return null;
        List<StateTransition> transitionPath = new ArrayList<>();
        for (int i = 0; i < statePath.size() - 1; i++) {
            SuperstructureState currentFrom = statePath.get(i);
            SuperstructureState currentTo = statePath.get(i + 1);
            Optional<StateTransition> transition =
                    graph.get(currentFrom).stream()
                            .filter(t -> t.getToState().equals(currentTo))
                            .findFirst();
            if (transition.isPresent()) {
                transitionPath.add(transition.get());
            } else {
                throw new IllegalStateException(
                        "No transition found from " + currentFrom + " to " + currentTo);
            }
        }
        return transitionPath;
    }





}
