package aoc.day05;

import aoc.day05.input.InventoryDatabaseParser;
import aoc.day05.inventory.FreshIngredientCounter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CafeteriaPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        CafeteriaPuzzle puzzle = new CafeteriaPuzzle(
                new InventoryDatabaseParser(),
                new FreshIngredientCounter()
        );

        long result = puzzle.solvePartOne(List.of(
                "3-5",
                "10-14",
                "16-20",
                "12-18",
                "",
                "1",
                "5",
                "8",
                "11",
                "17",
                "32"
        ));

        assertEquals(3, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        CafeteriaPuzzle puzzle = new CafeteriaPuzzle(
                new InventoryDatabaseParser(),
                new FreshIngredientCounter()
        );

        long result = puzzle.solvePartTwo(List.of(
                "3-5",
                "10-14",
                "16-20",
                "12-18",
                "",
                "1",
                "5",
                "8",
                "11",
                "17",
                "32"
        ));

        assertEquals(14, result);
    }
}
