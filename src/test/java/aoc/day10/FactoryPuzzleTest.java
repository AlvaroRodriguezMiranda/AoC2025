package aoc.day10;

import aoc.day10.factory.MachineInitializer;
import aoc.day10.input.FactoryManualParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactoryPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        FactoryPuzzle puzzle = new FactoryPuzzle(new FactoryManualParser(), new MachineInitializer());

        int result = puzzle.solvePartOne(officialExample());

        assertEquals(7, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        FactoryPuzzle puzzle = new FactoryPuzzle(new FactoryManualParser(), new MachineInitializer());

        long result = puzzle.solvePartTwo(officialExample());

        assertEquals(33, result);
    }

    private List<String> officialExample() {
        return List.of(
                "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}",
                "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}",
                "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}"
        );
    }
}
