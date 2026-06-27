package aoc.day03.battery;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JoltageCalculatorTest {
    @Test
    void sumsMaximumJoltageForAllBanks() {
        JoltageCalculator calculator = new JoltageCalculator();

        int result = calculator.totalMaximumJoltage(List.of(
                new BatteryBank(List.of(9, 8, 7)),
                new BatteryBank(List.of(1, 2, 3))
        ));

        assertEquals(98 + 23, result);
    }

    @Test
    void sumsMaximumJoltageUsingSelectedBatteryCount() {
        JoltageCalculator calculator = new JoltageCalculator();

        long result = calculator.totalMaximumJoltageUsingBatteries(List.of(
                new BatteryBank(List.of(9, 8, 7)),
                new BatteryBank(List.of(1, 2, 3))
        ), 2);

        assertEquals(98 + 23, result);
    }
}
