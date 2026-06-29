package aoc.day10.solver;

import aoc.day10.factory.FactoryMachine;

import java.util.List;

public interface FactorySolver<T> {
    T solve(List<FactoryMachine> machines);
}
