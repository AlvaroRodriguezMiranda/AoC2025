package aoc.day10.solver;

import aoc.day10.factory.FactoryMachine;
import aoc.day10.factory.MachineInitializer;

import java.util.List;

public final class LightInitializationSolver implements FactorySolver<Integer> {
    private final MachineInitializer initializer;

    public LightInitializationSolver(MachineInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public Integer solve(List<FactoryMachine> machines) {
        return initializer.minimumPressesForAll(machines);
    }
}
