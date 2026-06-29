package aoc.day09.solver;

import aoc.day09.theater.RedGreenTileArea;
import aoc.day09.theater.RedTile;

import java.util.List;

public final class RedGreenRectangleAreaSolver implements TheaterAreaSolver {
    @Override
    public long solve(List<RedTile> redTiles) {
        if (redTiles.size() < 2) {
            throw new IllegalArgumentException("At least two red tiles are required");
        }

        RedGreenTileArea redGreenArea = RedGreenTileArea.from(redTiles);
        long largestArea = 0;

        for (int firstIndex = 0; firstIndex < redTiles.size(); firstIndex++) {
            for (int secondIndex = firstIndex + 1; secondIndex < redTiles.size(); secondIndex++) {
                RedTile first = redTiles.get(firstIndex);
                RedTile second = redTiles.get(secondIndex);
                long area = first.rectangleAreaWith(second);
                if (area > largestArea && redGreenArea.containsRectangleBetween(first, second)) {
                    largestArea = area;
                }
            }
        }

        return largestArea;
    }
}
