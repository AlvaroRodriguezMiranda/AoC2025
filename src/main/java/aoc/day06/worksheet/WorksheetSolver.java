package aoc.day06.worksheet;

import java.util.List;

public final class WorksheetSolver {
    public long grandTotal(List<MathProblem> problems) {
        return problems.stream()
                .mapToLong(MathProblem::solve)
                .sum();
    }
}
