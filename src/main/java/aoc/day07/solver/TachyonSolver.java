package aoc.day07.solver;

import aoc.day07.manifold.TachyonManifold;

public interface TachyonSolver<T> {
    T solve(TachyonManifold manifold);
}
