package aoc.day04.paper;

import aoc.day04.solver.PaperRollSolver;
import aoc.day04.solver.AccessibleRollSolver;
import aoc.day04.solver.RemovableRollSolver;

public final class ForkliftAccessSolver {
    private final PaperRollSolver partOneSolver;
    private final PaperRollSolver partTwoSolver;

    public ForkliftAccessSolver() {
        this(new AccessibleRollSolver(), new RemovableRollSolver());
    }

    public ForkliftAccessSolver(PaperRollSolver partOneSolver, PaperRollSolver partTwoSolver) {
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public int countAccessibleRolls(PaperRollGrid paperRollGrid) {
        return partOneSolver.solve(paperRollGrid);
    }

    public int countRemovableRolls(PaperRollGrid paperRollGrid) {
        return partTwoSolver.solve(paperRollGrid);
    }
}
