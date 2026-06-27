package aoc.day02.input;

import aoc.day02.product.ProductIdRange;

import java.util.Arrays;
import java.util.List;

public final class ProductRangeParser {
    public List<ProductIdRange> parseRanges(String inputLine) {
        if (inputLine == null || inputLine.isBlank()) {
            throw new IllegalArgumentException("Input line cannot be empty");
        }

        String normalizedInput = inputLine.replaceAll("\\s+", "");
        return Arrays.stream(normalizedInput.split(","))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .map(this::parseRange)
                .toList();
    }

    public ProductIdRange parseRange(String text) {
        String[] parts = text.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Range must contain one dash: " + text);
        }

        long firstId = parseProductId(parts[0]);
        long lastId = parseProductId(parts[1]);
        return new ProductIdRange(firstId, lastId);
    }

    private long parseProductId(String text) {
        try {
            return Long.parseLong(text.trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Product ID must be a number: " + text, exception);
        }
    }
}
