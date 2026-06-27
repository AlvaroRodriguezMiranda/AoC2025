package aoc.day11.input;

import aoc.day11.reactor.DeviceNetwork;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeviceNetworkParserTest {
    private final DeviceNetworkParser parser = new DeviceNetworkParser();

    @Test
    void parsesDeviceOutputs() {
        DeviceNetwork network = parser.parse(List.of("you: bbb ccc"));

        assertEquals(List.of("bbb", "ccc"), network.outputsFrom("you"));
    }

    @Test
    void ignoresBlankLines() {
        DeviceNetwork network = parser.parse(List.of("", "you: out"));

        assertEquals(List.of("out"), network.outputsFrom("you"));
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("")));
    }

    @Test
    void rejectsLineWithoutSingleColon() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("you out")));
    }

    @Test
    void rejectsDuplicatedDeviceDefinitions() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("you: a", "you: b")));
    }
}
