package aoc.day12;

import aoc.day12.farm.FarmSummary;
import aoc.day12.farm.PresentFitter;
import aoc.day12.input.FarmSummaryParser;
import aoc.day12.solver.ChristmasTreeFarmSolver;
import aoc.day12.solver.FittingRegionSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ChristmasTreeFarmPuzzle {
    private final FarmSummaryParser parser;
    private final ChristmasTreeFarmSolver solver;

    public ChristmasTreeFarmPuzzle(FarmSummaryParser parser, PresentFitter presentFitter) {
        this(parser, new FittingRegionSolver(presentFitter));
    }

    public ChristmasTreeFarmPuzzle(FarmSummaryParser parser, ChristmasTreeFarmSolver solver) {
        this.parser = parser;
        this.solver = solver;
    }

    public long solvePartOne(List<String> inputLines) {
        FarmSummary farmSummary = parser.parse(inputLines);
        return solver.solve(farmSummary);
    }

    public static void main(String[] args) throws IOException {
        ChristmasTreeFarmPuzzle puzzle = new ChristmasTreeFarmPuzzle(
                new FarmSummaryParser(),
                new FittingRegionSolver(new PresentFitter())
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day12/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
    }
}
