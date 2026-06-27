package aoc.day06;

import aoc.day06.input.WorksheetParser;
import aoc.day06.worksheet.WorksheetSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrashCompactorPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        TrashCompactorPuzzle puzzle = new TrashCompactorPuzzle(new WorksheetParser(), new WorksheetSolver());

        long result = puzzle.solvePartOne(List.of(
                "123 328  51 64 ",
                " 45 64  387 23 ",
                "  6 98  215 314",
                "*   +   *   +  "
        ));

        assertEquals(4277556, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        TrashCompactorPuzzle puzzle = new TrashCompactorPuzzle(new WorksheetParser(), new WorksheetSolver());

        long result = puzzle.solvePartTwo(List.of(
                "123 328  51 64 ",
                " 45 64  387 23 ",
                "  6 98  215 314",
                "*   +   *   +  "
        ));

        assertEquals(3263827, result);
    }
}
