package aoc.day08.circuit;

import java.util.List;
import java.util.stream.IntStream;

final class CircuitNetwork {
    private final int[] parents;
    private final int[] sizes;
    private int circuitCount;

    CircuitNetwork(int circuitCount) {
        this.parents = new int[circuitCount];
        this.sizes = new int[circuitCount];
        this.circuitCount = circuitCount;

        for (int circuit = 0; circuit < circuitCount; circuit++) {
            parents[circuit] = circuit;
            sizes[circuit] = 1;
        }
    }

    boolean connect(int firstCircuit, int secondCircuit) {
        int firstRoot = find(firstCircuit);
        int secondRoot = find(secondCircuit);

        if (firstRoot == secondRoot) {
            return false;
        }

        if (sizes[firstRoot] < sizes[secondRoot]) {
            int temporary = firstRoot;
            firstRoot = secondRoot;
            secondRoot = temporary;
        }

        parents[secondRoot] = firstRoot;
        sizes[firstRoot] += sizes[secondRoot];
        circuitCount--;
        return true;
    }

    boolean isSingleCircuit() {
        return circuitCount == 1;
    }

    List<Integer> circuitSizesDescending() {
        return IntStream.range(0, parents.length)
                .filter(circuit -> find(circuit) == circuit)
                .map(circuit -> sizes[circuit])
                .boxed()
                .sorted((first, second) -> Integer.compare(second, first))
                .toList();
    }

    private int find(int circuit) {
        if (parents[circuit] != circuit) {
            parents[circuit] = find(parents[circuit]);
        }
        return parents[circuit];
    }
}
