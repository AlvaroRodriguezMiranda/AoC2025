package aoc.day04.input;

import aoc.day04.paper.PaperRollGrid;

import java.util.List;

public final class PaperRollMapParser {
    public PaperRollGrid parseLines(List<String> lines) {
        List<String> rows = lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .peek(this::validateLine)
                .toList();
        return new PaperRollGrid(rows);
    }

    private void validateLine(String line) {
        for (char character : line.toCharArray()) {
            if (character != '@' && character != '.') {
                throw new IllegalArgumentException("Paper roll map can only contain @ and . characters");
            }
        }
    }
}
