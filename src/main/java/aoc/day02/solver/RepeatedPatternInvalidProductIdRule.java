package aoc.day02.solver;

import aoc.day02.product.ProductIdRange;
import aoc.day02.product.RepeatedPatternProductId;

import java.util.HashSet;
import java.util.Set;

public final class RepeatedPatternInvalidProductIdRule implements InvalidProductIdRule {
    @Override
    public Set<Long> findInvalidProductIds(ProductIdRange range) {
        Set<Long> invalidProductIds = new HashSet<>();
        int maximumDigits = Long.toString(range.lastId()).length();

        for (int totalLength = 2; totalLength <= maximumDigits; totalLength++) {
            for (int sequenceLength = 1; sequenceLength <= totalLength / 2; sequenceLength++) {
                if (totalLength % sequenceLength == 0) {
                    addRepeatedSequenceCandidates(range, invalidProductIds, sequenceLength, totalLength / sequenceLength);
                }
            }
        }

        return invalidProductIds;
    }

    private void addRepeatedSequenceCandidates(
            ProductIdRange range,
            Set<Long> invalidProductIds,
            int sequenceLength,
            int repetitions
    ) {
        long firstSequence = RepeatedPatternProductId.powerOfTen(sequenceLength - 1);
        long lastSequence = RepeatedPatternProductId.powerOfTen(sequenceLength) - 1;

        for (long sequence = firstSequence; sequence <= lastSequence; sequence++) {
            long productId = RepeatedPatternProductId.fromRepeatedSequence(sequence, sequenceLength, repetitions);
            if (range.contains(productId)) {
                invalidProductIds.add(productId);
            }
        }
    }
}
