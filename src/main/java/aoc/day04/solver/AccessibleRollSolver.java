package aoc.day04.solver;

import aoc.day04.paper.GridPosition;
import aoc.day04.paper.PaperRollGrid;

public final class AccessibleRollSolver implements PaperRollSolver {
    private static final int MAXIMUM_ACCESSIBLE_ADJACENT_ROLLS = 3;

    @Override
    public int solve(PaperRollGrid paperRollGrid) {
        int accessibleRolls = 0;

        for (GridPosition position : paperRollGrid.paperRollPositions()) {
            if (isAccessible(paperRollGrid, position)) {
                accessibleRolls++;
            }
        }

        return accessibleRolls;
    }

    static boolean isAccessible(PaperRollGrid paperRollGrid, GridPosition position) {
        return paperRollGrid.adjacentPaperRolls(position) <= MAXIMUM_ACCESSIBLE_ADJACENT_ROLLS;
    }
}
