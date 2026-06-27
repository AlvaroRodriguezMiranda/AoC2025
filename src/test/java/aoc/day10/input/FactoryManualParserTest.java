package aoc.day10.input;

import aoc.day10.factory.FactoryMachine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FactoryManualParserTest {
    private final FactoryManualParser parser = new FactoryManualParser();

    @Test
    void parsesIndicatorDiagramAndButtons() {
        List<FactoryMachine> machines = parser.parse(List.of("[.##.] (3) (0,2) {3,5,4,7}"));

        FactoryMachine machine = machines.getFirst();
        assertEquals(4, machine.lightCount());
        assertEquals(0b0110, machine.targetMask());
        assertEquals(2, machine.buttons().size());
        assertEquals(0b1000, machine.buttons().get(0).toggleMask());
        assertEquals(0b0101, machine.buttons().get(1).toggleMask());
        assertEquals(List.of(3, 5, 4, 7), machine.joltageRequirements());
    }

    @Test
    void ignoresBlankLines() {
        List<FactoryMachine> machines = parser.parse(List.of("", "[#] (0) {1}"));

        assertEquals(1, machines.size());
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("")));
    }

    @Test
    void rejectsLineWithoutDiagram() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("(0) {1}")));
    }

    @Test
    void rejectsButtonIndexOutsideDiagram() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("[#] (1) {1}")));
    }

    @Test
    void rejectsJoltageRequirementCountDifferentFromDiagram() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(List.of("[##] (0) {1}")));
    }
}
