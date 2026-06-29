package aoc.day08.circuit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class JunctionConnectionPlanner {
    public List<JunctionConnection> connectionsByDistance(List<JunctionBox> junctionBoxes) {
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
