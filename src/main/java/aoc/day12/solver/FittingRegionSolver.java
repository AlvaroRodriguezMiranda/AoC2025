package aoc.day12.solver;

import aoc.day12.farm.FarmSummary;
import aoc.day12.farm.PresentFitter;

public final class FittingRegionSolver implements ChristmasTreeFarmSolver {
    private final PresentFitter presentFitter;

    public FittingRegionSolver(PresentFitter presentFitter) {
        this.presentFitter = presentFitter;
    }

    @Override
    public long solve(FarmSummary farmSummary) {
        return presentFitter.countFittingRegions(farmSummary);
    }
}
