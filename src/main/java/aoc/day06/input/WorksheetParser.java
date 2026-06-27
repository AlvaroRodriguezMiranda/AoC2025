package aoc.day06.input;

import aoc.day06.worksheet.MathOperation;
import aoc.day06.worksheet.MathProblem;

import java.util.ArrayList;
import java.util.List;

public final class WorksheetParser {
    public List<MathProblem> parseLines(List<String> lines) {
        List<String> paddedLines = padLines(lines);
        List<ColumnRange> problemColumns = findProblemColumns(paddedLines, paddedLines.getFirst().length());
        return problemColumns.stream()
                .map(columns -> parseProblemTopToBottom(paddedLines, columns))
                .toList();
    }

    public List<MathProblem> parseRightToLeftLines(List<String> lines) {
        List<String> paddedLines = padLines(lines);
        List<ColumnRange> problemColumns = findProblemColumns(paddedLines, paddedLines.getFirst().length());
        return problemColumns.stream()
                .map(columns -> parseProblemRightToLeft(paddedLines, columns))
                .toList();
    }

    private List<String> padLines(List<String> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Worksheet cannot be empty");
        }

        int width = lines.stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();
        List<String> paddedLines = lines.stream()
                .map(line -> padRight(line, width))
                .toList();
        return paddedLines;
    }

    private List<ColumnRange> findProblemColumns(List<String> lines, int width) {
        List<ColumnRange> columnRanges = new ArrayList<>();
        int startColumn = -1;

        for (int column = 0; column < width; column++) {
            boolean emptyColumn = isEmptyColumn(lines, column);
            if (!emptyColumn && startColumn == -1) {
                startColumn = column;
            } else if (emptyColumn && startColumn != -1) {
                columnRanges.add(new ColumnRange(startColumn, column));
                startColumn = -1;
            }
        }

        if (startColumn != -1) {
            columnRanges.add(new ColumnRange(startColumn, width));
        }

        return columnRanges;
    }

    private MathProblem parseProblemTopToBottom(List<String> lines, ColumnRange columns) {
        List<Long> numbers = new ArrayList<>();
        MathOperation operation = null;

        for (String line : lines) {
            String text = line.substring(columns.startColumn(), columns.endColumn()).trim();
            if (!text.isEmpty()) {
                if (text.equals("+") || text.equals("*")) {
                    operation = MathOperation.fromSymbol(text.charAt(0));
                } else {
                    numbers.add(parseNumber(text));
                }
            }
        }

        if (operation == null) {
            throw new IllegalArgumentException("Math problem must contain an operation");
        }

        return new MathProblem(numbers, operation);
    }

    private MathProblem parseProblemRightToLeft(List<String> lines, ColumnRange columns) {
        List<Long> numbers = new ArrayList<>();
        MathOperation operation = null;

        for (int column = columns.endColumn() - 1; column >= columns.startColumn(); column--) {
            StringBuilder number = new StringBuilder();
            for (String line : lines) {
                char character = line.charAt(column);
                if (Character.isDigit(character)) {
                    number.append(character);
                } else if (character == '+' || character == '*') {
                    operation = MathOperation.fromSymbol(character);
                } else if (character != ' ') {
                    throw new IllegalArgumentException("Unexpected worksheet character: " + character);
                }
            }

            if (!number.isEmpty()) {
                numbers.add(parseNumber(number.toString()));
            }
        }

        if (operation == null) {
            throw new IllegalArgumentException("Math problem must contain an operation");
        }

        return new MathProblem(numbers, operation);
    }

    private boolean isEmptyColumn(List<String> lines, int column) {
        return lines.stream()
                .allMatch(line -> line.charAt(column) == ' ');
    }

    private String padRight(String line, int width) {
        return line + " ".repeat(width - line.length());
    }

    private long parseNumber(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Problem value must be a number: " + text, exception);
        }
    }

    private record ColumnRange(int startColumn, int endColumn) {
    }
}
