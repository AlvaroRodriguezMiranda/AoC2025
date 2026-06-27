package aoc.day03.battery;

import java.util.List;

public final class BatteryBank {
    private final List<Integer> joltageRatings;

    public BatteryBank(List<Integer> joltageRatings) {
        if (joltageRatings.size() < 2) {
            throw new IllegalArgumentException("A battery bank must contain at least two batteries");
        }
        this.joltageRatings = List.copyOf(joltageRatings);
    }

    public int maximumTwoBatteryJoltage() {
        return (int) maximumJoltageUsingBatteries(2);
    }

    public long maximumJoltageUsingBatteries(int batteryCount) {
        if (batteryCount < 1 || batteryCount > joltageRatings.size()) {
            throw new IllegalArgumentException("Battery count must be between 1 and the bank size");
        }

        long maximumJoltage = 0;
        int startIndex = 0;

        for (int selected = 0; selected < batteryCount; selected++) {
            int remainingAfterSelection = batteryCount - selected - 1;
            int lastPossibleIndex = joltageRatings.size() - remainingAfterSelection - 1;
            int bestIndex = startIndex;

            for (int index = startIndex + 1; index <= lastPossibleIndex; index++) {
                if (joltageRatings.get(index) > joltageRatings.get(bestIndex)) {
                    bestIndex = index;
                }
            }

            maximumJoltage = maximumJoltage * 10 + joltageRatings.get(bestIndex);
            startIndex = bestIndex + 1;
        }

        return maximumJoltage;
    }

    public int size() {
        return joltageRatings.size();
    }
}
