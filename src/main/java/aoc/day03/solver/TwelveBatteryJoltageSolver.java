package aoc.day03.solver;

import aoc.day03.battery.BatteryBank;

import java.util.List;

public final class TwelveBatteryJoltageSolver implements JoltageSolver {
    private static final int BATTERY_COUNT = 12;

    @Override
    public long solve(List<BatteryBank> batteryBanks) {
        return batteryBanks.stream()
                .mapToLong(batteryBank -> batteryBank.maximumJoltageUsingBatteries(BATTERY_COUNT))
                .sum();
    }
}
