package aoc.day03;

import aoc.day03.battery.BatteryBank;
import aoc.day03.battery.JoltageCalculator;
import aoc.day03.input.BatteryBankParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class LobbyPuzzle {
    private final BatteryBankParser batteryBankParser;
    private final JoltageCalculator joltageCalculator;

    public LobbyPuzzle(BatteryBankParser batteryBankParser, JoltageCalculator joltageCalculator) {
        this.batteryBankParser = batteryBankParser;
        this.joltageCalculator = joltageCalculator;
    }

    public int solvePartOne(List<String> inputLines) {
        List<BatteryBank> batteryBanks = batteryBankParser.parseLines(inputLines);
        return joltageCalculator.totalMaximumJoltage(batteryBanks);
    }

    public long solvePartTwo(List<String> inputLines) {
        List<BatteryBank> batteryBanks = batteryBankParser.parseLines(inputLines);
        return joltageCalculator.totalMaximumJoltageUsingBatteries(batteryBanks, 12);
    }

    public static void main(String[] args) throws IOException {
        LobbyPuzzle puzzle = new LobbyPuzzle(new BatteryBankParser(), new JoltageCalculator());
        List<String> inputLines = Files.readAllLines(Path.of("src/main/resources/day03/input.txt"));
        System.out.println("Part 1: " + puzzle.solvePartOne(inputLines));
        System.out.println("Part 2: " + puzzle.solvePartTwo(inputLines));
    }
}
