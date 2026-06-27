package aoc.day12;

import aoc.day12.farm.PresentFitter;
import aoc.day12.input.FarmSummaryParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChristmasTreeFarmPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        ChristmasTreeFarmPuzzle puzzle = new ChristmasTreeFarmPuzzle(new FarmSummaryParser(), new PresentFitter());

        long result = puzzle.solvePartOne(officialExample());

        assertEquals(2, result);
    }

    static List<String> officialExample() {
        return List.of(
                "0:",
                "###",
                "##.",
                "##.",
                "",
                "1:",
                "###",
                "##.",
                ".##",
                "",
                "2:",
                ".##",
                "###",
                "##.",
                "",
                "3:",
                "##.",
                "###",
                "##.",
                "",
                "4:",
                "###",
                "#..",
                "###",
                "",
                "5:",
                "###",
                ".#.",
                "###",
                "",
                "4x4: 0 0 0 0 2 0",
                "12x5: 1 0 1 0 2 2",
                "12x5: 1 0 1 0 3 2"
        );
    }
}
