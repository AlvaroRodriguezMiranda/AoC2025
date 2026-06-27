package aoc.day07.input;

import aoc.day07.manifold.GridPosition;
import aoc.day07.manifold.TachyonManifold;

import java.util.ArrayList;
import java.util.List;

public final class TachyonManifoldParser {
    public TachyonManifold parse(List<String> lines) {
        List<String> diagramLines = lines.stream()
                .filter(line -> !line.isBlank())
                .toList();

        if (diagramLines.isEmpty()) {
            throw new IllegalArgumentException("Tachyon manifold cannot be empty");
        }

        int width = diagramLines.getFirst().length();
        List<List<Character>> cells = new ArrayList<>();
        GridPosition startPosition = null;

        for (int row = 0; row < diagramLines.size(); row++) {
            String line = diagramLines.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException("Tachyon manifold must be rectangular");
            }

            List<Character> rowCells = new ArrayList<>();
            for (int column = 0; column < line.length(); column++) {
                char cell = line.charAt(column);
                validateCell(cell);
                if (cell == 'S') {
                    if (startPosition != null) {
                        throw new IllegalArgumentException("Tachyon manifold must contain only one start position");
                    }
                    startPosition = new GridPosition(row, column);
                }
                rowCells.add(cell);
            }
            cells.add(rowCells);
        }

        if (startPosition == null) {
            throw new IllegalArgumentException("Tachyon manifold must contain a start position");
        }

        return new TachyonManifold(cells, startPosition);
    }

    private void validateCell(char cell) {
        if (cell != '.' && cell != '^' && cell != 'S') {
            throw new IllegalArgumentException("Unknown tachyon manifold cell: " + cell);
        }
    }
}
