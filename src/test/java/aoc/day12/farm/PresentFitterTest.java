package aoc.day12.farm;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresentFitterTest {
    private final PresentFitter presentFitter = new PresentFitter();

    @Test
    void fitsTwoRotatedPresentsInFourByFourRegion() {
        PresentShape shape = new PresentShape(0, List.of(
                new GridCell(0, 0),
                new GridCell(1, 0),
                new GridCell(2, 0),
                new GridCell(0, 1),
                new GridCell(0, 2),
                new GridCell(1, 2),
                new GridCell(2, 2)
        ));

        TreeRegion region = new TreeRegion(4, 4, List.of(2));

        assertTrue(presentFitter.canFit(List.of(shape), region));
    }

    @Test
    void rejectsRegionWhenRequiredAreaIsTooLarge() {
        PresentShape shape = new PresentShape(0, List.of(
                new GridCell(0, 0),
                new GridCell(1, 0),
                new GridCell(0, 1)
        ));

        TreeRegion region = new TreeRegion(2, 2, List.of(2));

        assertFalse(presentFitter.canFit(List.of(shape), region));
    }

    @Test
    void rejectsShapeThatCannotBePlacedInsideRegion() {
        PresentShape shape = new PresentShape(0, List.of(
                new GridCell(0, 0),
                new GridCell(1, 0),
                new GridCell(2, 0)
        ));

        TreeRegion region = new TreeRegion(2, 2, List.of(1));

        assertFalse(presentFitter.canFit(List.of(shape), region));
    }
}
