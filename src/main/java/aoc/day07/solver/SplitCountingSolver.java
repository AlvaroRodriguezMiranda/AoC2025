package aoc.day07.solver;

import aoc.day07.manifold.TachyonManifold;
import aoc.day07.manifold.TachyonSplitterCounter;

public final class SplitCountingSolver implements TachyonSolver<Integer> {
    private final TachyonSplitterCounter splitterCounter;

    public SplitCountingSolver(TachyonSplitterCounter splitterCounter) {
        this.splitterCounter = splitterCounter;
    }

    @Override
    public Integer solve(TachyonManifold manifold) {
        return splitterCounter.countSplits(manifold);
    }
}
