package aoc.day05.inventory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IngredientIdRangeTest {
    @Test
    void checksWhetherIngredientIdIsInsideRange() {
        IngredientIdRange range = new IngredientIdRange(3, 5);

        assertTrue(range.contains(3));
        assertTrue(range.contains(5));
        assertFalse(range.contains(2));
        assertFalse(range.contains(6));
    }

    @Test
    void rejectsRangeWithStartGreaterThanEnd() {
        assertThrows(IllegalArgumentException.class, () -> new IngredientIdRange(5, 3));
    }
}
