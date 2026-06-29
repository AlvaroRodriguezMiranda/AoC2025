package aoc.day03.solver;

import aoc.day03.battery.BatteryBank;

import java.util.List;

public final class TwoBatteryJoltageSolver implements JoltageSolver {
    private static final int BATTERY_COUNT = 2;

    @Override
    public long solve(List<BatteryBank> batteryBanks) {
        return batteryBanks.stream()
                .mapToLong(batteryBank -> batteryBank.maximumJoltageUsingBatteries(BATTERY_COUNT))
                .sum();
    }
}
