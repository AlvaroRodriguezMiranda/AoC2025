package aoc.day06;

import aoc.day06.input.WorksheetParser;
import aoc.day06.worksheet.MathProblem;
import aoc.day06.worksheet.WorksheetSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class TrashCompactorPuzzle {
    private final WorksheetParser worksheetParser;
    private final WorksheetSolver worksheetSolver;

    public TrashCompactorPuzzle(WorksheetParser worksheetParser, WorksheetSolver worksheetSolver) {
        this.worksheetParser = worksheetParser;
        this.worksheetSolver = worksheetSolver;
    }

    public long solvePartOne(List<String> inputLines) {
        List<MathProblem> problems = worksheetParser.parseLines(inputLines);
        return worksheetSolver.grandTotal(problems);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<MathProblem> problems = worksheetParser.parseRightToLeftLines(inputLines);
        return worksheetSolver.grandTotal(problems);
    }

    public static void main(String[] args) throws IOException {
        TrashCompactorPuzzle puzzle = new TrashCompactorPuzzle(new WorksheetParser(), new WorksheetSolver());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day06/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
