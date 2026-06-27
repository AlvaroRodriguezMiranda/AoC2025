package aoc.day01;

import aoc.day01.input.RotationParser;
import aoc.day01.password.PasswordSolver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretEntrancePuzzleTest {
    @Test
    void solvesPartOneFromInputLines() {
        SecretEntrancePuzzle puzzle = new SecretEntrancePuzzle(new RotationParser(), new PasswordSolver());

        int password = puzzle.solvePartOne(List.of("R50", "L1"));

        assertEquals(1, password);
    }

    @Test
    void solvesPartTwoFromInputLines() {
        SecretEntrancePuzzle puzzle = new SecretEntrancePuzzle(new RotationParser(), new PasswordSolver());

        int password = puzzle.solvePartTwo(List.of("R1000"));

        assertEquals(10, password);
    }
}
