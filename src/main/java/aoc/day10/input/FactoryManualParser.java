package aoc.day10.input;

import aoc.day10.factory.ButtonWiring;
import aoc.day10.factory.FactoryMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FactoryManualParser {
    private static final Pattern DIAGRAM_PATTERN = Pattern.compile("\\[([.#]+)]");
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\(([^)]*)\\)");
    private static final Pattern JOLTAG_PATTERN = Pattern.compile("\\{([^}]*)}");

    public List<FactoryMachine> parse(List<String> lines) {
        List<FactoryMachine> machines = lines.stream()
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .toList();

        if (machines.isEmpty()) {
            throw new IllegalArgumentException("Factory manual cannot be empty");
        }

        return machines;
    }

    private FactoryMachine parseLine(String line) {
        Matcher diagramMatcher = DIAGRAM_PATTERN.matcher(line);
        if (!diagramMatcher.find()) {
            throw new IllegalArgumentException("Machine line must contain an indicator diagram: " + line);
        }

        String diagram = diagramMatcher.group(1);
        long targetMask = parseTargetMask(diagram);
        List<Integer> joltageRequirements = parseJoltageRequirements(line);
        if (joltageRequirements.size() != diagram.length()) {
            throw new IllegalArgumentException("Joltage requirement count must match the indicator diagram: " + line);
        }
        List<ButtonWiring> buttons = parseButtons(line, diagram.length());
        return new FactoryMachine(diagram.length(), targetMask, buttons, joltageRequirements);
    }

    private long parseTargetMask(String diagram) {
        long targetMask = 0;
        for (int index = 0; index < diagram.length(); index++) {
            char light = diagram.charAt(index);
            if (light == '#') {
                targetMask |= 1L << index;
            } else if (light != '.') {
                throw new IllegalArgumentException("Unknown indicator light state: " + light);
            }
        }
        return targetMask;
    }

    private List<ButtonWiring> parseButtons(String line, int lightCount) {
        List<ButtonWiring> buttons = new ArrayList<>();
        Matcher buttonMatcher = BUTTON_PATTERN.matcher(line);

        while (buttonMatcher.find()) {
            buttons.add(new ButtonWiring(parseButtonMask(buttonMatcher.group(1), lightCount)));
        }

        if (buttons.isEmpty()) {
            throw new IllegalArgumentException("Machine line must contain at least one button: " + line);
        }

        return buttons;
    }

    private List<Integer> parseJoltageRequirements(String line) {
        Matcher joltageMatcher = JOLTAG_PATTERN.matcher(line);
        if (!joltageMatcher.find()) {
            throw new IllegalArgumentException("Machine line must contain joltage requirements: " + line);
        }

        List<Integer> requirements = new ArrayList<>();
        for (String requirementText : joltageMatcher.group(1).split(",")) {
            requirements.add(parseJoltageRequirement(requirementText.trim()));
        }
        return requirements;
    }

    private int parseJoltageRequirement(String text) {
        try {
            int requirement = Integer.parseInt(text);
            if (requirement < 0) {
                throw new IllegalArgumentException("Joltage requirement cannot be negative: " + text);
            }
            return requirement;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Joltage requirement must be a number: " + text, exception);
        }
    }

    private long parseButtonMask(String buttonText, int lightCount) {
        long buttonMask = 0;
        for (String indexText : buttonText.split(",")) {
            int lightIndex = parseLightIndex(indexText.trim(), lightCount);
            buttonMask ^= 1L << lightIndex;
        }
        return buttonMask;
    }

    private int parseLightIndex(String text, int lightCount) {
        try {
            int lightIndex = Integer.parseInt(text);
            if (lightIndex < 0 || lightIndex >= lightCount) {
                throw new IllegalArgumentException("Button light index is outside the diagram: " + text);
            }
            return lightIndex;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Button light index must be a number: " + text, exception);
        }
    }
}
