package aoc.day10;

import aoc.day10.factory.FactoryMachine;
import aoc.day10.factory.MachineInitializer;
import aoc.day10.input.FactoryManualParser;
import aoc.day10.solver.FactorySolver;
import aoc.day10.solver.JoltageInitializationSolver;
import aoc.day10.solver.LightInitializationSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FactoryPuzzle {
    private final FactoryManualParser parser;
    private final FactorySolver<Integer> partOneSolver;
    private final FactorySolver<Long> partTwoSolver;

    public FactoryPuzzle(FactoryManualParser parser, MachineInitializer initializer) {
        this(
                parser,
                new LightInitializationSolver(initializer),
                new JoltageInitializationSolver(initializer)
        );
    }

    public FactoryPuzzle(
            FactoryManualParser parser,
            FactorySolver<Integer> partOneSolver,
            FactorySolver<Long> partTwoSolver
    ) {
        this.parser = parser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        List<FactoryMachine> machines = parser.parse(inputLines);
        return partOneSolver.solve(machines);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<FactoryMachine> machines = parser.parse(inputLines);
        return partTwoSolver.solve(machines);
    }

    public static void main(String[] args) throws IOException {
        MachineInitializer initializer = new MachineInitializer();
        FactoryPuzzle puzzle = new FactoryPuzzle(
                new FactoryManualParser(),
                new LightInitializationSolver(initializer),
                new JoltageInitializationSolver(initializer)
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day10/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
