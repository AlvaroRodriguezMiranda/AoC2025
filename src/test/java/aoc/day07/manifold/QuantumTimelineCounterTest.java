package aoc.day07.manifold;

import aoc.day07.input.TachyonManifoldParser;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuantumTimelineCounterTest {
    private final TachyonManifoldParser parser = new TachyonManifoldParser();
    private final QuantumTimelineCounter counter = new QuantumTimelineCounter();

    @Test
    void keepsOneTimelineWhenNoSplitterIsReached() {
        TachyonManifold manifold = parser.parse(List.of(
                ".S.",
                "...",
                "..."
        ));

        assertEquals(BigInteger.ONE, counter.countTimelines(manifold));
    }

    @Test
    void createsTwoTimelinesWhenParticleHitsOneSplitter() {
        TachyonManifold manifold = parser.parse(List.of(
                ".S.",
                ".^.",
                "..."
        ));

        assertEquals(BigInteger.valueOf(2), counter.countTimelines(manifold));
    }

    @Test
    void addsDifferentTimelinesThatReachTheSameColumn() {
        TachyonManifold manifold = parser.parse(List.of(
                "..S..",
                "..^..",
                ".^.^.",
                "....."
        ));

        assertEquals(BigInteger.valueOf(4), counter.countTimelines(manifold));
    }
}
