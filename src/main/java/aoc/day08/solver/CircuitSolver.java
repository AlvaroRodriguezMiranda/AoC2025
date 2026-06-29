package aoc.day08.solver;

import aoc.day08.circuit.JunctionBox;

import java.util.List;

public interface CircuitSolver {
    long solve(List<JunctionBox> junctionBoxes);
}
