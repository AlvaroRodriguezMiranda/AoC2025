package aoc.day09;

import aoc.day09.input.RedTileParser;
import aoc.day09.solver.RedGreenRectangleAreaSolver;
import aoc.day09.solver.TheaterAreaSolver;
import aoc.day09.solver.UnrestrictedRectangleAreaSolver;
import aoc.day09.theater.LargestRectangleFinder;
import aoc.day09.theater.RedTile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class MovieTheaterPuzzle {
    private final RedTileParser parser;
    private final TheaterAreaSolver partOneSolver;
    private final TheaterAreaSolver partTwoSolver;

    public MovieTheaterPuzzle(RedTileParser parser, LargestRectangleFinder rectangleFinder) {
        this(parser, new UnrestrictedRectangleAreaSolver(), new RedGreenRectangleAreaSolver());
    }

    public MovieTheaterPuzzle(RedTileParser parser, TheaterAreaSolver partOneSolver, TheaterAreaSolver partTwoSolver) {
        this.parser = parser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long solvePartOne(List<String> inputLines) {
        List<RedTile> redTiles = parser.parse(inputLines);
        return partOneSolver.solve(redTiles);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<RedTile> redTiles = parser.parse(inputLines);
        return partTwoSolver.solve(redTiles);
    }

    public static void main(String[] args) throws IOException {
        MovieTheaterPuzzle puzzle = new MovieTheaterPuzzle(
                new RedTileParser(),
                new UnrestrictedRectangleAreaSolver(),
                new RedGreenRectangleAreaSolver()
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day09/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
