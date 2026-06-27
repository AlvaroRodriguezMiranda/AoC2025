package aoc.day03.battery;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BatteryBankTest {
    @Test
    void findsMaximumJoltageFromTwoBatteriesInOrder() {
        BatteryBank batteryBank = new BatteryBank(List.of(9, 8, 7, 6));

        assertEquals(98, batteryBank.maximumTwoBatteryJoltage());
    }

    @Test
    void keepsOriginalOrderWhenChoosingTwoBatteries() {
        BatteryBank batteryBank = new BatteryBank(List.of(8, 1, 1, 9));

        assertEquals(89, batteryBank.maximumTwoBatteryJoltage());
    }

    @Test
    void findsMaximumJoltageUsingTwelveBatteries() {
        BatteryBank batteryBank = new BatteryBank(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 1, 1, 1, 1, 1));

        assertEquals(987654321111L, batteryBank.maximumJoltageUsingBatteries(12));
    }

    @Test
    void respectsOrderWhenChoosingManyBatteries() {
        BatteryBank batteryBank = new BatteryBank(List.of(8, 1, 8, 1, 8, 1, 9, 1, 1, 1, 1, 2, 1, 1, 1));

        assertEquals(888911112111L, batteryBank.maximumJoltageUsingBatteries(12));
    }

    @Test
    void rejectsBankWithLessThanTwoBatteries() {
        assertThrows(IllegalArgumentException.class, () -> new BatteryBank(List.of(9)));
    }

    @Test
    void rejectsBatteryCountGreaterThanBankSize() {
        BatteryBank batteryBank = new BatteryBank(List.of(9, 8));

        assertThrows(IllegalArgumentException.class, () -> batteryBank.maximumJoltageUsingBatteries(3));
    }
}
