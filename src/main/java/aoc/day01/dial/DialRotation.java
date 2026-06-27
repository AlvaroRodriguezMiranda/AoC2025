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
}
