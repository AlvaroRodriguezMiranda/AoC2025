package aoc.day09.theater;

import aoc.day09.solver.RedGreenRectangleAreaSolver;
import aoc.day09.solver.TheaterAreaSolver;
import aoc.day09.solver.UnrestrictedRectangleAreaSolver;

import java.util.List;

public final class LargestRectangleFinder {
    private final TheaterAreaSolver partOneSolver;
    private final TheaterAreaSolver partTwoSolver;

    public LargestRectangleFinder() {
        this(new UnrestrictedRectangleAreaSolver(), new RedGreenRectangleAreaSolver());
    }

    public LargestRectangleFinder(TheaterAreaSolver partOneSolver, TheaterAreaSolver partTwoSolver) {
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long findLargestArea(List<RedTile> redTiles) {
        return partOneSolver.solve(redTiles);
    }

    public long findLargestAreaInsideRedGreenArea(List<RedTile> redTiles) {
        return partTwoSolver.solve(redTiles);
    }
}
