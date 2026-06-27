package aoc.day02.product;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductIdSumCalculatorTest {
    private final ProductIdSumCalculator calculator = new ProductIdSumCalculator();

    @Test
    void sumsInvalidProductIdsInOfficialSmallRanges() {
        long result = calculator.sumInvalidProductIds(List.of(
                new ProductIdRange(11, 22),
                new ProductIdRange(95, 115),
                new ProductIdRange(998, 1012)
        ));

        assertEquals(11 + 22 + 99 + 1010, result);
    }

    @Test
    void returnsZeroWhenRangeHasNoInvalidProductIds() {
        long result = calculator.sumInvalidProductIds(List.of(new ProductIdRange(1698522, 1698528)));

        assertEquals(0, result);
    }

    @Test
    void sumsInvalidProductIdsWithRepeatedSequencesInOfficialSmallRanges() {
        long result = calculator.sumInvalidProductIdsWithRepeatedSequences(List.of(
                new ProductIdRange(11, 22),
                new ProductIdRange(95, 115),
                new ProductIdRange(998, 1012)
        ));

        assertEquals(11 + 22 + 99 + 111 + 999 + 1010, result);
    }

    @Test
    void doesNotCountSameRepeatedSequenceProductIdTwice() {
        long result = calculator.sumInvalidProductIdsWithRepeatedSequences(List.of(new ProductIdRange(111111, 111111)));

        assertEquals(111111, result);
    }
}
