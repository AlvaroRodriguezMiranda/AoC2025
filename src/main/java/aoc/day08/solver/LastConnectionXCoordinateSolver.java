package aoc.day08.solver;

import aoc.day08.circuit.CircuitNetwork;
import aoc.day08.circuit.JunctionBox;
import aoc.day08.circuit.JunctionConnection;
import aoc.day08.circuit.JunctionConnectionPlanner;

import java.util.List;

public final class LastConnectionXCoordinateSolver implements CircuitSolver {
    private final JunctionConnectionPlanner connectionPlanner;

    public LastConnectionXCoordinateSolver(JunctionConnectionPlanner connectionPlanner) {
        this.connectionPlanner = connectionPlanner;
    }

    @Override
    public long solve(List<JunctionBox> junctionBoxes) {
        if (junctionBoxes.size() < 2) {
            throw new IllegalArgumentException("At least two junction boxes are required");
        }

        CircuitNetwork network = new CircuitNetwork(junctionBoxes.size());
        List<JunctionConnection> connections = connectionPlanner.connectionsByDistance(junctionBoxes);

        for (JunctionConnection connection : connections) {
            boolean connected = network.connect(connection.firstIndex(), connection.secondIndex());
            if (connected && network.isSingleCircuit()) {
                JunctionBox first = junctionBoxes.get(connection.firstIndex());
                JunctionBox second = junctionBoxes.get(connection.secondIndex());
                return first.x() * second.x();
            }
        }

        throw new IllegalStateException("Junction boxes could not be connected into one circuit");
    }
}
