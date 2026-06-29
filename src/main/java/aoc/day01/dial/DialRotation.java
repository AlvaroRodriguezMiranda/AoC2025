package aoc.day01.dial;

public record DialRotation(RotationDirection direction, int distance) {
    public DialRotation {
        if (direction == null) {
            throw new IllegalArgumentException("Rotation direction is required");
        }
        if (distance < 0) {
            throw new IllegalArgumentException("Rotation distance cannot be negative");
        }
    }

    public static DialRotation fromInstruction(String instruction) {
        if (instruction == null) {
            throw new IllegalArgumentException("Rotation instruction must contain a direction and a distance");
        }

        String trimmedInstruction = instruction.trim();
        if (trimmedInstruction.length() < 2) {
            throw new IllegalArgumentException("Rotation instruction must contain a direction and a distance");
        }

        RotationDirection direction = RotationDirection.fromSymbol(trimmedInstruction.charAt(0));
        int distance = parseDistance(trimmedInstruction.substring(1));
        return new DialRotation(direction, distance);
    }

    private static int parseDistance(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Rotation distance must be a number: " + text, exception);
        }
    }
}
