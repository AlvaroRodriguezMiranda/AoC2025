package aoc.day02.product;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ProductIdSumCalculator {
    public long sumInvalidProductIds(List<ProductIdRange> ranges) {
        long total = 0;

        for (ProductIdRange range : ranges) {
            total += sumInvalidProductIds(range);
        }

        return total;
    }

    public long sumInvalidProductIdsWithRepeatedSequences(List<ProductIdRange> ranges) {
        long total = 0;

        for (ProductIdRange range : ranges) {
            total += sumInvalidProductIdsWithRepeatedSequences(range);
        }

        return total;
    }

    private long sumInvalidProductIds(ProductIdRange range) {
        long total = 0;
        int maximumDigits = Long.toString(range.lastId()).length();

        for (int halfLength = 1; halfLength * 2 <= maximumDigits; halfLength++) {
            long firstHalf = RepeatedPatternProductId.powerOfTen(halfLength - 1);
            long lastHalf = RepeatedPatternProductId.powerOfTen(halfLength) - 1;

            for (long half = firstHalf; half <= lastHalf; half++) {
                long productId = RepeatedPatternProductId.fromRepeatedHalf(half, halfLength);
                if (range.contains(productId)) {
                    total += productId;
                }
            }
        }

        return total;
    }

    private long sumInvalidProductIdsWithRepeatedSequences(ProductIdRange range) {
        Set<Long> invalidProductIds = new HashSet<>();
        int maximumDigits = Long.toString(range.lastId()).length();

        for (int totalLength = 2; totalLength <= maximumDigits; totalLength++) {
            for (int sequenceLength = 1; sequenceLength <= totalLength / 2; sequenceLength++) {
                if (totalLength % sequenceLength == 0) {
                    addRepeatedSequenceCandidates(range, invalidProductIds, sequenceLength, totalLength / sequenceLength);
                }
            }
        }

        long total = 0;
        for (long productId : invalidProductIds) {
            total += productId;
        }
        return total;
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
