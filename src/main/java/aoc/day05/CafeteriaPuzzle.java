package aoc.day05;

import aoc.day05.input.InventoryDatabaseParser;
import aoc.day05.inventory.FreshIngredientCounter;
import aoc.day05.inventory.InventoryDatabase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CafeteriaPuzzle {
    private final InventoryDatabaseParser inventoryDatabaseParser;
    private final FreshIngredientCounter freshIngredientCounter;

    public CafeteriaPuzzle(
            InventoryDatabaseParser inventoryDatabaseParser,
            FreshIngredientCounter freshIngredientCounter
    ) {
        this.inventoryDatabaseParser = inventoryDatabaseParser;
        this.freshIngredientCounter = freshIngredientCounter;
    }

    public long solvePartOne(List<String> inputLines) {
        InventoryDatabase inventoryDatabase = inventoryDatabaseParser.parseLines(inputLines);
        return freshIngredientCounter.countFreshAvailableIngredients(inventoryDatabase);
    }

    public long solvePartTwo(List<String> inputLines) {
        InventoryDatabase inventoryDatabase = inventoryDatabaseParser.parseLines(inputLines);
        return freshIngredientCounter.countAllFreshIngredientIds(inventoryDatabase);
    }

    public static void main(String[] args) throws IOException {
        CafeteriaPuzzle puzzle = new CafeteriaPuzzle(
                new InventoryDatabaseParser(),
                new FreshIngredientCounter()
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day05/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
