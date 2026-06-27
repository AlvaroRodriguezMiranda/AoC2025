package aoc.day08;

import aoc.day08.circuit.CircuitSizeCalculator;
import aoc.day08.input.JunctionBoxParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaygroundPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOneUsingTenConnections() {
        JunctionBoxParser parser = new JunctionBoxParser();
        CircuitSizeCalculator calculator = new CircuitSizeCalculator();
        long result = calculator.multiplyThreeLargestCircuitSizes(parser.parse(officialExample()), 10);

        assertEquals(40, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        PlaygroundPuzzle puzzle = new PlaygroundPuzzle(new JunctionBoxParser(), new CircuitSizeCalculator());

        long result = puzzle.solvePartTwo(officialExample());

        assertEquals(25272, result);
    }

    private List<String> officialExample() {
        return List.of(
                "162,817,812",
                "57,618,57",
                "906,360,560",
                "592,479,940",
                "352,342,300",
                "466,668,158",
                "542,29,236",
                "431,825,988",
                "739,650,466",
                "52,470,668",
                "216,146,977",
                "819,987,18",
                "117,168,530",
                "805,96,715",
                "346,949,466",
                "970,615,88",
                "941,993,340",
                "862,61,35",
                "984,92,344",
                "425,690,689"
        );
    }
}
