package aoc.day07.manifold;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public final class QuantumTimelineCounter {
    public BigInteger countTimelines(TachyonManifold manifold) {
        Map<Integer, BigInteger> activeTimelines = Map.of(manifold.startPosition().column(), BigInteger.ONE);
        BigInteger completedTimelines = BigInteger.ZERO;

        for (int row = manifold.startPosition().row() + 1; row < manifold.height() && !activeTimelines.isEmpty(); row++) {
            Map<Integer, BigInteger> nextTimelines = new HashMap<>();

            for (Map.Entry<Integer, BigInteger> timeline : activeTimelines.entrySet()) {
                int column = timeline.getKey();
                BigInteger count = timeline.getValue();

                if (manifold.hasSplitterAt(row, column)) {
                    completedTimelines = addSplitTimeline(nextTimelines, manifold, column - 1, count, completedTimelines);
                    completedTimelines = addSplitTimeline(nextTimelines, manifold, column + 1, count, completedTimelines);
                } else {
                    nextTimelines.merge(column, count, BigInteger::add);
                }
            }

            activeTimelines = nextTimelines;
        }

        return activeTimelines.values().stream()
                .reduce(completedTimelines, BigInteger::add);
    }

    private BigInteger addSplitTimeline(
            Map<Integer, BigInteger> timelines,
            TachyonManifold manifold,
            int column,
            BigInteger count,
            BigInteger completedTimelines
    ) {
        if (column < 0 || column >= manifold.width()) {
            return completedTimelines.add(count);
        }

        timelines.merge(column, count, BigInteger::add);
        return completedTimelines;
    }
}
