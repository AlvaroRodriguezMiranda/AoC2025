package aoc.day11.solver;

import aoc.day11.reactor.DeviceNetwork;
import aoc.day11.reactor.PathCounter;

public final class DirectPathSolver implements ReactorSolver {
    private static final String START_DEVICE = "you";
    private static final String TARGET_DEVICE = "out";

    private final PathCounter pathCounter;

    public DirectPathSolver(PathCounter pathCounter) {
        this.pathCounter = pathCounter;
    }

    @Override
    public long solve(DeviceNetwork network) {
        return pathCounter.countPaths(network, START_DEVICE, TARGET_DEVICE);
    }
}
