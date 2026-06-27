package aoc.day03.battery;

import java.util.List;

public final class JoltageCalculator {
    public int totalMaximumJoltage(List<BatteryBank> batteryBanks) {
        return batteryBanks.stream()
                .mapToInt(BatteryBank::maximumTwoBatteryJoltage)
                .sum();
    }

    public long totalMaximumJoltageUsingBatteries(List<BatteryBank> batteryBanks, int batteryCount) {
        return batteryBanks.stream()
                .mapToLong(batteryBank -> batteryBank.maximumJoltageUsingBatteries(batteryCount))
                .sum();
    }
}
