package aoc.day09;

import aoc.day09.input.RedTileParser;
import aoc.day09.theater.LargestRectangleFinder;
import aoc.day09.theater.RedTile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class MovieTheaterPuzzle {
    private final RedTileParser parser;
    private final LargestRectangleFinder rectangleFinder;

    public MovieTheaterPuzzle(RedTileParser parser, LargestRectangleFinder rectangleFinder) {
        this.parser = parser;
        this.rectangleFinder = rectangleFinder;
    }

    public long solvePartOne(List<String> inputLines) {
        List<RedTile> redTiles = parser.parse(inputLines);
        return rectangleFinder.findLargestArea(redTiles);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<RedTile> redTiles = parser.parse(inputLines);
        return rectangleFinder.findLargestAreaInsideRedGreenArea(redTiles);
    }

    public static void main(String[] args) throws IOException {
        MovieTheaterPuzzle puzzle = new MovieTheaterPuzzle(new RedTileParser(), new LargestRectangleFinder());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day09/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
