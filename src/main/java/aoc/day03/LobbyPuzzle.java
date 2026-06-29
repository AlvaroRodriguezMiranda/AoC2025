package aoc.day03;

import aoc.day03.battery.BatteryBank;
import aoc.day03.input.BatteryBankParser;
import aoc.day03.solver.JoltageSolver;
import aoc.day03.solver.TwelveBatteryJoltageSolver;
import aoc.day03.solver.TwoBatteryJoltageSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class LobbyPuzzle {
    private final BatteryBankParser batteryBankParser;
    private final JoltageSolver partOneSolver;
    private final JoltageSolver partTwoSolver;

    public LobbyPuzzle(BatteryBankParser batteryBankParser, JoltageSolver partOneSolver, JoltageSolver partTwoSolver) {
        this.batteryBankParser = batteryBankParser;
        this.partOneSolver = partOneSolver;
        this.partTwoSolver = partTwoSolver;
    }

    public int solvePartOne(List<String> inputLines) {
        List<BatteryBank> batteryBanks = batteryBankParser.parseLines(inputLines);
        return Math.toIntExact(partOneSolver.solve(batteryBanks));
    }

    public long solvePartTwo(List<String> inputLines) {
        List<BatteryBank> batteryBanks = batteryBankParser.parseLines(inputLines);
        return partTwoSolver.solve(batteryBanks);
    }

    public static void main(String[] args) throws IOException {
        LobbyPuzzle puzzle = new LobbyPuzzle(
                new BatteryBankParser(),
                new TwoBatteryJoltageSolver(),
                new TwelveBatteryJoltageSolver()
        );
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day03/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
