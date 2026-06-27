package aoc.day09.input;

import aoc.day09.theater.RedTile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RedTileParserTest {
    private final RedTileParser parser = new RedTileParser();

    @Test
    void parsesRedTileCoordinates() {
        List<RedTile> redTiles = parser.parse(List.of("7,1"));

        assertEquals(new RedTile(7, 1), redTiles.getFirst());
    }

    @Test
    void ignoresBlankLines() {
        List<RedTile> redTiles = parser.parse(List.of("", "11,7"));

        assertEquals(1, redTiles.size());
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("")));
    }

    @Test
    void rejectsPositionsWithoutTwoCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("1,2,3")));
    }

    @Test
    void rejectsNonNumericCoordinates() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("x,3")));
    }
}
