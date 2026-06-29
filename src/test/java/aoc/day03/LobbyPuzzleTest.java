package aoc.day03;

import aoc.day03.input.BatteryBankParser;
import aoc.day03.solver.TwelveBatteryJoltageSolver;
import aoc.day03.solver.TwoBatteryJoltageSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LobbyPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        LobbyPuzzle puzzle = new LobbyPuzzle(
                new BatteryBankParser(),
                new TwoBatteryJoltageSolver(),
                new TwelveBatteryJoltageSolver()
        );

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
        LobbyPuzzle puzzle = new LobbyPuzzle(
                new BatteryBankParser(),
                new TwoBatteryJoltageSolver(),
                new TwelveBatteryJoltageSolver()
        );

        long result = puzzle.solvePartTwo(List.of(
                "987654321111111",
                "811111111111119",
                "234234234234278",
                "818181911112111"
        ));

        assertEquals(3121910778619L, result);
    }
}
