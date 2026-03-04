package com.team900.frc2026.subsystems.superstructure;

import com.team900.frc2026.RobotContainer;
import com.team900.lib.commands.ChezySequenceCommandGroup;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.littletonrobotics.junction.Logger;

public class SuperstructureStateMachine {

    Map<SuperstructureState, List<StateTransition>> graph = new HashMap<>();
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
    public Command buildCharacterizationCommand() {
        Command overallSequence = Commands.none();
        desiredState = SuperstructureState.IDLE_AND_DRIVING;
        File costFile = new File(Filesystem.getDeployDirectory(), "transition_costs.txt");
        for (StateTransition transition : transitions) {
            final double[] startTime = new double[1];
            Command transitionSequence =
                    Commands.sequence(
                                    new InstantCommand(
                                            () -> {
                                                currentState = transition.getFromState();
                                                desiredState = transition.getFromState();
                                                Logger.recordOutput(
                                                        "Latest Characterization State",
                                                        transition.getFromState()
                                                                + " to "
                                                                + transition.getToState());
                                            }),
                                    new WaitUntilCommand(() -> isStable()),
                                    new InstantCommand(
                                            () -> startTime[0] = Timer.getFPGATimestamp()),
                                    Commands.sequence(
                                                    transition.getCommand(),
                                                    new InstantCommand(
                                                            () ->
                                                                    setCurrentState(
                                                                            transition
                                                                                    .getToState())))
                                            .withTimeout(5),
                                    new InstantCommand(
                                            () -> {
                                                double duration =
                                                        Timer.getFPGATimestamp() - startTime[0];
                                                String logMessage;
                                                if (!getCurrentState()
                                                        .equals(transition.getToState())) {
                                                    logMessage =
                                                            transition.getFromState()
                                                                    + ","
                                                                    + transition.getToState()
                                                                    + ",100";
                                                } else {
                                                    logMessage =
                                                            transition.getFromState()
                                                                    + ","
                                                                    + transition.getToState()
                                                                    + ","
                                                                    + duration;
                                                }
                                                try (FileWriter fw =
                                                                new FileWriter(costFile, true);
                                                        BufferedWriter bw = new BufferedWriter(fw);
                                                        PrintWriter out = new PrintWriter(bw)) {
                                                    out.println(logMessage);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }))
                            .withTimeout(5);
            overallSequence = Commands.sequence(overallSequence, transitionSequence);
        }
        overallSequence =
                Commands.sequence(
                        overallSequence,
                        new InstantCommand(
                                () -> Logger.recordOutput("Characterization Complete", true)));
        return overallSequence;
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

    public void setDesiredState(SuperstructureState state, boolean setFuture, boolean wipeFuture) {
        if (currentState == null && desiredState != null) {
            if (setFuture) setFutureDesiredState(state);
            return;
        }
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State not registered: " + state);
        }

        // TODO: Add something such that if the desired state is a shooting state, don't move to
        // that state until a ball is in the handoff (assuming no manual override)

        desiredState = state;
        if (!DriverStation.isAutonomous() && wipeFuture) futureDesiredState = null;
        continueTransition();
    }

    public void continueTransition() {
        if (transitioning) {
            return;
        }

        if (currentState == null) {
            new ChezySequenceCommandGroup(
                            desiredState.getCommand(container),
                            new InstantCommand(
                                    () -> {
                                        setCurrentState(desiredState);
                                        transitioning = false;
                                        if (!currentState.equals(desiredState)) {
                                            continueTransition();
                                        }
                                    }))
                    .withName("SuperstructureMove")
                    .ignoringDisable(true)
                    .schedule();
            return;
        }

        if (currentState.equals(desiredState)) {
            return;
        }

        List<StateTransition> path = getPrecomputedPath(currentState, desiredState);
        if (path == null || path.isEmpty()) {
            setDesiredState(currentState);
            return;
        }

        StateTransition nextTransitionTemp = path.get(0);
        if (isTransitionBlocked(nextTransitionTemp)) {
            Logger.recordOutput(
                    "Superstructure/BlockedTransition",
                    "Precomputed transition "
                            + nextTransitionTemp.toString()
                            + " is blocked. Searching for alternative.");
            List<StateTransition> alternativePath =
                    computeDynamicTransitionPath(currentState, desiredState);
            if (alternativePath != null && !alternativePath.isEmpty()) {
                nextTransitionTemp = alternativePath.get(0);
            } else {
                Logger.recordOutput(
                        "Superstructure/BlockedTransition",
                        "No alternative transition available from " + currentState);
                return;
            }
        }
        final StateTransition nextTransition = nextTransitionTemp;
        Command transitionCommand = nextTransition.getCommand();
        transitioning = true;
        Command wrappedCommand =
                new ChezySequenceCommandGroup(
                        transitionCommand,
                        new InstantCommand(
                                () -> {
                                    setCurrentState(nextTransition.getToState());
                                    transitioning = false;
                                    if (!currentState.equals(desiredState)) {
                                        continueTransition();
                                    }
                                }));
        wrappedCommand.withName("SuperstructureMove").ignoringDisable(true).schedule();
    }

    public List<StateTransition> computeDynamicTransitionPath(
            SuperstructureState from, SuperstructureState to) {
        Map<SuperstructureState, List<StateTransition>> dynamicGraph = new HashMap<>();
        for (SuperstructureState state : states) {
            dynamicGraph.put(state, new ArrayList<>());
        }
        for (StateTransition t : transitions) {
            if (!t.hasCollision() && !isTransitionBlocked(t)) {
                dynamicGraph.get(t.getFromState()).add(t);
            }
        }
        AStarSolver<SuperstructureState> solver = new AStarSolver<>();
        List<SuperstructureState> statePath =
                solver.solve(
                        from,
                        to,
                        (current, goal) -> current.equals(goal) ? 0 : 1,
                        state -> {
                            List<AStarSolver.Edge<SuperstructureState>> neighbors =
                                    new ArrayList<>();
                            for (StateTransition t : dynamicGraph.get(state)) {
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
                    dynamicGraph.get(currentFrom).stream()
                            .filter(t -> t.getToState().equals(currentTo))
                            .findFirst();
            if (transition.isPresent()) {
                transitionPath.add(transition.get());
            } else {
                throw new IllegalStateException(
                        "No dynamic transition found from " + currentFrom + " to " + currentTo);
            }
        }
        return transitionPath;
    }

    private boolean isTransitionBlocked(StateTransition transition) {
        SuperstructureState toState = transition.getToState();
// if we try shooting and there's no balls in the robot, return true
        return false;
    }

    public boolean isStable() {
        return !transitioning;
    }

    private void setFutureDesiredState(SuperstructureState state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("State not registered: " + state);
        }
        futureDesiredState = state;
        futureDesiredStateTime = Timer.getFPGATimestamp();
    }

    private List<StateTransition> getPrecomputedPath(
            SuperstructureState from, SuperstructureState to) {
        return precomputedPaths[from.ordinal()][to.ordinal()];
    }

    public void wipeFutureDesiredState() {
        futureDesiredState = null;
    }

    public SuperstructureState getFutureDesiredState() {
        if (futureDesiredState != null && Timer.getFPGATimestamp() - futureDesiredStateTime > 3) {
            futureDesiredState = null;
        }
        return futureDesiredState;
    }

    public void applyFutureDesiredState() {
        if (getFutureDesiredState() != null) {
            Logger.recordOutput("LastApplyFutureDesiredState", Timer.getFPGATimestamp());
            setDesiredState(futureDesiredState);
            futureDesiredState = null;
        }
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

    private Boolean isShootingState(SuperstructureState state) {
        return state == SuperstructureState.INTAKE_AND_SHOOT || state == SuperstructureState.SHOOT;
    }

    private Boolean isIntakingState(SuperstructureState state) {
        return state == SuperstructureState.INTAKE_AND_SHOOT || state == SuperstructureState.INTAKE;
    }
}
