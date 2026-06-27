package aoc.day08;

import aoc.day08.circuit.CircuitSizeCalculator;
import aoc.day08.circuit.JunctionBox;
import aoc.day08.input.JunctionBoxParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PlaygroundPuzzle {
    private final JunctionBoxParser parser;
    private final CircuitSizeCalculator calculator;

    public PlaygroundPuzzle(JunctionBoxParser parser, CircuitSizeCalculator calculator) {
        this.parser = parser;
        this.calculator = calculator;
    }

    public long solvePartOne(List<String> inputLines) {
        List<JunctionBox> junctionBoxes = parser.parse(inputLines);
        return calculator.multiplyThreeLargestCircuitSizes(junctionBoxes, 1000);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<JunctionBox> junctionBoxes = parser.parse(inputLines);
        return calculator.multiplyLastConnectionXCoordinates(junctionBoxes);
    }

    public static void main(String[] args) throws IOException {
        PlaygroundPuzzle puzzle = new PlaygroundPuzzle(new JunctionBoxParser(), new CircuitSizeCalculator());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day08/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
