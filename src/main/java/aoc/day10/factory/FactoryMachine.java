package aoc.day10.factory;

import java.util.List;

public final class FactoryMachine {
    private final int lightCount;
    private final long targetMask;
    private final List<ButtonWiring> buttons;
    private final List<Integer> joltageRequirements;

    public FactoryMachine(int lightCount, long targetMask, List<ButtonWiring> buttons) {
        this(lightCount, targetMask, buttons, List.of());
    }

    public FactoryMachine(int lightCount, long targetMask, List<ButtonWiring> buttons, List<Integer> joltageRequirements) {
        if (lightCount <= 0 || lightCount > 62) {
            throw new IllegalArgumentException("Machine must have between 1 and 62 lights");
        }
        if (buttons.isEmpty()) {
            throw new IllegalArgumentException("Machine must have at least one button");
        }
        if (!joltageRequirements.isEmpty() && joltageRequirements.size() != lightCount) {
            throw new IllegalArgumentException("Joltage requirement count must match the indicator light count");
        }
        this.lightCount = lightCount;
        this.targetMask = targetMask;
        this.buttons = List.copyOf(buttons);
        this.joltageRequirements = List.copyOf(joltageRequirements);
    }

    public int lightCount() {
        return lightCount;
    }

    public long targetMask() {
        return targetMask;
    }

    public List<ButtonWiring> buttons() {
        return buttons;
    }

    public List<Integer> joltageRequirements() {
        return joltageRequirements;
    }
}
