package aoc.day09.solver;

import aoc.day09.theater.RedTile;

import java.util.List;

public final class UnrestrictedRectangleAreaSolver implements TheaterAreaSolver {
    @Override
    public long solve(List<RedTile> redTiles) {
        if (redTiles.size() < 2) {
            throw new IllegalArgumentException("At least two red tiles are required");
        }

        long largestArea = 0;
        for (int firstIndex = 0; firstIndex < redTiles.size(); firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < redTiles.size(); secondIndex++) {
                long area = redTiles.get(firstIndex).rectangleAreaWith(redTiles.get(secondIndex));
                largestArea = Math.max(largestArea, area);
            }
        }

        return largestArea;
    }
}
