package aoc.day11;

import aoc.day11.input.DeviceNetworkParser;
import aoc.day11.reactor.DeviceNetwork;
import aoc.day11.reactor.PathCounter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ReactorPuzzle {
    private final DeviceNetworkParser parser;
    private final PathCounter pathCounter;

    public ReactorPuzzle(DeviceNetworkParser parser, PathCounter pathCounter) {
        this.parser = parser;
        this.pathCounter = pathCounter;
    }

    public long solvePartOne(List<String> inputLines) {
        DeviceNetwork network = parser.parse(inputLines);
        return pathCounter.countPaths(network, "you", "out");
    }

    public long solvePartTwo(List<String> inputLines) {
        DeviceNetwork network = parser.parse(inputLines);
        return pathCounter.countPathsVisitingBoth(network, "svr", "out", "dac", "fft");
    }

    public static void main(String[] args) throws IOException {
        ReactorPuzzle puzzle = new ReactorPuzzle(new DeviceNetworkParser(), new PathCounter());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day11/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
