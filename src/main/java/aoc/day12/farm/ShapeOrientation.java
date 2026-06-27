package aoc.day12.farm;

import java.util.List;

public final class ShapeOrientation {
    private final List<GridCell> cells;
    private final int width;
    private final int height;

    public ShapeOrientation(List<GridCell> cells) {
        this.cells = List.copyOf(cells);
        this.width = cells.stream().mapToInt(GridCell::x).max().orElseThrow() + 1;
        this.height = cells.stream().mapToInt(GridCell::y).max().orElseThrow() + 1;
    }

    public List<GridCell> cells() {
        return cells;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    String signature() {
        return cells.stream()
                .map(cell -> cell.x() + "," + cell.y())
                .reduce((first, second) -> first + ";" + second)
                .orElse("");
    }
}
