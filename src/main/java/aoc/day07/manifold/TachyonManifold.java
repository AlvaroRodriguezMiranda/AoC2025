package aoc.day07.manifold;

import java.util.List;

public final class TachyonManifold {
    private final List<List<Character>> cells;
    private final GridPosition startPosition;

    public TachyonManifold(List<List<Character>> cells, GridPosition startPosition) {
        this.cells = cells.stream()
                .map(List::copyOf)
                .toList();
        this.startPosition = startPosition;
    }

    public int height() {
        return cells.size();
    }

    public int width() {
        return cells.getFirst().size();
    }

    public GridPosition startPosition() {
        return startPosition;
    }

    public boolean contains(int row, int column) {
        return row >= 0 && row < height() && column >= 0 && column < width();
    }

    public boolean hasSplitterAt(int row, int column) {
        return contains(row, column) && cells.get(row).get(column) == '^';
    }
}
