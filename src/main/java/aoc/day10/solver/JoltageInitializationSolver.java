package aoc.day10.solver;

import aoc.day10.factory.FactoryMachine;
import aoc.day10.factory.MachineInitializer;

import java.util.List;

public final class JoltageInitializationSolver implements FactorySolver<Long> {
    private final MachineInitializer initializer;

    public JoltageInitializationSolver(MachineInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public Long solve(List<FactoryMachine> machines) {
        return initializer.minimumJoltagePressesForAll(machines);
    }
}
