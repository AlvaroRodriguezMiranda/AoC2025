package aoc.day12.farm;

import java.util.List;

public record TreeRegion(int width, int length, List<Integer> presentCounts) {
    public TreeRegion {
        if (width <= 0 || length <= 0) {
            throw new IllegalArgumentException("Tree region dimensions must be positive");
        }
        presentCounts = List.copyOf(presentCounts);
    }

    public int area() {
        return width * length;
    }
}
