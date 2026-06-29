package aoc.day08.circuit;

import aoc.day08.solver.LastConnectionXCoordinateSolver;
import aoc.day08.solver.ThreeLargestCircuitSizeSolver;

import java.util.List;

public final class CircuitSizeCalculator {
    private final JunctionConnectionPlanner connectionPlanner;

    public CircuitSizeCalculator() {
        this(new JunctionConnectionPlanner());
    }

    public CircuitSizeCalculator(JunctionConnectionPlanner connectionPlanner) {
        this.connectionPlanner = connectionPlanner;
    }

    public long multiplyThreeLargestCircuitSizes(List<JunctionBox> junctionBoxes, int connectionCount) {
        return new ThreeLargestCircuitSizeSolver(connectionPlanner, connectionCount).solve(junctionBoxes);
    }

    public long multiplyLastConnectionXCoordinates(List<JunctionBox> junctionBoxes) {
        return new LastConnectionXCoordinateSolver(connectionPlanner).solve(junctionBoxes);
    }
}
