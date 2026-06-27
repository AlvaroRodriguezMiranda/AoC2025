package aoc.day02.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductIdRangeTest {
    @Test
    void checksWhetherProductIdIsInsideRange() {
        ProductIdRange range = new ProductIdRange(95, 115);

        assertTrue(range.contains(99));
        assertFalse(range.contains(94));
        assertFalse(range.contains(116));
    }

    @Test
    void rejectsRangeWithStartGreaterThanEnd() {
        assertThrows(IllegalArgumentException.class, () -> new ProductIdRange(22, 11));
    }
}
