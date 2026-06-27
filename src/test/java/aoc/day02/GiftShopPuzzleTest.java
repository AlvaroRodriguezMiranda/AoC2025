package aoc.day02;

import aoc.day02.input.ProductRangeParser;
import aoc.day02.product.ProductIdSumCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GiftShopPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        GiftShopPuzzle puzzle = new GiftShopPuzzle(new ProductRangeParser(), new ProductIdSumCalculator());

        long result = puzzle.solvePartOne("""
                11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
                1698522-1698528,446443-446449,38593856-38593862,565653-565659,
                824824821-824824827,2121212118-2121212124
                """);

        assertEquals(1227775554L, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        GiftShopPuzzle puzzle = new GiftShopPuzzle(new ProductRangeParser(), new ProductIdSumCalculator());

        long result = puzzle.solvePartTwo("""
                11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
                1698522-1698528,446443-446449,38593856-38593862,565653-565659,
                824824821-824824827,2121212118-2121212124
                """);

        assertEquals(4174379265L, result);
    }
}
