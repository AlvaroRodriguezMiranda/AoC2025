package aoc.day08;

import aoc.day08.circuit.CircuitSizeCalculator;
import aoc.day08.circuit.JunctionBox;
import aoc.day08.circuit.JunctionConnectionPlanner;
import aoc.day08.input.JunctionBoxParser;
import aoc.day08.solver.CircuitSolver;
import aoc.day08.solver.LastConnectionXCoordinateSolver;
import aoc.day08.solver.ThreeLargestCircuitSizeSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PlaygroundPuzzle {
    private final JunctionBoxParser parser;
    private final CircuitSolver partOneSolver;
    private final CircuitSolver partTwoSolver;

    public PlaygroundPuzzle(JunctionBoxParser parser, CircuitSizeCalculator calculator) {
        this(
                parser,
                new ThreeLargestCircuitSizeSolver(new JunctionConnectionPlanner()),
                new LastConnectionXCoordinateSolver(new JunctionConnectionPlanner())
        );
    }

    public PlaygroundPuzzle(JunctionBoxParser parser, CircuitSolver partOneSolver, CircuitSolver partTwoSolver) {
        this.parser = parser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long solvePartOne(List<String> inputLines) {
        List<JunctionBox> junctionBoxes = parser.parse(inputLines);
        return partOneSolver.solve(junctionBoxes);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<JunctionBox> junctionBoxes = parser.parse(inputLines);
        return partTwoSolver.solve(junctionBoxes);
    }

    public static void main(String[] args) throws IOException {
        JunctionConnectionPlanner connectionPlanner = new JunctionConnectionPlanner();
        PlaygroundPuzzle puzzle = new PlaygroundPuzzle(
                new JunctionBoxParser(),
                new ThreeLargestCircuitSizeSolver(connectionPlanner),
                new LastConnectionXCoordinateSolver(connectionPlanner)
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day08/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
