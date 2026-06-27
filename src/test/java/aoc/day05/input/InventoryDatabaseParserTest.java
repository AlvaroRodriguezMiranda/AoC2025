package aoc.day05.input;

import aoc.day05.inventory.IngredientIdRange;
import aoc.day05.inventory.InventoryDatabase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryDatabaseParserTest {
    private final InventoryDatabaseParser parser = new InventoryDatabaseParser();

    @Test
    void parsesIngredientRange() {
        IngredientIdRange range = parser.parseRange("10-14");

        assertEquals(10, range.firstId());
        assertEquals(14, range.lastId());
    }

    @Test
    void parsesDatabaseSectionsSeparatedByBlankLine() {
        InventoryDatabase database = parser.parseLines(List.of(
                "3-5",
                "10-14",
                "",
                "1",
                "5"
        ));

        assertEquals(2, database.freshRanges().size());
        assertEquals(2, database.availableIngredientIds().size());
    }

    @Test
    void rejectsDatabaseWithoutSeparator() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLines(List.of("3-5", "1")));
    }

    @Test
    void rejectsInvalidRangeFormat() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseRange("3/5"));
    }
}
