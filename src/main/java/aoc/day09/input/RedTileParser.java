package aoc.day09.input;

import aoc.day09.theater.RedTile;

import java.util.List;

public final class RedTileParser {
    public List<RedTile> parse(List<String> lines) {
        List<RedTile> redTiles = lines.stream()
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .toList();

        if (redTiles.isEmpty()) {
            throw new IllegalArgumentException("Red tile list cannot be empty");
        }

        return redTiles;
    }

    private RedTile parseLine(String line) {
        String[] coordinates = line.split(",");
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("Red tile position must have two coordinates: " + line);
        }

        return new RedTile(parseCoordinate(coordinates[0]), parseCoordinate(coordinates[1]));
    }

    private long parseCoordinate(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Coordinate must be a number: " + text, exception);
        }
    }
}
