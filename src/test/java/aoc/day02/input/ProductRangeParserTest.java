package aoc.day02.input;

import aoc.day02.product.ProductIdRange;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductRangeParserTest {
    private final ProductRangeParser parser = new ProductRangeParser();

    @Test
    void parsesSingleRange() {
        ProductIdRange range = parser.parseRange("95-115");

        assertEquals(95, range.firstId());
        assertEquals(115, range.lastId());
    }

    @Test
    void parsesCommaSeparatedRangesAndIgnoresTrailingComma() {
        List<ProductIdRange> ranges = parser.parseRanges("11-22, 95-115,");

        assertEquals(2, ranges.size());
        assertEquals(new ProductIdRange(11, 22), ranges.get(0));
        assertEquals(new ProductIdRange(95, 115), ranges.get(1));
    }

    @Test
    void removesWhitespaceInsertedInsideLongInputLine() {
        List<ProductIdRange> ranges = parser.parseRanges("580816-6161 31,162 32012-16441905");

        assertEquals(new ProductIdRange(580816, 616131), ranges.get(0));
        assertEquals(new ProductIdRange(16232012, 16441905), ranges.get(1));
    }

    @Test
    void rejectsInvalidRangeFormat() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseRange("11/22"));
    }

    @Test
    void rejectsEmptyInputLine() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseRanges(" "));
    }
}
