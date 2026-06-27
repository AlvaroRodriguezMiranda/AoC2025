package aoc.day10.factory;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MachineInitializerTest {
    private final MachineInitializer initializer = new MachineInitializer();

    @Test
    void returnsZeroWhenTargetAlreadyMatchesInitialState() {
        FactoryMachine machine = new FactoryMachine(3, 0, List.of(new ButtonWiring(1)));

        assertEquals(0, initializer.minimumPressesFor(machine));
    }

    @Test
    void findsFewestButtonPresses() {
        FactoryMachine machine = new FactoryMachine(4, 0b0110, List.of(
                new ButtonWiring(0b1000),
                new ButtonWiring(0b1010),
                new ButtonWiring(0b0100),
                new ButtonWiring(0b1100),
                new ButtonWiring(0b0101),
                new ButtonWiring(0b0011)
        ));

        assertEquals(2, initializer.minimumPressesFor(machine));
    }

    @Test
    void rejectsMachineWithoutButtons() {
        assertThrows(IllegalArgumentException.class, () -> new FactoryMachine(3, 1, List.of()));
    }

    @Test
    void findsFewestJoltageButtonPresses() {
        FactoryMachine machine = new FactoryMachine(4, 0, List.of(
                new ButtonWiring(0b1000),
                new ButtonWiring(0b1010),
                new ButtonWiring(0b0100),
                new ButtonWiring(0b1100),
                new ButtonWiring(0b0101),
                new ButtonWiring(0b0011)
        ), List.of(3, 5, 4, 7));

        assertEquals(10, initializer.minimumJoltagePressesFor(machine));
    }

    @Test
    void returnsZeroWhenJoltageRequirementsAreAlreadyZero() {
        FactoryMachine machine = new FactoryMachine(2, 0, List.of(new ButtonWiring(0b01)), List.of(0, 0));

        assertEquals(0, initializer.minimumJoltagePressesFor(machine));
    }
}
