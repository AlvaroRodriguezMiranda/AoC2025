package aoc.day04.paper;

import java.util.ArrayList;
import java.util.List;

public final class ForkliftAccessSolver {
    private static final int MAXIMUM_ACCESSIBLE_ADJACENT_ROLLS = 3;

    public int countAccessibleRolls(PaperRollGrid paperRollGrid) {
        return accessibleRollPositions(paperRollGrid).size();
    }

    public int countRemovableRolls(PaperRollGrid paperRollGrid) {
        int removedRolls = 0;
        PaperRollGrid currentGrid = paperRollGrid;

        List<GridPosition> accessiblePositions = accessibleRollPositions(currentGrid);
        while (!accessiblePositions.isEmpty()) {
            removedRolls += accessiblePositions.size();
            currentGrid = currentGrid.withoutPaperRolls(accessiblePositions);
            accessiblePositions = accessibleRollPositions(currentGrid);
        }

        return removedRolls;
    }

    private List<GridPosition> accessibleRollPositions(PaperRollGrid paperRollGrid) {
        List<GridPosition> accessiblePositions = new ArrayList<>();

        for (GridPosition position : paperRollGrid.paperRollPositions()) {
            if (paperRollGrid.adjacentPaperRolls(position) <= MAXIMUM_ACCESSIBLE_ADJACENT_ROLLS) {
                accessiblePositions.add(position);
            }
        }

        return accessiblePositions;
    }
}
