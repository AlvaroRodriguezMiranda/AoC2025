package aoc.day01.input;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.RotationDirection;

import java.util.List;

public final class RotationParser {
    public List<DialRotation> parseLines(List<String> lines) {
        return lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::parseLine)
                .toList();
    }

    public DialRotation parseLine(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Rotation line must contain a direction and a distance");
        }

        String trimmedLine = line.trim();
        if (trimmedLine.length() < 2) {
            throw new IllegalArgumentException("Rotation line must contain a direction and a distance");
        }

        RotationDirection direction = RotationDirection.fromSymbol(trimmedLine.charAt(0));
        int distance = parseDistance(trimmedLine.substring(1));
        return new DialRotation(direction, distance);
    }

    private int parseDistance(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Rotation distance must be a number: " + text, exception);
        }
    }
}
