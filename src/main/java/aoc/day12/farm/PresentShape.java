package aoc.day12.farm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PresentShape {
    private final int index;
    private final List<GridCell> cells;

    public PresentShape(int index, List<GridCell> cells) {
        if (cells.isEmpty()) {
            throw new IllegalArgumentException("Present shape must contain at least one filled cell");
        }
        this.index = index;
        this.cells = List.copyOf(cells);
    }

    public int index() {
        return index;
    }

    public int area() {
        return cells.size();
    }

    public List<ShapeOrientation> orientations() {
        Set<String> seenOrientations = new HashSet<>();
        List<ShapeOrientation> orientations = new ArrayList<>();

        for (Transform transform : Transform.values()) {
            ShapeOrientation orientation = normalize(transform.apply(cells));
            if (seenOrientations.add(orientation.signature())) {
                orientations.add(orientation);
            }
        }

        return orientations;
    }

    private ShapeOrientation normalize(List<GridCell> transformedCells) {
        int minimumX = transformedCells.stream().mapToInt(GridCell::x).min().orElseThrow();
        int minimumY = transformedCells.stream().mapToInt(GridCell::y).min().orElseThrow();

        List<GridCell> normalizedCells = transformedCells.stream()
                .map(cell -> new GridCell(cell.x() - minimumX, cell.y() - minimumY))
                .sorted(Comparator.comparingInt(GridCell::y).thenComparingInt(GridCell::x))
                .toList();

        return new ShapeOrientation(normalizedCells);
    }

    private enum Transform {
        IDENTITY {
            @Override
            GridCell apply(GridCell cell) {
                return cell;
            }
        },
        ROTATE_90 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(-cell.y(), cell.x());
            }
        },
        ROTATE_180 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(-cell.x(), -cell.y());
            }
        },
        ROTATE_270 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(cell.y(), -cell.x());
            }
        },
        FLIP {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(-cell.x(), cell.y());
            }
        },
        FLIP_ROTATE_90 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(-cell.y(), -cell.x());
            }
        },
        FLIP_ROTATE_180 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(cell.x(), -cell.y());
            }
        },
        FLIP_ROTATE_270 {
            @Override
            GridCell apply(GridCell cell) {
                return new GridCell(cell.y(), cell.x());
            }
        };

        abstract GridCell apply(GridCell cell);

        List<GridCell> apply(List<GridCell> cells) {
            return cells.stream()
                    .map(this::apply)
                    .toList();
        }
    }
}
