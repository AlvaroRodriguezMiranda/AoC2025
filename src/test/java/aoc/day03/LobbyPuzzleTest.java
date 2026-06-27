package aoc.day03;

import aoc.day03.battery.JoltageCalculator;
import aoc.day03.input.BatteryBankParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LobbyPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        LobbyPuzzle puzzle = new LobbyPuzzle(new BatteryBankParser(), new JoltageCalculator());

        int result = puzzle.solvePartOne(List.of(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        assertEquals(357, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        LobbyPuzzle puzzle = new LobbyPuzzle(new BatteryBankParser(), new JoltageCalculator());

        long result = puzzle.solvePartTwo(List.of(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        assertEquals(3121910778619L, result);
    }
}
