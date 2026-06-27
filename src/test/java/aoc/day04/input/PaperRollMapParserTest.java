package aoc.day04.input;

import aoc.day04.paper.PaperRollGrid;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaperRollMapParserTest {
    private final PaperRollMapParser parser = new PaperRollMapParser();

    @Test
    void parsesPaperRollMapIgnoringBlankLines() {
        PaperRollGrid grid = parser.parseLines(List.of(".@", "", "@."));

        assertEquals(2, grid.height());
        assertEquals(2, grid.width());
    }

    @Test
    void rejectsUnknownCharacter() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLines(List.of(".#", "@.")));
    }

    @Test
    void rejectsNonRectangularMap() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLines(List.of(".@", "@..")));
    }
}
