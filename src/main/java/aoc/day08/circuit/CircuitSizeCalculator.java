package aoc.day08.circuit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class CircuitSizeCalculator {
    public long multiplyThreeLargestCircuitSizes(List<JunctionBox> junctionBoxes, int connectionCount) {
        if (junctionBoxes.size() < 3) {
            throw new IllegalArgumentException("At least three junction boxes are required");
        }

        CircuitNetwork network = new CircuitNetwork(junctionBoxes.size());
        List<JunctionConnection> connections = findConnectionsByDistance(junctionBoxes);

        connections.stream()
                .limit(connectionCount)
                .forEach(connection -> network.connect(connection.firstIndex(), connection.secondIndex()));

        List<Integer> circuitSizes = network.circuitSizesDescending();
        return (long) circuitSizes.get(0) * circuitSizes.get(1) * circuitSizes.get(2);
    }

    public long multiplyLastConnectionXCoordinates(List<JunctionBox> junctionBoxes) {
        if (junctionBoxes.size() < 2) {
            throw new IllegalArgumentException("At least two junction boxes are required");
        }

        CircuitNetwork network = new CircuitNetwork(junctionBoxes.size());
        List<JunctionConnection> connections = findConnectionsByDistance(junctionBoxes);

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

    private List<JunctionConnection> findConnectionsByDistance(List<JunctionBox> junctionBoxes) {
        List<JunctionConnection> connections = new ArrayList<>();

        for (int firstIndex = 0; firstIndex < junctionBoxes.size(); firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < junctionBoxes.size(); secondIndex++) {
                JunctionBox first = junctionBoxes.get(firstIndex);
                JunctionBox second = junctionBoxes.get(secondIndex);
                connections.add(new JunctionConnection(firstIndex, secondIndex, first.squaredDistanceTo(second)));
            }
        }

        connections.sort(Comparator.comparingLong(JunctionConnection::squaredDistance));
        return connections;
    }
}
