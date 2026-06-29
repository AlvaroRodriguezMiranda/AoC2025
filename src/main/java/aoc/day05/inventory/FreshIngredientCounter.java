package aoc.day05.inventory;

import aoc.day05.solver.FreshIngredientSolver;
import aoc.day05.solver.AllFreshIngredientSolver;
import aoc.day05.solver.AvailableFreshIngredientSolver;

public final class FreshIngredientCounter {
    private final FreshIngredientSolver partOneSolver;
    private final FreshIngredientSolver partTwoSolver;

    public FreshIngredientCounter() {
        this(new AvailableFreshIngredientSolver(), new AllFreshIngredientSolver());
    }

    public FreshIngredientCounter(FreshIngredientSolver partOneSolver, FreshIngredientSolver partTwoSolver) {
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public long countFreshAvailableIngredients(InventoryDatabase inventoryDatabase) {
        return partOneSolver.solve(inventoryDatabase);
    }

    public long countAllFreshIngredientIds(InventoryDatabase inventoryDatabase) {
        return partTwoSolver.solve(inventoryDatabase);
    }
}
