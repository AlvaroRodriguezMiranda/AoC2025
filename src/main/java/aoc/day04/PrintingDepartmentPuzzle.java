package aoc.day04;

import aoc.day04.input.PaperRollMapParser;
import aoc.day04.paper.ForkliftAccessSolver;
import aoc.day04.paper.PaperRollGrid;
import aoc.day04.solver.AccessibleRollSolver;
import aoc.day04.solver.RemovableRollSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class PrintingDepartmentPuzzle {
    private final PaperRollMapParser paperRollMapParser;
    private final ForkliftAccessSolver forkliftAccessSolver;

    public PrintingDepartmentPuzzle(PaperRollMapParser paperRollMapParser, ForkliftAccessSolver forkliftAccessSolver) {
        this.paperRollMapParser = paperRollMapParser;
        this.forkliftAccessSolver = forkliftAccessSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        PaperRollGrid paperRollGrid = paperRollMapParser.parseLines(inputLines);
        return forkliftAccessSolver.countAccessibleRolls(paperRollGrid);
    }

    public int solvePartTwo(List<String> inputLines) {
        PaperRollGrid paperRollGrid = paperRollMapParser.parseLines(inputLines);
        return forkliftAccessSolver.countRemovableRolls(paperRollGrid);
    }

    public static void main(String[] args) throws IOException {
        PrintingDepartmentPuzzle puzzle = new PrintingDepartmentPuzzle(
                new PaperRollMapParser(),
                new ForkliftAccessSolver(new AccessibleRollSolver(), new RemovableRollSolver())
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day04/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
