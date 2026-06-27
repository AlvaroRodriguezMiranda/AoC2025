package aoc.day08.input;

import aoc.day08.circuit.JunctionBox;

import java.util.List;

public final class JunctionBoxParser {
    public List<JunctionBox> parse(List<String> lines) {
        List<JunctionBox> junctionBoxes = lines.stream()
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .toList();

        if (junctionBoxes.isEmpty()) {
            throw new IllegalArgumentException("Junction box list cannot be empty");
        }

        return junctionBoxes;
    }

    private JunctionBox parseLine(String line) {
        String[] coordinates = line.split(",");
        if (coordinates.length != 3) {
            throw new IllegalArgumentException("Junction box position must have three coordinates: " + line);
        }

        return new JunctionBox(
                parseCoordinate(coordinates[0]),
                parseCoordinate(coordinates[1]),
                parseCoordinate(coordinates[2])
        );
    }

    private long parseCoordinate(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Coordinate must be a number: " + text, exception);
        }
    }
}
