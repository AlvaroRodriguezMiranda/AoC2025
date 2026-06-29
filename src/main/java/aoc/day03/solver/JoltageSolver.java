package aoc.day03.solver;

import aoc.day03.battery.BatteryBank;

import java.util.List;

public interface JoltageSolver {
    long solve(List<BatteryBank> batteryBanks);
}
