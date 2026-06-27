package aoc.day08.circuit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircuitSizeCalculatorTest {
    private final CircuitSizeCalculator calculator = new CircuitSizeCalculator();

    @Test
    void connectsClosestPairsAndMultipliesThreeLargestCircuits() {
        List<JunctionBox> junctionBoxes = List.of(
                new JunctionBox(0, 0, 0),
                new JunctionBox(1, 0, 0),
                new JunctionBox(10, 0, 0),
                new JunctionBox(11, 0, 0),
                new JunctionBox(30, 0, 0)
        );

        long result = calculator.multiplyThreeLargestCircuitSizes(junctionBoxes, 2);

        assertEquals(4, result);
    }

    @Test
    void repeatedConnectionsInsideSameCircuitDoNotChangeCircuitSizes() {
        List<JunctionBox> junctionBoxes = List.of(
                new JunctionBox(0, 0, 0),
                new JunctionBox(1, 0, 0),
                new JunctionBox(2, 0, 0),
                new JunctionBox(100, 0, 0),
                new JunctionBox(200, 0, 0)
        );

        long result = calculator.multiplyThreeLargestCircuitSizes(junctionBoxes, 3);

        assertEquals(3, result);
    }

    @Test
    void rejectsLessThanThreeJunctionBoxes() {
        assertThrows(IllegalArgumentException.class, () -> calculator.multiplyThreeLargestCircuitSizes(
                List.of(new JunctionBox(0, 0, 0), new JunctionBox(1, 0, 0)),
                1
        ));
    }

    @Test
    void returnsXCoordinateProductForConnectionThatCreatesSingleCircuit() {
        List<JunctionBox> junctionBoxes = List.of(
                new JunctionBox(2, 0, 0),
                new JunctionBox(3, 0, 0),
                new JunctionBox(10, 0, 0),
                new JunctionBox(11, 0, 0)
        );

        long result = calculator.multiplyLastConnectionXCoordinates(junctionBoxes);

        assertEquals(30, result);
    }

    @Test
    void rejectsLessThanTwoJunctionBoxesForPartTwo() {
        assertThrows(IllegalArgumentException.class, () -> calculator.multiplyLastConnectionXCoordinates(
                List.of(new JunctionBox(0, 0, 0))
        ));
    }
}
