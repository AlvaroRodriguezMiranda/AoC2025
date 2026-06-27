package aoc.day04.paper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperRollGridTest {
    @Test
    void detectsPaperRollPositions() {
        PaperRollGrid grid = new PaperRollGrid(List.of(".@", "@."));

        assertFalse(grid.hasPaperRollAt(new GridPosition(0, 0)));
        assertTrue(grid.hasPaperRollAt(new GridPosition(0, 1)));
        assertTrue(grid.hasPaperRollAt(new GridPosition(1, 0)));
    }

    @Test
    void countsAdjacentPaperRollsInEightDirections() {
        PaperRollGrid grid = new PaperRollGrid(List.of(
                "@@@",
                "@.@",
                "@@@"
        ));

        assertEquals(8, grid.adjacentPaperRolls(new GridPosition(1, 1)));
    }

    @Test
    void countsAdjacentPaperRollsAtMapEdge() {
        PaperRollGrid grid = new PaperRollGrid(List.of(
                "@@.",
                "@..",
                "..."
        ));

        assertEquals(2, grid.adjacentPaperRolls(new GridPosition(0, 0)));
    }

    @Test
    void returnsAllPaperRollPositions() {
        PaperRollGrid grid = new PaperRollGrid(List.of(".@", "@."));

        assertEquals(2, grid.paperRollPositions().size());
    }

    @Test
    void createsNewGridWithoutSelectedPaperRolls() {
        PaperRollGrid grid = new PaperRollGrid(List.of(".@", "@."));

        PaperRollGrid updatedGrid = grid.withoutPaperRolls(List.of(new GridPosition(0, 1)));

        assertTrue(grid.hasPaperRollAt(new GridPosition(0, 1)));
        assertFalse(updatedGrid.hasPaperRollAt(new GridPosition(0, 1)));
        assertTrue(updatedGrid.hasPaperRollAt(new GridPosition(1, 0)));
    }
}
