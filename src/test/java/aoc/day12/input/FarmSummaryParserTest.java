package aoc.day12.input;

import aoc.day12.farm.FarmSummary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FarmSummaryParserTest {
    private final FarmSummaryParser parser = new FarmSummaryParser();

    @Test
    void parsesPresentShapesAndTreeRegions() {
        FarmSummary summary = parser.parse(List.of(
                "0:",
                "##",
                ".#",
                "",
                "1:",
                "#",
                "",
                "4x3: 2 1"
        ));

        assertEquals(2, summary.shapes().size());
        assertEquals(1, summary.regions().size());
        assertEquals(3, summary.shapes().get(0).area());
        assertEquals(4, summary.regions().getFirst().width());
        assertEquals(3, summary.regions().getFirst().length());
        assertEquals(List.of(2, 1), summary.regions().getFirst().presentCounts());
    }

    @Test
    void rejectsUnknownShapeCells() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of(
                "0:",
                "#A",
                "",
                "2x2: 1"
        )));
    }

    @Test
    void rejectsRegionWithDifferentNumberOfPresentCounts() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of(
                "0:",
                "#",
                "",
                "2x2: 1 0"
        )));
    }
}
