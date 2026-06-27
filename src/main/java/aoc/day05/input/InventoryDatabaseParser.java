package aoc.day05.input;

import aoc.day05.inventory.IngredientIdRange;
import aoc.day05.inventory.InventoryDatabase;

import java.util.ArrayList;
import java.util.List;

public final class InventoryDatabaseParser {
    public InventoryDatabase parseLines(List<String> lines) {
        int separatorIndex = separatorIndex(lines);
        List<IngredientIdRange> freshRanges = parseRanges(lines.subList(0, separatorIndex));
        List<Long> availableIngredientIds = parseAvailableIngredientIds(lines.subList(separatorIndex + 1, lines.size()));
        return new InventoryDatabase(freshRanges, availableIngredientIds);
    }

    public IngredientIdRange parseRange(String line) {
        String[] parts = line.trim().split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Ingredient range must contain one dash: " + line);
        }

        long firstId = parseIngredientId(parts[0]);
        long lastId = parseIngredientId(parts[1]);
        return new IngredientIdRange(firstId, lastId);
    }

    private int separatorIndex(List<String> lines) {
        for (int index = 0; index < lines.size(); index++) {
            if (lines.get(index).isBlank()) {
                return index;
            }
        }
        throw new IllegalArgumentException("Inventory database must contain a blank line separator");
    }

    private List<IngredientIdRange> parseRanges(List<String> lines) {
        List<IngredientIdRange> ranges = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                ranges.add(parseRange(line));
            }
        }
        return ranges;
    }

    private List<Long> parseAvailableIngredientIds(List<String> lines) {
        List<Long> ingredientIds = new ArrayList<>();
        for (String line : lines) {
            if (!line.isBlank()) {
                ingredientIds.add(parseIngredientId(line));
            }
        }
        return ingredientIds;
    }

    private long parseIngredientId(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Ingredient ID must be a number: " + text, exception);
        }
    }
}
