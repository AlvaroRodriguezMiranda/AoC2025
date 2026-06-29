package aoc.day06;

import aoc.day06.input.WorksheetParser;
import aoc.day06.solver.RightToLeftWorksheetSolver;
import aoc.day06.solver.TopToBottomWorksheetSolver;
import aoc.day06.solver.TrashCompactorSolver;
import aoc.day06.worksheet.WorksheetSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class TrashCompactorPuzzle {
    private final TrashCompactorSolver partOneSolver;
    private final TrashCompactorSolver partTwoSolver;

    public TrashCompactorPuzzle(WorksheetParser worksheetParser, WorksheetSolver worksheetSolver) {
        this(
                new TopToBottomWorksheetSolver(worksheetParser, worksheetSolver),
                new RightToLeftWorksheetSolver(worksheetParser, worksheetSolver)
        );
    }

    public TrashCompactorPuzzle(TrashCompactorSolver partOneSolver, TrashCompactorSolver partTwoSolver) {
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long solvePartOne(List<String> inputLines) {
        return partOneSolver.solve(inputLines);
    }

    public long solvePartTwo(List<String> inputLines) {
        return partTwoSolver.solve(inputLines);
    }

    public static void main(String[] args) throws IOException {
        WorksheetParser worksheetParser = new WorksheetParser();
        WorksheetSolver worksheetSolver = new WorksheetSolver();
        TrashCompactorPuzzle puzzle = new TrashCompactorPuzzle(
                new TopToBottomWorksheetSolver(worksheetParser, worksheetSolver),
                new RightToLeftWorksheetSolver(worksheetParser, worksheetSolver)
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day06/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
