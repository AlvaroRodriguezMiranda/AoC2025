package aoc.day12;

import aoc.day12.farm.FarmSummary;
import aoc.day12.farm.PresentFitter;
import aoc.day12.input.FarmSummaryParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ChristmasTreeFarmPuzzle {
    private final FarmSummaryParser parser;
    private final PresentFitter presentFitter;

    public ChristmasTreeFarmPuzzle(FarmSummaryParser parser, PresentFitter presentFitter) {
        this.parser = parser;
        this.presentFitter = presentFitter;
    }

    public long solvePartOne(List<String> inputLines) {
        FarmSummary farmSummary = parser.parse(inputLines);
        return presentFitter.countFittingRegions(farmSummary);
    }

    public static void main(String[] args) throws IOException {
        ChristmasTreeFarmPuzzle puzzle = new ChristmasTreeFarmPuzzle(new FarmSummaryParser(), new PresentFitter());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day12/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
    }
}
