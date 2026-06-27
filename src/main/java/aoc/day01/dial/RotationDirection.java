package aoc.day01.dial;

public enum RotationDirection {
    LEFT {
        @Override
        int apply(int position, int distance, int size) {
            return Math.floorMod(position - distance, size);
        }

        @Override
        int clicksToReach(int position, int targetPosition, int size) {
            return Math.floorMod(position - targetPosition, size);
        }
    },
    RIGHT {
        @Override
        int apply(int position, int distance, int size) {
            return Math.floorMod(position + distance, size);
        }

        @Override
        int clicksToReach(int position, int targetPosition, int size) {
            return Math.floorMod(targetPosition - position, size);
        }
    };

    abstract int apply(int position, int distance, int size);

    abstract int clicksToReach(int position, int targetPosition, int size);

    public static RotationDirection fromSymbol(char symbol) {
        return switch (symbol) {
            case 'L' -> LEFT;
            case 'R' -> RIGHT;
            default -> throw new IllegalArgumentException("Unknown rotation direction: " + symbol);
        };
    }
}
