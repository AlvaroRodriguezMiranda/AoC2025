package aoc.day07.manifold;

import aoc.day07.input.TachyonManifoldParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TachyonSplitterCounterTest {
    private final TachyonManifoldParser parser = new TachyonManifoldParser();
    private final TachyonSplitterCounter counter = new TachyonSplitterCounter();

    @Test
    void countsNoSplitsWhenBeamDoesNotHitSplitter() {
        TachyonManifold manifold = parser.parse(List.of(
                ".S.",
                "...",
                "..."
        ));

        assertEquals(0, counter.countSplits(manifold));
    }

    @Test
    void countsOneSplitWhenBeamHitsSplitter() {
        TachyonManifold manifold = parser.parse(List.of(
                ".S.",
                ".^.",
                "..."
        ));

        assertEquals(1, counter.countSplits(manifold));
    }

    @Test
    void mergesBeamsThatReachTheSameColumn() {
        TachyonManifold manifold = parser.parse(List.of(
                "..S..",
                "..^..",
                ".^.^.",
                "....."
        ));

        assertEquals(3, counter.countSplits(manifold));
    }
}
