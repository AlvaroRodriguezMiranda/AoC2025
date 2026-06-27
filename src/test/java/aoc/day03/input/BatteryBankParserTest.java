package aoc.day03.input;

import aoc.day03.battery.BatteryBank;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BatteryBankParserTest {
    private final BatteryBankParser parser = new BatteryBankParser();

    @Test
    void parsesBatteryBankLine() {
        BatteryBank batteryBank = parser.parseLine("12345");

        assertEquals(5, batteryBank.size());
        assertEquals(45, batteryBank.maximumTwoBatteryJoltage());
    }

    @Test
    void parsesLinesIgnoringBlankLines() {
        List<BatteryBank> batteryBanks = parser.parseLines(List.of("123", "", "987"));

        assertEquals(2, batteryBanks.size());
    }

    @Test
    void rejectsZeroRating() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLine("120"));
    }

    @Test
    void rejectsNonDigitRating() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLine("12A"));
    }
}
