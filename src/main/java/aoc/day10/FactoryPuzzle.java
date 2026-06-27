package aoc.day10;

import aoc.day10.factory.FactoryMachine;
import aoc.day10.factory.MachineInitializer;
import aoc.day10.input.FactoryManualParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FactoryPuzzle {
    private final FactoryManualParser parser;
    private final MachineInitializer initializer;

    public FactoryPuzzle(FactoryManualParser parser, MachineInitializer initializer) {
        this.parser = parser;
        this.initializer = initializer;
    }

    public int solvePartOne(List<String> inputLines) {
        List<FactoryMachine> machines = parser.parse(inputLines);
        return initializer.minimumPressesForAll(machines);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<FactoryMachine> machines = parser.parse(inputLines);
        return initializer.minimumJoltagePressesForAll(machines);
    }

    public static void main(String[] args) throws IOException {
        FactoryPuzzle puzzle = new FactoryPuzzle(new FactoryManualParser(), new MachineInitializer());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day10/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
