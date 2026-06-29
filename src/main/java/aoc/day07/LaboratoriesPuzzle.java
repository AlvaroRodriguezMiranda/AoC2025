package aoc.day07;

import aoc.day07.input.TachyonManifoldParser;
import aoc.day07.manifold.QuantumTimelineCounter;
import aoc.day07.manifold.TachyonManifold;
import aoc.day07.manifold.TachyonSplitterCounter;
import aoc.day07.solver.QuantumTimelineSolver;
import aoc.day07.solver.SplitCountingSolver;
import aoc.day07.solver.TachyonSolver;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class LaboratoriesPuzzle {
    private final TachyonManifoldParser parser;
    private final TachyonSolver<Integer> partOneSolver;
    private final TachyonSolver<BigInteger> partTwoSolver;

    public LaboratoriesPuzzle(
            TachyonManifoldParser parser,
            TachyonSplitterCounter splitterCounter,
            QuantumTimelineCounter timelineCounter
    ) {
        this(parser, new SplitCountingSolver(splitterCounter), new QuantumTimelineSolver(timelineCounter));
    }

    public LaboratoriesPuzzle(
            TachyonManifoldParser parser,
            TachyonSolver<Integer> partOneSolver,
            TachyonSolver<BigInteger> partTwoSolver
    ) {
        this.parser = parser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        TachyonManifold manifold = parser.parse(inputLines);
        return partOneSolver.solve(manifold);
    }

    public BigInteger solvePartTwo(List<String> inputLines) {
        TachyonManifold manifold = parser.parse(inputLines);
        return partTwoSolver.solve(manifold);
    }

    public static void main(String[] args) throws IOException {
        LaboratoriesPuzzle puzzle = new LaboratoriesPuzzle(
                new TachyonManifoldParser(),
                new TachyonSplitterCounter(),
                new QuantumTimelineCounter()
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day07/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
