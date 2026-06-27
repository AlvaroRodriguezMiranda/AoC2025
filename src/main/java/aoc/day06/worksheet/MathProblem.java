package aoc.day06.worksheet;

import java.util.List;

public final class MathProblem {
    private final List<Long> numbers;
    private final MathOperation operation;

    public MathProblem(List<Long> numbers, MathOperation operation) {
        if (numbers.isEmpty()) {
            throw new IllegalArgumentException("A math problem must contain at least one number");
        }
        this.numbers = List.copyOf(numbers);
        this.operation = operation;
    }

    public long solve() {
        return operation.apply(numbers);
    }

    public List<Long> numbers() {
        return numbers;
    }

    public MathOperation operation() {
        return operation;
    }
}
