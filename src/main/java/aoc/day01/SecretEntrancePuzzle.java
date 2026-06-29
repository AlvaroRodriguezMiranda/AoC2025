package aoc.day01;

import aoc.day01.dial.RotationProgram;
import aoc.day01.input.RotationParser;
import aoc.day01.password.ClickPasswordSolver;
import aoc.day01.password.FinalPositionPasswordSolver;
import aoc.day01.password.PasswordSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class SecretEntrancePuzzle {
    private final RotationParser rotationParser;
    private final PasswordSolver partOneSolver;
    private final PasswordSolver partTwoSolver;

    public SecretEntrancePuzzle(RotationParser rotationParser, PasswordSolver partOneSolver, PasswordSolver partTwoSolver) {
        this.rotationParser = rotationParser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        RotationProgram rotationProgram = rotationParser.parseProgram(inputLines);
        return partOneSolver.solve(rotationProgram);
    }

    public int solvePartTwo(List<String> inputLines) {
        RotationProgram rotationProgram = rotationParser.parseProgram(inputLines);
        return partTwoSolver.solve(rotationProgram);
    }

    public static void main(String[] args) throws IOException {
        SecretEntrancePuzzle puzzle = new SecretEntrancePuzzle(
                new RotationParser(),
                new FinalPositionPasswordSolver(),
                new ClickPasswordSolver()
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day01/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
