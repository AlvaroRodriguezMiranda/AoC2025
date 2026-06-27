package aoc.day08.circuit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JunctionBoxTest {
    @Test
    void calculatesSquaredDistance() {
        JunctionBox first = new JunctionBox(1, 2, 3);
        JunctionBox second = new JunctionBox(4, 6, 3);

        assertEquals(25, first.squaredDistanceTo(second));
    }
}
