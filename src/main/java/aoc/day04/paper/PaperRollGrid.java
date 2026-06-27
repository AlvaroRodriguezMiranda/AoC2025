package aoc.day04.paper;

import java.util.ArrayList;
import java.util.List;

public final class PaperRollGrid {
    private final List<String> rows;

    public PaperRollGrid(List<String> rows) {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Paper roll grid cannot be empty");
        }
        int width = rows.getFirst().length();
        if (width == 0) {
            throw new IllegalArgumentException("Paper roll grid rows cannot be empty");
        }
        for (String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException("Paper roll grid must be rectangular");
            }
        }
        this.rows = List.copyOf(rows);
    }

    public int height() {
        return rows.size();
    }

    public int width() {
        return rows.getFirst().length();
    }

    public boolean hasPaperRollAt(GridPosition position) {
        return isInside(position) && rows.get(position.row()).charAt(position.column()) == '@';
    }

    public int adjacentPaperRolls(GridPosition position) {
        int adjacentRolls = 0;

        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset != 0 || columnOffset != 0) {
                    GridPosition adjacentPosition = new GridPosition(
                            position.row() + rowOffset,
                            position.column() + columnOffset
                    );
                    if (hasPaperRollAt(adjacentPosition)) {
                        adjacentRolls++;
                    }
                }
            }
        }

        return adjacentRolls;
    }

    public List<GridPosition> paperRollPositions() {
        List<GridPosition> positions = new ArrayList<>();

        for (int row = 0; row < height(); row++) {
            for (int column = 0; column < width(); column++) {
                GridPosition position = new GridPosition(row, column);
                if (hasPaperRollAt(position)) {
                    positions.add(position);
                }
            }
        }

        return positions;
    }

    public PaperRollGrid withoutPaperRolls(List<GridPosition> positionsToRemove) {
        List<StringBuilder> mutableRows = rows.stream()
                .map(StringBuilder::new)
                .toList();

        for (GridPosition position : positionsToRemove) {
            if (hasPaperRollAt(position)) {
                mutableRows.get(position.row()).setCharAt(position.column(), '.');
            }
        }

        List<String> updatedRows = mutableRows.stream()
                .map(StringBuilder::toString)
                .toList();
        return new PaperRollGrid(updatedRows);
    }

    private boolean isInside(GridPosition position) {
        return position.row() >= 0
                && position.row() < height()
                && position.column() >= 0
                && position.column() < width();
    }
}
