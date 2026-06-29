package aoc.day05.solver;

import aoc.day05.inventory.InventoryDatabase;

public final class AvailableFreshIngredientSolver implements FreshIngredientSolver {
    @Override
    public long solve(InventoryDatabase inventoryDatabase) {
        return inventoryDatabase.availableIngredientIds().stream()
                .filter(ingredientId -> isFresh(ingredientId, inventoryDatabase))
                .count();
    }

    private boolean isFresh(long ingredientId, InventoryDatabase inventoryDatabase) {
        return inventoryDatabase.freshRanges().stream()
                .anyMatch(range -> range.contains(ingredientId));
    }
}
