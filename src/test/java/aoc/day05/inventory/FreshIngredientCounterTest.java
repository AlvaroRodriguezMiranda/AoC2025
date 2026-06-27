package aoc.day05.inventory;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FreshIngredientCounterTest {
    private final FreshIngredientCounter counter = new FreshIngredientCounter();

    @Test
    void countsAvailableIngredientsInsideAnyFreshRange() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(new IngredientIdRange(3, 5), new IngredientIdRange(10, 14)),
                List.of(1L, 5L, 8L, 11L)
        );

        assertEquals(2, counter.countFreshAvailableIngredients(database));
    }

    @Test
    void countsIngredientOnlyOnceEvenWhenRangesOverlap() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(new IngredientIdRange(10, 20), new IngredientIdRange(12, 18)),
                List.of(17L)
        );

        assertEquals(1, counter.countFreshAvailableIngredients(database));
    }

    @Test
    void countsAllFreshIngredientIdsMergingOverlappingRanges() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(
                        new IngredientIdRange(3, 5),
                        new IngredientIdRange(10, 14),
                        new IngredientIdRange(16, 20),
                        new IngredientIdRange(12, 18)
                ),
                List.of()
        );

        assertEquals(14, counter.countAllFreshIngredientIds(database));
    }

    @Test
    void countsSeparatedRangesWithoutMergingThem() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(new IngredientIdRange(1, 2), new IngredientIdRange(10, 12)),
                List.of()
        );

        assertEquals(5, counter.countAllFreshIngredientIds(database));
    }

    @Test
    void countsAdjacentRangesAsOneContinuousFreshArea() {
        InventoryDatabase database = new InventoryDatabase(
                List.of(new IngredientIdRange(1, 3), new IngredientIdRange(4, 6)),
                List.of()
        );

        assertEquals(6, counter.countAllFreshIngredientIds(database));
    }
}
