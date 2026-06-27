package aoc.day04.paper;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForkliftAccessSolverTest {
    private final ForkliftAccessSolver solver = new ForkliftAccessSolver();

    @Test
    void countsRollWithFewerThanFourAdjacentRollsAsAccessible() {
        PaperRollGrid grid = new PaperRollGrid(List.of(
                "@@.",
                "@..",
                "..."
        ));

        assertEquals(3, solver.countAccessibleRolls(grid));
    }

    @Test
    void doesNotCountRollWithFourAdjacentRollsAsAccessible() {
        PaperRollGrid grid = new PaperRollGrid(List.of(
                "@@@",
                ".@.",
                "@.."
        ));

        assertEquals(4, solver.countAccessibleRolls(grid));
    }

    @Test
    void countsRollsRemovedAcrossRepeatedRounds() {
        PaperRollGrid grid = new PaperRollGrid(List.of(
                "@@@",
                "@@@",
                "@@@"
        ));

        assertEquals(9, solver.countRemovableRolls(grid));
    }
}
