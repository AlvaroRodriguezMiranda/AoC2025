package aoc.day09.theater;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LargestRectangleFinderTest {
    private final LargestRectangleFinder finder = new LargestRectangleFinder();

    @Test
    void findsLargestRectangleFromSeveralRedTiles() {
        long result = finder.findLargestArea(List.of(
                new RedTile(2, 5),
                new RedTile(9, 7),
                new RedTile(7, 1),
                new RedTile(11, 7)
        ));

        assertEquals(35, result);
    }

    @Test
    void supportsThinHorizontalRectangles() {
        long result = finder.findLargestArea(List.of(
                new RedTile(7, 3),
                new RedTile(2, 3)
        ));

        assertEquals(6, result);
    }

    @Test
    void rejectsLessThanTwoRedTiles() {
        assertThrows(IllegalArgumentException.class, () -> finder.findLargestArea(List.of(new RedTile(1, 1))));
    }

    @Test
    void findsLargestRectangleInsideRedGreenArea() {
        long result = finder.findLargestAreaInsideRedGreenArea(List.of(
                new RedTile(7, 1),
                new RedTile(11, 1),
                new RedTile(11, 7),
                new RedTile(9, 7),
                new RedTile(9, 5),
                new RedTile(2, 5),
                new RedTile(2, 3),
                new RedTile(7, 3)
        ));

        assertEquals(24, result);
    }

    @Test
    void rejectsLessThanTwoRedTilesForRedGreenArea() {
        assertThrows(IllegalArgumentException.class, () -> finder.findLargestAreaInsideRedGreenArea(List.of(new RedTile(1, 1))));
    }
}
