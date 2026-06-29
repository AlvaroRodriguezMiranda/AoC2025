package aoc.day02.solver;

import aoc.day02.product.ProductIdRange;
import aoc.day02.product.RepeatedPatternProductId;

import java.util.HashSet;
import java.util.Set;

public final class DoublePatternInvalidProductIdRule implements InvalidProductIdRule {
    @Override
    public Set<Long> findInvalidProductIds(ProductIdRange range) {
        Set<Long> invalidProductIds = new HashSet<>();
        int maximumDigits = Long.toString(range.lastId()).length();

        for (int halfLength = 1; halfLength * 2 <= maximumDigits; halfLength++) {
            long firstHalf = RepeatedPatternProductId.powerOfTen(halfLength - 1);
            long lastHalf = RepeatedPatternProductId.powerOfTen(halfLength) - 1;

            for (long half = firstHalf; half <= lastHalf; half++) {
                long productId = RepeatedPatternProductId.fromRepeatedHalf(half, halfLength);
                if (range.contains(productId)) {
                    invalidProductIds.add(productId);
                }
            }
        }

        return invalidProductIds;
    }
}
