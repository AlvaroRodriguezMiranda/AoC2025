package aoc.day12.farm;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresentShapeTest {
    @Test
    void createsUniqueOrientationsWithRotationsAndFlips() {
        PresentShape shape = new PresentShape(0, List.of(
                new GridCell(0, 0),
                new GridCell(1, 0),
                new GridCell(0, 1)
        ));

        List<ShapeOrientation> orientations = shape.orientations();

        assertEquals(4, orientations.size());
        assertTrue(orientations.stream().anyMatch(orientation -> orientation.width() == 2 && orientation.height() == 2));
    }

    @Test
    void exposesShapeArea() {
        PresentShape shape = new PresentShape(0, List.of(
                new GridCell(0, 0),
                new GridCell(1, 0),
                new GridCell(0, 1)
        ));

        assertEquals(3, shape.area());
    }
}
