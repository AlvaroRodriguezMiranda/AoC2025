package aoc.day04;

import aoc.day04.input.PaperRollMapParser;
import aoc.day04.paper.ForkliftAccessSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrintingDepartmentPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        PrintingDepartmentPuzzle puzzle = new PrintingDepartmentPuzzle(
                new PaperRollMapParser(),
                new ForkliftAccessSolver()
        );

        int result = puzzle.solvePartOne(List.of(
                "..@@.@@@@.",
                "@@@.@.@.@@",
                "@@@@@.@.@@",
                "@.@@@@..@.",
                "@@.@@@@.@@",
                ".@@@@@@@.@",
                ".@.@.@.@@@",
                "@.@@@.@@@@",
                ".@@@@@@@@.",
                "@.@.@@@.@."
        ));

        assertEquals(13, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        PrintingDepartmentPuzzle puzzle = new PrintingDepartmentPuzzle(
                new PaperRollMapParser(),
                new ForkliftAccessSolver()
        );

        int result = puzzle.solvePartTwo(List.of(
                "..@@.@@@@.",
                "@@@.@.@.@@",
                "@@@@@.@.@@",
                "@.@@@@..@.",
                "@@.@@@@.@@",
                ".@@@@@@@.@",
                ".@.@.@.@@@",
                "@.@@@.@@@@",
                ".@@@@@@@@.",
                "@.@.@@@.@."
        ));

        assertEquals(43, result);
    }
}
