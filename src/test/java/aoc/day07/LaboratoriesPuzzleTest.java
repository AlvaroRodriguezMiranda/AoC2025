package aoc.day07;

import aoc.day07.input.TachyonManifoldParser;
import aoc.day07.manifold.QuantumTimelineCounter;
import aoc.day07.manifold.TachyonSplitterCounter;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LaboratoriesPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        LaboratoriesPuzzle puzzle = createPuzzle();

        int result = puzzle.solvePartOne(officialExample());

        assertEquals(21, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        LaboratoriesPuzzle puzzle = createPuzzle();

        BigInteger result = puzzle.solvePartTwo(officialExample());

        assertEquals(BigInteger.valueOf(40), result);
    }

    private LaboratoriesPuzzle createPuzzle() {
        return new LaboratoriesPuzzle(
                new TachyonManifoldParser(),
                new TachyonSplitterCounter(),
                new QuantumTimelineCounter()
        );
    }

    private List<String> officialExample() {
        return List.of(
                ".......S.......",
                "...............",
                ".......^.......",
                "...............",
                "......^.^......",
                "...............",
                ".....^.^.^.....",
                "...............",
                "....^.^...^....",
                "...............",
                "...^.^...^.^...",
                "...............",
                "..^...^.....^..",
                "...............",
                ".^.^.^.^.^...^.",
                "..............."
        );
    }
}
