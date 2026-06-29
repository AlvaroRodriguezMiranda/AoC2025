package aoc.day11.solver;

import aoc.day11.reactor.DeviceNetwork;
import aoc.day11.reactor.PathCounter;

public final class RequiredDevicePathSolver implements ReactorSolver {
    private static final String START_DEVICE = "svr";
    private static final String TARGET_DEVICE = "out";
    private static final String FIRST_REQUIRED_DEVICE = "dac";
    private static final String SECOND_REQUIRED_DEVICE = "fft";

    private final PathCounter pathCounter;

    public RequiredDevicePathSolver(PathCounter pathCounter) {
        this.pathCounter = pathCounter;
    }

    @Override
    public long solve(DeviceNetwork network) {
        return pathCounter.countPathsVisitingBoth(
                network,
                START_DEVICE,
                TARGET_DEVICE,
                FIRST_REQUIRED_DEVICE,
                SECOND_REQUIRED_DEVICE
        );
    }
}
