package aoc.day07.input;

import aoc.day07.manifold.TachyonManifold;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TachyonManifoldParserTest {
    private final TachyonManifoldParser parser = new TachyonManifoldParser();

    @Test
    void parsesStartPositionAndSplitters() {
        TachyonManifold manifold = parser.parse(List.of(
                "..S",
                ".^.",
                "..."
        ));

        assertEquals(3, manifold.height());
        assertEquals(3, manifold.width());
        assertEquals(0, manifold.startPosition().row());
        assertEquals(2, manifold.startPosition().column());
        assertTrue(manifold.hasSplitterAt(1, 1));
    }

    @Test
    void rejectsDiagramWithoutStartPosition() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("...")));
    }

    @Test
    void rejectsMoreThanOneStartPosition() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("S.S")));
    }

    @Test
    void rejectsNonRectangularDiagram() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("S..", "..")));
    }

    @Test
    void rejectsUnknownCells() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("Sx.")));
    }
}
