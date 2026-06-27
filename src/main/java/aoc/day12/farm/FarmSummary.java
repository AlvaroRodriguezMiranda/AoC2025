package aoc.day12.farm;

import java.util.List;

public record FarmSummary(List<PresentShape> shapes, List<TreeRegion> regions) {
    public FarmSummary {
        if (shapes.isEmpty()) {
            throw new IllegalArgumentException("Farm summary must contain present shapes");
        }
        shapes = List.copyOf(shapes);
        regions = List.copyOf(regions);
    }
}
