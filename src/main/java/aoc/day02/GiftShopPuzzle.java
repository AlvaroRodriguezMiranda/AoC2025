package aoc.day02;

import aoc.day02.input.ProductRangeParser;
import aoc.day02.product.ProductIdRange;
import aoc.day02.solver.DoublePatternInvalidProductIdRule;
import aoc.day02.solver.ProductIdSumCalculator;
import aoc.day02.solver.RepeatedPatternInvalidProductIdRule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class GiftShopPuzzle {
    private final ProductRangeParser productRangeParser;
    private final ProductIdSumCalculator partOneCalculator;
    private final ProductIdSumCalculator partTwoCalculator;

    public GiftShopPuzzle(
            ProductRangeParser productRangeParser,
            ProductIdSumCalculator partOneCalculator,
            ProductIdSumCalculator partTwoCalculator
    ) {
        this.productRangeParser = productRangeParser;
        this.partOneCalculator = partOneCalculator;
        this.partTwoCalculator = partTwoCalculator;
    }

    public long solvePartOne(String inputLine) {
        List<ProductIdRange> ranges = productRangeParser.parseRanges(inputLine);
        return partOneCalculator.sumInvalidProductIds(ranges);
    }

    public long solvePartTwo(String inputLine) {
        List<ProductIdRange> ranges = productRangeParser.parseRanges(inputLine);
        return partTwoCalculator.sumInvalidProductIds(ranges);
    }

    public static void main(String[] args) throws IOException {
        GiftShopPuzzle puzzle = new GiftShopPuzzle(
                new ProductRangeParser(),
                new ProductIdSumCalculator(new DoublePatternInvalidProductIdRule()),
                new ProductIdSumCalculator(new RepeatedPatternInvalidProductIdRule())
        );
        String inputLine = Files.readString(Path.of("src/main/resources/day02/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLine));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLine));
    }
}
