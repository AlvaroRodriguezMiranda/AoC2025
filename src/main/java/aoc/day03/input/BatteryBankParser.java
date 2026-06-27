package aoc.day03.input;

import aoc.day03.battery.BatteryBank;

import java.util.ArrayList;
import java.util.List;

public final class BatteryBankParser {
    public List<BatteryBank> parseLines(List<String> lines) {
        return lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(this::parseLine)
                .toList();
    }

    public BatteryBank parseLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Battery bank line cannot be empty");
        }

        List<Integer> ratings = new ArrayList<>();
        for (char character : line.trim().toCharArray()) {
            if (!Character.isDigit(character) || character == '0') {
                throw new IllegalArgumentException("Battery rating must be a digit from 1 to 9: " + character);
            }
            ratings.add(Character.digit(character, 10));
        }

        return new BatteryBank(ratings);
    }
}
