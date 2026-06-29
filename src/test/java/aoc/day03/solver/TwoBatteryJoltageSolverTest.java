package aoc.day03.solver;

import aoc.day03.battery.BatteryBank;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TwoBatteryJoltageSolverTest {
    @Test
    void sumsMaximumJoltageForAllBanks() {
        long result = new TwoBatteryJoltageSolver().solve(List.of(
                new BatteryBank(List.of(9, 8, 7)),
                new BatteryBank(List.of(1, 2, 3))
        ));

        assertEquals(98 + 23, result);
    }

    @Test
    void sumsMaximumJoltageUsingSelectedBatteryCount() {
        long result = new TwoBatteryJoltageSolver().solve(List.of(
                new BatteryBank(List.of(9, 8, 7)),
                new BatteryBank(List.of(1, 2, 3))
        ));

        assertEquals(98 + 23, result);
    }
}
