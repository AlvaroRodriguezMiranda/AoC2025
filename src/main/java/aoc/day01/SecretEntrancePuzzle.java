package aoc.day01;

import aoc.day01.dial.DialRotation;
import aoc.day01.input.RotationParser;
import aoc.day01.password.PasswordSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class SecretEntrancePuzzle {
    private final RotationParser rotationParser;
    private final PasswordSolver passwordSolver;

    public SecretEntrancePuzzle(RotationParser rotationParser, PasswordSolver passwordSolver) {
        this.rotationParser = rotationParser;
        this.passwordSolver = passwordSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        List<DialRotation> rotations = rotationParser.parseLines(inputLines);
        return passwordSolver.countRotationsEndingAtZero(rotations);
    }

    public int solvePartTwo(List<String> inputLines) {
        List<DialRotation> rotations = rotationParser.parseLines(inputLines);
        return passwordSolver.countClicksLandingOnZero(rotations);
    }

    public static void main(String[] args) throws IOException {
        SecretEntrancePuzzle puzzle = new SecretEntrancePuzzle(new RotationParser(), new PasswordSolver());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day01/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
