package aoc.day12.input;

import aoc.day12.farm.FarmSummary;
import aoc.day12.farm.GridCell;
import aoc.day12.farm.PresentShape;
import aoc.day12.farm.TreeRegion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FarmSummaryParser {
    public FarmSummary parse(List<String> lines) {
        List<PresentShape> shapes = new ArrayList<>();
        List<TreeRegion> regions = new ArrayList<>();
        int lineIndex = 0;

        while (lineIndex < lines.size()) {
            String line = lines.get(lineIndex).trim();
            if (line.isBlank()) {
                lineIndex++;
            } else if (line.endsWith(":")) {
                ParsedShape parsedShape = parseShape(lines, lineIndex);
                shapes.add(parsedShape.shape());
                lineIndex = parsedShape.nextLineIndex();
            } else {
                break;
            }
        }

        while (lineIndex < lines.size()) {
            String line = lines.get(lineIndex).trim();
            if (!line.isBlank()) {
                regions.add(parseRegion(line, shapes.size()));
            }
            lineIndex++;
        }

        shapes.sort(Comparator.comparingInt(PresentShape::index));
        validateShapeIndexes(shapes);
        return new FarmSummary(shapes, regions);
    }

    private ParsedShape parseShape(List<String> lines, int headerIndex) {
        String header = lines.get(headerIndex).trim();
        int shapeIndex = parseShapeIndex(header.substring(0, header.length() - 1));
        List<GridCell> cells = new ArrayList<>();
        int lineIndex = headerIndex + 1;
        int width = -1;

        while (lineIndex < lines.size() && !lines.get(lineIndex).isBlank()) {
            String row = lines.get(lineIndex).trim();
            if (width == -1) {
                width = row.length();
            } else if (row.length() != width) {
                throw new IllegalArgumentException("Present shape rows must have the same width");
            }

            for (int x = 0; x < row.length(); x++) {
                char cell = row.charAt(x);
                if (cell == '#') {
                    cells.add(new GridCell(x, lineIndex - headerIndex - 1));
                } else if (cell != '.') {
                    throw new IllegalArgumentException("Unknown present shape cell: " + cell);
                }
            }
            lineIndex++;
        }

        return new ParsedShape(new PresentShape(shapeIndex, cells), lineIndex);
    }

    private TreeRegion parseRegion(String line, int shapeCount) {
        String[] parts = line.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Region line must contain one colon: " + line);
        }

        String[] dimensions = parts[0].trim().split("x");
        if (dimensions.length != 2) {
            throw new IllegalArgumentException("Region dimensions must use WIDTHxLENGTH: " + line);
        }

        List<Integer> presentCounts = parsePresentCounts(parts[1].trim());
        if (presentCounts.size() != shapeCount) {
            throw new IllegalArgumentException("Region present count must match the number of shapes: " + line);
        }

        return new TreeRegion(parsePositiveInteger(dimensions[0]), parsePositiveInteger(dimensions[1]), presentCounts);
    }

    private List<Integer> parsePresentCounts(String text) {
        List<Integer> counts = new ArrayList<>();
        for (String countText : text.split("\\s+")) {
            if (!countText.isBlank()) {
                counts.add(parseNonNegativeInteger(countText));
            }
        }
        return counts;
    }

    private int parseShapeIndex(String text) {
        return parseNonNegativeInteger(text.trim());
    }

    private int parsePositiveInteger(String text) {
        int value = parseNonNegativeInteger(text.trim());
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive: " + text);
        }
        return value;
    }

    private int parseNonNegativeInteger(String text) {
        try {
            int value = Integer.parseInt(text);
            if (value < 0) {
                throw new IllegalArgumentException("Value cannot be negative: " + text);
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Value must be a number: " + text, exception);
        }
    }

    private void validateShapeIndexes(List<PresentShape> shapes) {
        if (shapes.isEmpty()) {
            throw new IllegalArgumentException("Farm summary must contain present shapes");
        }
        for (int index = 0; index < shapes.size(); index++) {
            if (shapes.get(index).index() != index) {
                throw new IllegalArgumentException("Present shape indexes must start at 0 and be consecutive");
            }
        }
    }

    private record ParsedShape(PresentShape shape, int nextLineIndex) {
    }
}
