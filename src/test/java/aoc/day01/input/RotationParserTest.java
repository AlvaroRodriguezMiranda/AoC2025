package aoc.day01.input;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.RotationDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RotationParserTest {
    private final RotationParser parser = new RotationParser();

    @Test
    void parsesRotationLine() {
        DialRotation rotation = parser.parseLine(" L68 ");

        assertEquals(RotationDirection.LEFT, rotation.direction());
        assertEquals(68, rotation.distance());
    }

    @Test
    void parsesLinesIgnoringBlankLines() {
        List<DialRotation> rotations = parser.parseLines(List.of("L68", "", " R48 "));

        assertEquals(2, rotations.size());
        assertEquals(RotationDirection.LEFT, rotations.get(0).direction());
        assertEquals(RotationDirection.RIGHT, rotations.get(1).direction());
    }

    @Test
    void rejectsUnknownDirection() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLine("X10"));
    }

    @Test
    void rejectsNonNumericDistance() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLine("Rabc"));
    }
}
