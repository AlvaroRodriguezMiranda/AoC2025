package aoc.day05.inventory;

public record IngredientIdRange(long firstId, long lastId) {
    public IngredientIdRange {
        if (firstId > lastId) {
            throw new IllegalArgumentException("Range start cannot be greater than range end");
        }
    }

    public boolean contains(long ingredientId) {
        return ingredientId >= firstId && ingredientId <= lastId;
    }
}
