package aoc.day07.solver;

import aoc.day07.manifold.QuantumTimelineCounter;
import aoc.day07.manifold.TachyonManifold;

import java.math.BigInteger;

public final class QuantumTimelineSolver implements TachyonSolver<BigInteger> {
    private final QuantumTimelineCounter timelineCounter;

    public QuantumTimelineSolver(QuantumTimelineCounter timelineCounter) {
        this.timelineCounter = timelineCounter;
    }

    @Override
    public BigInteger solve(TachyonManifold manifold) {
        return timelineCounter.countTimelines(manifold);
    }
}
