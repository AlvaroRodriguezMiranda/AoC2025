package aoc.day02.solver;

import aoc.day02.product.ProductIdRange;

import java.util.List;

public final class ProductIdSumCalculator {
    private final InvalidProductIdRule invalidProductIdRule;

    public ProductIdSumCalculator(InvalidProductIdRule invalidProductIdRule) {
        this.invalidProductIdRule = invalidProductIdRule;
    }

    public long sumInvalidProductIds(List<ProductIdRange> ranges) {
        long total = 0;

        for (ProductIdRange range : ranges) {
            for (long productId : invalidProductIdRule.findInvalidProductIds(range)) {
                total += productId;
            }
        }

        return total;
    }
}
