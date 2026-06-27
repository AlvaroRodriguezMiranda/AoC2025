package aoc.day10.factory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class MachineInitializer {
    public int minimumPressesForAll(List<FactoryMachine> machines) {
        return machines.stream()
                .mapToInt(this::minimumPressesFor)
                .sum();
    }

    public long minimumJoltagePressesForAll(List<FactoryMachine> machines) {
        return machines.stream()
                .mapToLong(this::minimumJoltagePressesFor)
                .sum();
    }

    public int minimumPressesFor(FactoryMachine machine) {
        if (machine.targetMask() == 0) {
            return 0;
        }

        Queue<MachineState> pendingStates = new ArrayDeque<>();
        Set<Long> visitedMasks = new HashSet<>();
        pendingStates.add(new MachineState(0, 0));
        visitedMasks.add(0L);

        while (!pendingStates.isEmpty()) {
            MachineState state = pendingStates.remove();

            for (ButtonWiring button : machine.buttons()) {
                long nextMask = state.lightMask() ^ button.toggleMask();
                if (!visitedMasks.add(nextMask)) {
                    continue;
                }
                int nextPressCount = state.pressCount() + 1;
                if (nextMask == machine.targetMask()) {
                    return nextPressCount;
                }
                pendingStates.add(new MachineState(nextMask, nextPressCount));
            }
        }

        throw new IllegalStateException("Machine cannot be initialized with the available buttons");
    }

    public long minimumJoltagePressesFor(FactoryMachine machine) {
        if (machine.joltageRequirements().isEmpty()) {
            throw new IllegalArgumentException("Machine must contain joltage requirements");
        }

        JoltageSearch search = new JoltageSearch(machine);
        return search.findMinimumPresses();
    }

    private record MachineState(long lightMask, int pressCount) {
    }

    private static final class JoltageSearch {
        private final int counterCount;
        private final int[] requirements;
        private final List<JoltageButton> buttons;

        private JoltageSearch(FactoryMachine machine) {
            this.counterCount = machine.joltageRequirements().size();
            this.requirements = machine.joltageRequirements().stream()
                    .mapToInt(Integer::intValue)
                    .toArray();
            this.buttons = machine.buttons().stream()
                    .map(button -> JoltageButton.from(button, counterCount))
                    .distinct()
                    .sorted(Comparator.comparingInt(JoltageButton::width).reversed())
                    .toList();
        }

        private long findMinimumPresses() {
            LinearSystemSolution system = reduceSystem();
            long result = enumerateFreeVariables(system, 0, new long[system.freeColumns().size()], 0);
            if (result == Long.MAX_VALUE) {
                throw new IllegalStateException("Machine cannot satisfy the joltage requirements");
            }
            return result;
        }

        private LinearSystemSolution reduceSystem() {
            int rowCount = counterCount;
            int columnCount = buttons.size();
            Rational[][] matrix = new Rational[rowCount][columnCount + 1];

            for (int row = 0; row < rowCount; row++) {
                for (int column = 0; column < columnCount; column++) {
                    matrix[row][column] = buttons.get(column).affects(row) ? Rational.ONE : Rational.ZERO;
                }
                matrix[row][columnCount] = Rational.of(requirements[row]);
            }

            List<Integer> pivotColumns = new ArrayList<>();
            int pivotRow = 0;

            for (int column = 0; column < columnCount && pivotRow < rowCount; column++) {
                int selectedRow = findPivotRow(matrix, pivotRow, column);
                if (selectedRow == -1) {
                    continue;
                }

                Rational[] temporaryRow = matrix[pivotRow];
                matrix[pivotRow] = matrix[selectedRow];
                matrix[selectedRow] = temporaryRow;

                Rational pivotValue = matrix[pivotRow][column];
                for (int valueColumn = column; valueColumn <= columnCount; valueColumn++) {
                    matrix[pivotRow][valueColumn] = matrix[pivotRow][valueColumn].divide(pivotValue);
                }

                for (int row = 0; row < rowCount; row++) {
                    if (row == pivotRow || matrix[row][column].isZero()) {
                        continue;
                    }
                    Rational factor = matrix[row][column];
                    for (int valueColumn = column; valueColumn <= columnCount; valueColumn++) {
                        matrix[row][valueColumn] = matrix[row][valueColumn].subtract(factor.multiply(matrix[pivotRow][valueColumn]));
                    }
                }

                pivotColumns.add(column);
                pivotRow++;
            }

            validateConsistency(matrix, pivotRow, columnCount);

            List<Integer> freeColumns = new ArrayList<>();
            for (int column = 0; column < columnCount; column++) {
                if (!pivotColumns.contains(column)) {
                    freeColumns.add(column);
                }
            }

            return new LinearSystemSolution(matrix, pivotColumns, freeColumns, pivotRow);
        }

        private int findPivotRow(Rational[][] matrix, int startRow, int column) {
            for (int row = startRow; row < matrix.length; row++) {
                if (!matrix[row][column].isZero()) {
                    return row;
                }
            }
            return -1;
        }

        private void validateConsistency(Rational[][] matrix, int firstNonPivotRow, int buttonCount) {
            for (int row = firstNonPivotRow; row < matrix.length; row++) {
                boolean hasCoefficient = false;
                for (int column = 0; column < buttonCount; column++) {
                    if (!matrix[row][column].isZero()) {
                        hasCoefficient = true;
                        break;
                    }
                }
                if (!hasCoefficient && !matrix[row][buttonCount].isZero()) {
                    throw new IllegalStateException("Machine cannot satisfy the joltage requirements");
                }
            }
        }

        private long enumerateFreeVariables(LinearSystemSolution system, int freeIndex, long[] freeValues, long currentFreePresses) {
            if (freeIndex == system.freeColumns().size()) {
                return evaluateSolution(system, freeValues, currentFreePresses);
            }

            int freeColumn = system.freeColumns().get(freeIndex);
            long bestResult = Long.MAX_VALUE;
            int maximumValue = maximumPressesForButton(freeColumn);

            for (int value = 0; value <= maximumValue; value++) {
                freeValues[freeIndex] = value;
                long result = enumerateFreeVariables(system, freeIndex + 1, freeValues, currentFreePresses + value);
                bestResult = Math.min(bestResult, result);
            }

            return bestResult;
        }

        private long evaluateSolution(LinearSystemSolution system, long[] freeValues, long currentFreePresses) {
            long totalPresses = currentFreePresses;
            int buttonCount = buttons.size();

            for (int row = 0; row < system.rank(); row++) {
                Rational value = system.matrix()[row][buttonCount];
                for (int freeIndex = 0; freeIndex < system.freeColumns().size(); freeIndex++) {
                    int freeColumn = system.freeColumns().get(freeIndex);
                    value = value.subtract(system.matrix()[row][freeColumn].multiply(freeValues[freeIndex]));
                }

                if (!value.isNonNegativeInteger()) {
                    return Long.MAX_VALUE;
                }
                totalPresses += value.toLong();
            }

            return totalPresses;
        }

        private int maximumPressesForButton(int buttonIndex) {
            int maximumPresses = Integer.MAX_VALUE;
            for (int counter : buttons.get(buttonIndex).affectedCounters()) {
                maximumPresses = Math.min(maximumPresses, requirements[counter]);
            }
            return maximumPresses == Integer.MAX_VALUE ? 0 : maximumPresses;
        }
    }

    private record LinearSystemSolution(
            Rational[][] matrix,
            List<Integer> pivotColumns,
            List<Integer> freeColumns,
            int rank
    ) {
    }

    private record Rational(long numerator, long denominator) {
        private static final Rational ZERO = new Rational(0, 1);
        private static final Rational ONE = new Rational(1, 1);

        private Rational {
            if (denominator == 0) {
                throw new IllegalArgumentException("Denominator cannot be zero");
            }
            if (denominator < 0) {
                numerator = -numerator;
                denominator = -denominator;
            }
            long greatestCommonDivisor = greatestCommonDivisor(Math.abs(numerator), denominator);
            numerator /= greatestCommonDivisor;
            denominator /= greatestCommonDivisor;
        }

        private static Rational of(long value) {
            return new Rational(value, 1);
        }

        private Rational add(Rational other) {
            return new Rational(
                    numerator * other.denominator + other.numerator * denominator,
                    denominator * other.denominator
            );
        }

        private Rational subtract(Rational other) {
            return add(new Rational(-other.numerator, other.denominator));
        }

        private Rational multiply(Rational other) {
            return new Rational(numerator * other.numerator, denominator * other.denominator);
        }

        private Rational multiply(long value) {
            return new Rational(numerator * value, denominator);
        }

        private Rational divide(Rational other) {
            return new Rational(numerator * other.denominator, denominator * other.numerator);
        }

        private boolean isZero() {
            return numerator == 0;
        }

        private boolean isNonNegativeInteger() {
            return numerator >= 0 && numerator % denominator == 0;
        }

        private long toLong() {
            return numerator / denominator;
        }

        private static long greatestCommonDivisor(long first, long second) {
            long a = first;
            long b = second;
            while (b != 0) {
                long temporary = a % b;
                a = b;
                b = temporary;
            }
            return a == 0 ? 1 : a;
        }
    }

    private record JoltageButton(List<Integer> affectedCounters) {
        private static JoltageButton from(ButtonWiring button, int counterCount) {
            List<Integer> affectedCounters = new ArrayList<>();
            for (int counter = 0; counter < counterCount; counter++) {
                if ((button.toggleMask() & (1L << counter)) != 0) {
                    affectedCounters.add(counter);
                }
            }
            return new JoltageButton(affectedCounters);
        }

        private boolean affects(int counter) {
            return affectedCounters.contains(counter);
        }

        private int width() {
            return affectedCounters.size();
        }
    }
}
