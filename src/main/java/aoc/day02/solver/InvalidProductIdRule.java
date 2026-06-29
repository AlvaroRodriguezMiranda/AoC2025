package aoc.day02.solver;

import aoc.day02.product.ProductIdRange;

import java.util.Set;

public interface InvalidProductIdRule {
    Set<Long> findInvalidProductIds(ProductIdRange range);
}
