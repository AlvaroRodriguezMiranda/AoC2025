package aoc.day05.solver;

import aoc.day05.inventory.IngredientIdRange;
import aoc.day05.inventory.InventoryDatabase;

import java.util.Comparator;
import java.util.List;

public final class AllFreshIngredientSolver implements FreshIngredientSolver {
    @Override
    public long solve(InventoryDatabase inventoryDatabase) {
        List<IngredientIdRange> sortedRanges = inventoryDatabase.freshRanges().stream()
                .sorted(Comparator.comparingLong(IngredientIdRange::firstId))
                .toList();
        if (sortedRanges.isEmpty()) {
            return 0;
        }

        long totalFreshIds = 0;
        long currentFirstId = sortedRanges.getFirst().firstId();
        long currentLastId = sortedRanges.getFirst().lastId();

        for (int index = 1; index < sortedRanges.size(); index++) {
            IngredientIdRange range = sortedRanges.get(index);
            if (range.firstId() <= currentLastId + 1) {
                currentLastId = Math.max(currentLastId, range.lastId());
            } else {
                totalFreshIds += currentLastId - currentFirstId + 1;
                currentFirstId = range.firstId();
                currentLastId = range.lastId();
            }
        }

        return totalFreshIds + currentLastId - currentFirstId + 1;
    }
}
