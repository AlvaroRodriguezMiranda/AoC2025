package aoc.day11.reactor;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PathCounterTest {
    private final PathCounter pathCounter = new PathCounter();

    @Test
    void countsSinglePathToOutput() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "you", List.of("a"),
                "a", List.of("out")
        ));

        assertEquals(1, pathCounter.countPaths(network, "you", "out"));
    }

    @Test
    void countsBranchingPaths() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "you", List.of("a", "b"),
                "a", List.of("out"),
                "b", List.of("out")
        ));

        assertEquals(2, pathCounter.countPaths(network, "you", "out"));
    }

    @Test
    void rejectsMissingStartDevice() {
        DeviceNetwork network = new DeviceNetwork(Map.of("a", List.of("out")));

        assertThrows(IllegalArgumentException.class, () -> pathCounter.countPaths(network, "you", "out"));
    }

    @Test
    void rejectsCycles() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "you", List.of("a"),
                "a", List.of("you")
        ));

        assertThrows(IllegalStateException.class, () -> pathCounter.countPaths(network, "you", "out"));
    }

    @Test
    void countsPathsThatVisitTwoRequiredDevices() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "svr", List.of("dac"),
                "dac", List.of("fft"),
                "fft", List.of("out")
        ));

        assertEquals(1, pathCounter.countPathsVisitingBoth(network, "svr", "out", "dac", "fft"));
    }

    @Test
    void countsRequiredDevicesInTheOtherOrder() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "svr", List.of("fft"),
                "fft", List.of("dac"),
                "dac", List.of("out")
        ));

        assertEquals(1, pathCounter.countPathsVisitingBoth(network, "svr", "out", "dac", "fft"));
    }

    @Test
    void ignoresPathsMissingOneRequiredDevice() {
        DeviceNetwork network = new DeviceNetwork(Map.of(
                "svr", List.of("dac", "fft"),
                "dac", List.of("out"),
                "fft", List.of("out")
        ));

        assertEquals(0, pathCounter.countPathsVisitingBoth(network, "svr", "out", "dac", "fft"));
    }
}
