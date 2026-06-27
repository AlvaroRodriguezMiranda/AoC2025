package aoc.day08.input;

import aoc.day08.circuit.JunctionBox;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JunctionBoxParserTest {
    private final JunctionBoxParser parser = new JunctionBoxParser();

    @Test
    void parsesJunctionBoxCoordinates() {
        List<JunctionBox> junctionBoxes = parser.parse(List.of("162,817,812"));

        assertEquals(new JunctionBox(162, 817, 812), junctionBoxes.getFirst());
    }

    @Test
    void ignoresBlankLines() {
        List<JunctionBox> junctionBoxes = parser.parse(List.of("", "1,2,3"));

        assertEquals(1, junctionBoxes.size());
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("")));
    }

    @Test
    void rejectsPositionsWithoutThreeCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("1,2")));
    }

    @Test
    void rejectsNonNumericCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("1,x,3")));
    }
}
