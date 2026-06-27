package aoc.day06.worksheet;

import java.util.List;

public enum MathOperation {
    ADDITION {
        @Override
        public long apply(List<Long> numbers) {
            return numbers.stream()
                    .mapToLong(Long::longValue)
                    .sum();
        }
    },
    MULTIPLICATION {
        @Override
        public long apply(List<Long> numbers) {
            long result = 1;
            for (long number : numbers) {
                result *= number;
            }
            return result;
        }
    };

    public abstract long apply(List<Long> numbers);

    public static MathOperation fromSymbol(char symbol) {
        return switch (symbol) {
            case '+' -> ADDITION;
            case '*' -> MULTIPLICATION;
            default -> throw new IllegalArgumentException("Unknown operation symbol: " + symbol);
        };
    }
}
