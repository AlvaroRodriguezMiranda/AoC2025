package aoc.day01.dial;

public final class SafeDial {
    private static final int SIZE = 100;

    private int position;

    public SafeDial(int initialPosition) {
        if (initialPosition < 0 || initialPosition >= SIZE) {
            throw new IllegalArgumentException("Initial position must be between 0 and 99");
        }
        this.position = initialPosition;
    }

    public int position() {
        return position;
    }

    public void rotate(DialRotation rotation) {
        position = rotation.direction().apply(position, rotation.distance(), SIZE);
    }

    public int countClicksLandingOn(int targetPosition, DialRotation rotation) {
        if (targetPosition < 0 || targetPosition >= SIZE) {
            throw new IllegalArgumentException("Target position must be between 0 and 99");
        }

        int firstClick = rotation.direction().clicksToReach(position, targetPosition, SIZE);
        if (firstClick == 0) {
            firstClick = SIZE;
        }
        if (rotation.distance() < firstClick) {
            return 0;
        }

        return 1 + (rotation.distance() - firstClick) / SIZE;
    }
}
