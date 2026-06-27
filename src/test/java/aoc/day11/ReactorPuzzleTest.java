package aoc.day11;

import aoc.day11.input.DeviceNetworkParser;
import aoc.day11.reactor.PathCounter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReactorPuzzleTest {
    @Test
    void solvesOfficialExampleForPartOne() {
        ReactorPuzzle puzzle = new ReactorPuzzle(new DeviceNetworkParser(), new PathCounter());

        long result = puzzle.solvePartOne(officialExample());

        assertEquals(5, result);
    }

    @Test
    void solvesOfficialExampleForPartTwo() {
        ReactorPuzzle puzzle = new ReactorPuzzle(new DeviceNetworkParser(), new PathCounter());

        long result = puzzle.solvePartTwo(partTwoOfficialExample());

        assertEquals(2, result);
    }

    private List<String> officialExample() {
        return List.of(
                "aaa: you hhh",
                "you: bbb ccc",
                "bbb: ddd eee",
                "ccc: ddd eee fff",
                "ddd: ggg",
                "eee: out",
                "fff: out",
                "ggg: out",
                "hhh: ccc fff iii",
                "iii: out"
        );
    }

    private List<String> partTwoOfficialExample() {
        return List.of(
                "svr: aaa bbb",
                "aaa: fft",
                "fft: ccc",
                "bbb: tty",
                "tty: ccc",
                "ccc: ddd eee",
                "ddd: hub",
                "hub: fff",
                "eee: dac",
                "dac: fff",
                "fff: ggg hhh",
                "ggg: out",
                "hhh: out"
        );
    }
}
