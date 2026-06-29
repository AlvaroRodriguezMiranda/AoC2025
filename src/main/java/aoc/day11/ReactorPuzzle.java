package aoc.day11;

import aoc.day11.input.DeviceNetworkParser;
import aoc.day11.reactor.DeviceNetwork;
import aoc.day11.reactor.PathCounter;
import aoc.day11.solver.DirectPathSolver;
import aoc.day11.solver.ReactorSolver;
import aoc.day11.solver.RequiredDevicePathSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ReactorPuzzle {
    private final DeviceNetworkParser parser;
    private final ReactorSolver partOneSolver;
    private final ReactorSolver partTwoSolver;

    public ReactorPuzzle(DeviceNetworkParser parser, PathCounter pathCounter) {
        this(parser, new DirectPathSolver(pathCounter), new RequiredDevicePathSolver(pathCounter));
    }

    public ReactorPuzzle(DeviceNetworkParser parser, ReactorSolver partOneSolver, ReactorSolver partTwoSolver) {
        this.parser = parser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long solvePartOne(List<String> inputLines) {
        DeviceNetwork network = parser.parse(inputLines);
        return partOneSolver.solve(network);
    }

    public long solvePartTwo(List<String> inputLines) {
        DeviceNetwork network = parser.parse(inputLines);
        return partTwoSolver.solve(network);
    }

    public static void main(String[] args) throws IOException {
        PathCounter pathCounter = new PathCounter();
        ReactorPuzzle puzzle = new ReactorPuzzle(
                new DeviceNetworkParser(),
                new DirectPathSolver(pathCounter),
                new RequiredDevicePathSolver(pathCounter)
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day11/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
