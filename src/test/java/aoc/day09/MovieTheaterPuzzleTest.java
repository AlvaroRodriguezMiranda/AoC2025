package aoc.day09;

import aoc.day09.input.RedTileParser;
import aoc.day09.theater.LargestRectangleFinder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieTheaterPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        MovieTheaterPuzzle puzzle = new MovieTheaterPuzzle(new RedTileParser(), new LargestRectangleFinder());

        long result = puzzle.solvePartOne(officialExample());

        assertEquals(50, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        MovieTheaterPuzzle puzzle = new MovieTheaterPuzzle(new RedTileParser(), new LargestRectangleFinder());

        long result = puzzle.solvePartTwo(officialExample());

        assertEquals(24, result);
    }

    private List<String> officialExample() {
        return List.of(
                "7,1",
                "11,1",
                "11,7",
                "9,7",
                "9,5",
                "2,5",
                "2,3",
                "7,3"
        );
    }
}
