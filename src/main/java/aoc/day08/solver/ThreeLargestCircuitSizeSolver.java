package aoc.day08.solver;

import aoc.day08.circuit.CircuitNetwork;
import aoc.day08.circuit.JunctionBox;
import aoc.day08.circuit.JunctionConnectionPlanner;

import java.util.List;

public final class ThreeLargestCircuitSizeSolver implements CircuitSolver {
    private static final int DEFAULT_CONNECTION_COUNT = 1000;

    private final JunctionConnectionPlanner connectionPlanner;
    private final int connectionCount;

    public ThreeLargestCircuitSizeSolver(JunctionConnectionPlanner connectionPlanner) {
        this(connectionPlanner, DEFAULT_CONNECTION_COUNT);
    }

    public ThreeLargestCircuitSizeSolver(JunctionConnectionPlanner connectionPlanner, int connectionCount) {
        this.connectionPlanner = connectionPlanner;
        this.connectionCount = connectionCount;
    }

    @Override
    public long solve(List<JunctionBox> junctionBoxes) {
        if (junctionBoxes.size() < 3) {
            throw new IllegalArgumentException("At least three junction boxes are required");
        }

        CircuitNetwork network = new CircuitNetwork(junctionBoxes.size());
        connectionPlanner.connectionsByDistance(junctionBoxes).stream()
                .limit(connectionCount)
                .forEach(connection -> network.connect(connection.firstIndex(), connection.secondIndex()));

        List<Integer> circuitSizes = network.circuitSizesDescending();
        return (long) circuitSizes.get(0) * circuitSizes.get(1) * circuitSizes.get(2);
    }
}
