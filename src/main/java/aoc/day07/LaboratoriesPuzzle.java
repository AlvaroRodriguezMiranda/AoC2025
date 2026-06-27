package aoc.day07;

import aoc.day07.input.TachyonManifoldParser;
import aoc.day07.manifold.QuantumTimelineCounter;
import aoc.day07.manifold.TachyonManifold;
import aoc.day07.manifold.TachyonSplitterCounter;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class LaboratoriesPuzzle {
    private final TachyonManifoldParser parser;
    private final TachyonSplitterCounter splitterCounter;
    private final QuantumTimelineCounter timelineCounter;

    public LaboratoriesPuzzle(
            TachyonManifoldParser parser,
            TachyonSplitterCounter splitterCounter,
            QuantumTimelineCounter timelineCounter
    ) {
        this.parser = parser;
        this.splitterCounter = splitterCounter;
        this.timelineCounter = timelineCounter;
    }

    public int solvePartOne(List<String> inputLines) {
        TachyonManifold manifold = parser.parse(inputLines);
        return splitterCounter.countSplits(manifold);
    }

    public BigInteger solvePartTwo(List<String> inputLines) {
        TachyonManifold manifold = parser.parse(inputLines);
        return timelineCounter.countTimelines(manifold);
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
