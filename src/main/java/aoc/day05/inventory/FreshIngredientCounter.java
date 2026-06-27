package aoc.day05.inventory;

import java.util.Comparator;
import java.util.List;

public final class FreshIngredientCounter {
    public long countFreshAvailableIngredients(InventoryDatabase inventoryDatabase) {
        return inventoryDatabase.availableIngredientIds().stream()
                .filter(ingredientId -> isFresh(ingredientId, inventoryDatabase))
                .count();
    }

    public long countAllFreshIngredientIds(InventoryDatabase inventoryDatabase) {
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

    private boolean isFresh(long ingredientId, InventoryDatabase inventoryDatabase) {
        return inventoryDatabase.freshRanges().stream()
                .anyMatch(range -> range.contains(ingredientId));
    }
}
