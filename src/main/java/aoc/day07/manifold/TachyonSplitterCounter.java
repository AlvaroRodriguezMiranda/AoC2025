package aoc.day07.manifold;

import java.util.HashSet;
import java.util.Set;

public final class TachyonSplitterCounter {
    public int countSplits(TachyonManifold manifold) {
        Set<Integer> beamColumns = Set.of(manifold.startPosition().column());
        int splitCount = 0;

        for (int row = manifold.startPosition().row() + 1; row < manifold.height() && !beamColumns.isEmpty(); row++) {
            Set<Integer> nextBeamColumns = new HashSet<>();

            for (int column : beamColumns) {
                if (!manifold.contains(row, column)) {
                    continue;
                }

                if (manifold.hasSplitterAt(row, column)) {
                    splitCount++;
                    addBeamIfInside(nextBeamColumns, manifold, column - 1);
                    addBeamIfInside(nextBeamColumns, manifold, column + 1);
                } else {
                    nextBeamColumns.add(column);
                }
            }

            beamColumns = nextBeamColumns;
        }

        return splitCount;
    }

    private void addBeamIfInside(Set<Integer> beamColumns, TachyonManifold manifold, int column) {
        if (column >= 0 && column < manifold.width()) {
            beamColumns.add(column);
        }
    }
}
