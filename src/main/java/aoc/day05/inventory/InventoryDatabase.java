package aoc.day05.inventory;

import java.util.List;

public final class InventoryDatabase {
    private final List<IngredientIdRange> freshRanges;
    private final List<Long> availableIngredientIds;

    public InventoryDatabase(List<IngredientIdRange> freshRanges, List<Long> availableIngredientIds) {
        this.freshRanges = List.copyOf(freshRanges);
        this.availableIngredientIds = List.copyOf(availableIngredientIds);
    }

    public List<IngredientIdRange> freshRanges() {
        return freshRanges;
    }

    public List<Long> availableIngredientIds() {
        return availableIngredientIds;
    }
}
