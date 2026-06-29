package aoc.day02.solver;

import aoc.day02.product.ProductIdRange;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductIdSumCalculatorTest {
    @Test
    void sumsInvalidProductIdsInOfficialSmallRanges() {
        ProductIdSumCalculator calculator = new ProductIdSumCalculator(new DoublePatternInvalidProductIdRule());

        long result = calculator.sumInvalidProductIds(List.of(
                new ProductIdRange(11, 22),
                new ProductIdRange(95, 115),
                new ProductIdRange(998, 1012)
        ));

        assertEquals(11 + 22 + 99 + 1010, result);
    }

    @Test
    void returnsZeroWhenRangeHasNoInvalidProductIds() {
        ProductIdSumCalculator calculator = new ProductIdSumCalculator(new DoublePatternInvalidProductIdRule());

        long result = calculator.sumInvalidProductIds(List.of(new ProductIdRange(1698522, 1698528)));

        assertEquals(0, result);
    }

    @Test
    void sumsInvalidProductIdsWithRepeatedSequencesInOfficialSmallRanges() {
        ProductIdSumCalculator calculator = new ProductIdSumCalculator(new RepeatedPatternInvalidProductIdRule());

        long result = calculator.sumInvalidProductIds(List.of(
                new ProductIdRange(11, 22),
                new ProductIdRange(95, 115),
                new ProductIdRange(998, 1012)
        ));

        assertEquals(11 + 22 + 99 + 111 + 999 + 1010, result);
    }

    @Test
    void doesNotCountSameRepeatedSequenceProductIdTwice() {
        ProductIdSumCalculator calculator = new ProductIdSumCalculator(new RepeatedPatternInvalidProductIdRule());

        long result = calculator.sumInvalidProductIds(List.of(new ProductIdRange(111111, 111111)));

        assertEquals(111111, result);
    }
}
