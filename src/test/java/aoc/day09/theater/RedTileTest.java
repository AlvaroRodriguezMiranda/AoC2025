package aoc.day09.theater;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedTileTest {
    @Test
    void calculatesInclusiveRectangleArea() {
        RedTile first = new RedTile(2, 5);
        RedTile second = new RedTile(9, 7);

        assertEquals(24, first.rectangleAreaWith(second));
    }
}
