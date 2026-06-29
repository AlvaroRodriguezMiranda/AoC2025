package aoc.day04.solver;

import aoc.day04.paper.GridPosition;
import aoc.day04.paper.PaperRollGrid;

import java.util.ArrayList;
import java.util.List;

public final class RemovableRollSolver implements PaperRollSolver {
    @Override
    public int solve(PaperRollGrid paperRollGrid) {
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
            if (AccessibleRollSolver.isAccessible(paperRollGrid, position)) {
                accessiblePositions.add(position);
            }
        }

        return accessiblePositions;
    }
}
