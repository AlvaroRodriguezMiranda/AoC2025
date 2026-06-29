package aoc.day01.input;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.RotationProgram;

import java.util.List;

public final class RotationParser {
    public RotationProgram parseProgram(List<String> lines) {
        List<DialRotation> rotations = lines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(DialRotation::fromInstruction)
                .toList();
        return new RotationProgram(rotations);
    }

    public List<DialRotation> parseLines(List<String> lines) {
        return parseProgram(lines).asList();
    }

    public DialRotation parseLine(String line) {
        return DialRotation.fromInstruction(line);
    }
}
