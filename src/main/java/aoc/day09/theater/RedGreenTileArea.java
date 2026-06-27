package aoc.day09.theater;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

final class RedGreenTileArea {
    private final List<VerticalBand> allowedBands;

    private RedGreenTileArea(List<VerticalBand> allowedBands) {
        this.allowedBands = allowedBands;
    }

    static RedGreenTileArea from(List<RedTile> redTiles) {
        Bounds bounds = Bounds.from(redTiles);
        AxisCompression xAxis = AxisCompression.from(redTiles.stream().map(RedTile::x).toList(), bounds.minimumX() - 1, bounds.maximumX() + 1);
        AxisCompression yAxis = AxisCompression.from(redTiles.stream().map(RedTile::y).toList(), bounds.minimumY() - 1, bounds.maximumY() + 1);
        boolean[][] boundary = buildBoundary(redTiles, xAxis, yAxis);
        boolean[][] outside = findOutsideTiles(boundary, xAxis, yAxis);
        return new RedGreenTileArea(buildAllowedBands(bounds, xAxis, yAxis, outside));
    }

    boolean containsRectangleBetween(RedTile first, RedTile second) {
        long minimumX = Math.min(first.x(), second.x());
        long maximumX = Math.max(first.x(), second.x());
        long minimumY = Math.min(first.y(), second.y());
        long maximumY = Math.max(first.y(), second.y());

        return allowedBands.stream()
                .filter(band -> band.overlaps(minimumY, maximumY))
                .allMatch(band -> band.containsHorizontalRange(minimumX, maximumX));
    }

    private static boolean[][] buildBoundary(List<RedTile> redTiles, AxisCompression xAxis, AxisCompression yAxis) {
        boolean[][] boundary = new boolean[yAxis.size()][xAxis.size()];

        for (int index = 0; index < redTiles.size(); index++) {
            RedTile current = redTiles.get(index);
            RedTile next = redTiles.get((index + 1) % redTiles.size());
            addBoundaryLine(boundary, xAxis, yAxis, current, next);
        }

        return boundary;
    }

    private static void addBoundaryLine(boolean[][] boundary, AxisCompression xAxis, AxisCompression yAxis, RedTile first, RedTile second) {
        if (first.x() == second.x()) {
            int xIndex = xAxis.indexOfSingleCoordinate(first.x());
            long minimumY = Math.min(first.y(), second.y());
            long maximumY = Math.max(first.y(), second.y());
            for (int yIndex = 0; yIndex < yAxis.size(); yIndex++) {
                if (yAxis.rangeAt(yIndex).overlaps(minimumY, maximumY)) {
                    boundary[yIndex][xIndex] = true;
                }
            }
            return;
        }

        if (first.y() == second.y()) {
            int yIndex = yAxis.indexOfSingleCoordinate(first.y());
            long minimumX = Math.min(first.x(), second.x());
            long maximumX = Math.max(first.x(), second.x());
            for (int xIndex = 0; xIndex < xAxis.size(); xIndex++) {
                if (xAxis.rangeAt(xIndex).overlaps(minimumX, maximumX)) {
                    boundary[yIndex][xIndex] = true;
                }
            }
            return;
        }

        throw new IllegalArgumentException("Adjacent red tiles must share a row or a column");
    }

    private static boolean[][] findOutsideTiles(boolean[][] boundary, AxisCompression xAxis, AxisCompression yAxis) {
        boolean[][] outside = new boolean[yAxis.size()][xAxis.size()];
        Queue<BlockPosition> pendingBlocks = new ArrayDeque<>();
        pendingBlocks.add(new BlockPosition(0, 0));
        outside[0][0] = true;

        while (!pendingBlocks.isEmpty()) {
            BlockPosition block = pendingBlocks.remove();
            for (BlockPosition neighbor : block.neighbors()) {
                if (!neighbor.isInside(xAxis.size(), yAxis.size())
                        || boundary[neighbor.yIndex()][neighbor.xIndex()]
                        || outside[neighbor.yIndex()][neighbor.xIndex()]) {
                    continue;
                }
                outside[neighbor.yIndex()][neighbor.xIndex()] = true;
                pendingBlocks.add(neighbor);
            }
        }

        return outside;
    }

    private static List<VerticalBand> buildAllowedBands(Bounds bounds, AxisCompression xAxis, AxisCompression yAxis, boolean[][] outside) {
        List<VerticalBand> bands = new ArrayList<>();

        for (int yIndex = 0; yIndex < yAxis.size(); yIndex++) {
            CoordinateRange yRange = yAxis.rangeAt(yIndex).clip(bounds.minimumY(), bounds.maximumY());
            if (yRange == null) {
                continue;
            }

            List<HorizontalRange> horizontalRanges = new ArrayList<>();
            Long rangeStart = null;
            Long rangeEnd = null;

            for (int xIndex = 0; xIndex < xAxis.size(); xIndex++) {
                CoordinateRange xRange = xAxis.rangeAt(xIndex).clip(bounds.minimumX(), bounds.maximumX());
                boolean allowed = xRange != null && !outside[yIndex][xIndex];

                if (allowed && rangeStart == null) {
                    rangeStart = xRange.minimum();
                    rangeEnd = xRange.maximum();
                } else if (allowed) {
                    rangeEnd = xRange.maximum();
                } else if (rangeStart != null) {
                    horizontalRanges.add(new HorizontalRange(rangeStart, rangeEnd));
                    rangeStart = null;
                    rangeEnd = null;
                }
            }

            if (rangeStart != null) {
                horizontalRanges.add(new HorizontalRange(rangeStart, rangeEnd));
            }

            bands.add(new VerticalBand(yRange.minimum(), yRange.maximum(), horizontalRanges));
        }

        return bands;
    }

    private record Bounds(long minimumX, long maximumX, long minimumY, long maximumY) {
        static Bounds from(List<RedTile> redTiles) {
            long minimumX = redTiles.stream().mapToLong(RedTile::x).min().orElseThrow();
            long maximumX = redTiles.stream().mapToLong(RedTile::x).max().orElseThrow();
            long minimumY = redTiles.stream().mapToLong(RedTile::y).min().orElseThrow();
            long maximumY = redTiles.stream().mapToLong(RedTile::y).max().orElseThrow();
            return new Bounds(minimumX, maximumX, minimumY, maximumY);
        }
    }

    private record AxisCompression(List<CoordinateRange> ranges) {
        static AxisCompression from(List<Long> coordinates, long minimum, long maximum) {
            TreeSet<Long> splitCoordinates = new TreeSet<>();
            splitCoordinates.add(minimum);
            splitCoordinates.add(maximum);

            for (long coordinate : coordinates) {
                for (long value = coordinate - 1; value <= coordinate + 1; value++) {
                    if (value >= minimum && value <= maximum) {
                        splitCoordinates.add(value);
                    }
                }
            }

            List<CoordinateRange> ranges = new ArrayList<>();
            long current = minimum;
            for (long coordinate : splitCoordinates) {
                if (coordinate < current) {
                    continue;
                }
                if (current < coordinate) {
                    ranges.add(new CoordinateRange(current, coordinate - 1));
                }
                ranges.add(new CoordinateRange(coordinate, coordinate));
                current = coordinate + 1;
            }

            if (current <= maximum) {
                ranges.add(new CoordinateRange(current, maximum));
            }

            ranges.sort(Comparator.comparingLong(CoordinateRange::minimum));
            return new AxisCompression(ranges);
        }

        int size() {
            return ranges.size();
        }

        CoordinateRange rangeAt(int index) {
            return ranges.get(index);
        }

        int indexOfSingleCoordinate(long coordinate) {
            for (int index = 0; index < ranges.size(); index++) {
                CoordinateRange range = ranges.get(index);
                if (range.minimum() == coordinate && range.maximum() == coordinate) {
                    return index;
                }
            }
            throw new IllegalArgumentException("Coordinate is not represented as a single range: " + coordinate);
        }
    }

    private record CoordinateRange(long minimum, long maximum) {
        boolean overlaps(long requestedMinimum, long requestedMaximum) {
            return maximum >= requestedMinimum && minimum <= requestedMaximum;
        }

        CoordinateRange clip(long requestedMinimum, long requestedMaximum) {
            long clippedMinimum = Math.max(minimum, requestedMinimum);
            long clippedMaximum = Math.min(maximum, requestedMaximum);
            if (clippedMinimum > clippedMaximum) {
                return null;
            }
            return new CoordinateRange(clippedMinimum, clippedMaximum);
        }
    }

    private record VerticalBand(long minimumY, long maximumY, List<HorizontalRange> horizontalRanges) {
        boolean overlaps(long requestedMinimumY, long requestedMaximumY) {
            return maximumY >= requestedMinimumY && minimumY <= requestedMaximumY;
        }

        boolean containsHorizontalRange(long minimumX, long maximumX) {
            return horizontalRanges.stream()
                    .anyMatch(range -> range.contains(minimumX, maximumX));
        }
    }

    private record HorizontalRange(long minimumX, long maximumX) {
        boolean contains(long requestedMinimumX, long requestedMaximumX) {
            return requestedMinimumX >= minimumX && requestedMaximumX <= maximumX;
        }
    }

    private record BlockPosition(int xIndex, int yIndex) {
        List<BlockPosition> neighbors() {
            return List.of(
                    new BlockPosition(xIndex + 1, yIndex),
                    new BlockPosition(xIndex - 1, yIndex),
                    new BlockPosition(xIndex, yIndex + 1),
                    new BlockPosition(xIndex, yIndex - 1)
            );
        }

        boolean isInside(int width, int height) {
            return xIndex >= 0 && xIndex < width && yIndex >= 0 && yIndex < height;
        }
    }
}
