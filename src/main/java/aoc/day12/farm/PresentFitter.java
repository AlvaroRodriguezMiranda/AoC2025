package aoc.day12.farm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;

public final class PresentFitter {
    private static final int DIRECT_PACKING_MINIMUM_SIDE = 20;

    public long countFittingRegions(FarmSummary farmSummary) {
        return farmSummary.regions().stream()
                .filter(region -> canFit(farmSummary.shapes(), region))
                .count();
    }

    public boolean canFit(List<PresentShape> shapes, TreeRegion region) {
        int requiredArea = requiredArea(shapes, region);
        if (requiredArea > region.area()) {
            return false;
        }
        if (Math.min(region.width(), region.length()) >= DIRECT_PACKING_MINIMUM_SIDE) {
            return true;
        }

        List<List<BitSet>> placementsByShape = shapes.stream()
                .map(shape -> placementsFor(shape, region))
                .toList();

        int[] remainingCounts = region.presentCounts().stream()
                .mapToInt(Integer::intValue)
                .toArray();

        return canFitRemaining(placementsByShape, remainingCounts, new BitSet(region.area()), region.area(), requiredArea);
    }

    private boolean canFitRemaining(
            List<List<BitSet>> placementsByShape,
            int[] remainingCounts,
            BitSet occupiedCells,
            int regionArea,
            int remainingArea
    ) {
        if (allCountsAreZero(remainingCounts)) {
            return true;
        }
        if (regionArea - occupiedCells.cardinality() < remainingArea) {
            return false;
        }

        int selectedShape = selectShapeWithFewestPlacements(placementsByShape, remainingCounts, occupiedCells);
        if (selectedShape == -1) {
            return false;
        }

        for (BitSet placement : compatiblePlacements(placementsByShape.get(selectedShape), occupiedCells)) {
            BitSet nextOccupiedCells = (BitSet) occupiedCells.clone();
            nextOccupiedCells.or(placement);
            remainingCounts[selectedShape]--;
            if (canFitRemaining(
                    placementsByShape,
                    remainingCounts,
                    nextOccupiedCells,
                    regionArea,
                    remainingArea - placement.cardinality()
            )) {
                remainingCounts[selectedShape]++;
                return true;
            }
            remainingCounts[selectedShape]++;
        }

        return false;
    }

    private int selectShapeWithFewestPlacements(List<List<BitSet>> placementsByShape, int[] remainingCounts, BitSet occupiedCells) {
        int selectedShape = -1;
        int selectedPlacementCount = Integer.MAX_VALUE;

        for (int shapeIndex = 0; shapeIndex < remainingCounts.length; shapeIndex++) {
            if (remainingCounts[shapeIndex] == 0) {
                continue;
            }

            int placementCount = compatiblePlacementCount(placementsByShape.get(shapeIndex), occupiedCells);
            if (placementCount == 0) {
                return -1;
            }
            if (placementCount < selectedPlacementCount) {
                selectedShape = shapeIndex;
                selectedPlacementCount = placementCount;
            }
        }

        return selectedShape;
    }

    private int compatiblePlacementCount(List<BitSet> placements, BitSet occupiedCells) {
        int count = 0;
        for (BitSet placement : placements) {
            if (!placement.intersects(occupiedCells)) {
                count++;
            }
        }
        return count;
    }

    private List<BitSet> compatiblePlacements(List<BitSet> placements, BitSet occupiedCells) {
        return placements.stream()
                .filter(placement -> !placement.intersects(occupiedCells))
                .sorted(Comparator.comparingInt(BitSet::cardinality).reversed())
                .toList();
    }

    private List<BitSet> placementsFor(PresentShape shape, TreeRegion region) {
        List<BitSet> placements = new ArrayList<>();

        for (ShapeOrientation orientation : shape.orientations()) {
            for (int y = 0; y <= region.length() - orientation.height(); y++) {
                for (int x = 0; x <= region.width() - orientation.width(); x++) {
                    placements.add(toPlacement(orientation, region, x, y));
                }
            }
        }

        return placements;
    }

    private BitSet toPlacement(ShapeOrientation orientation, TreeRegion region, int offsetX, int offsetY) {
        BitSet placement = new BitSet(region.area());
        for (GridCell cell : orientation.cells()) {
            int x = offsetX + cell.x();
            int y = offsetY + cell.y();
            placement.set(y * region.width() + x);
        }
        return placement;
    }

    private int requiredArea(List<PresentShape> shapes, TreeRegion region) {
        int area = 0;
        for (int index = 0; index < shapes.size(); index++) {
            area += shapes.get(index).area() * region.presentCounts().get(index);
        }
        return area;
    }

    private boolean allCountsAreZero(int[] remainingCounts) {
        for (int count : remainingCounts) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }
}
