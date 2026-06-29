package aoc.day06.solver;

import aoc.day06.input.WorksheetParser;
import aoc.day06.worksheet.MathProblem;
import aoc.day06.worksheet.WorksheetSolver;

import java.util.List;

public final class RightToLeftWorksheetSolver implements TrashCompactorSolver {
    private final WorksheetParser worksheetParser;
    private final WorksheetSolver worksheetSolver;

    public RightToLeftWorksheetSolver(WorksheetParser worksheetParser, WorksheetSolver worksheetSolver) {
        this.worksheetParser = worksheetParser;
        this.worksheetSolver = worksheetSolver;
    }

    @Override
    public long solve(List<String> inputLines) {
        List<MathProblem> problems = worksheetParser.parseRightToLeftLines(inputLines);
        return worksheetSolver.grandTotal(problems);
    }
}
