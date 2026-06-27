package aoc.day02.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepeatedPatternProductIdTest {
    @Test
    void detectsInvalidRepeatedPatternProductIds() {
        assertTrue(RepeatedPatternProductId.isInvalid(55));
        assertTrue(RepeatedPatternProductId.isInvalid(6464));
        assertTrue(RepeatedPatternProductId.isInvalid(123123));
    }

    @Test
    void ignoresProductIdsWithoutRepeatedPattern() {
        assertFalse(RepeatedPatternProductId.isInvalid(101));
        assertFalse(RepeatedPatternProductId.isInvalid(123124));
        assertFalse(RepeatedPatternProductId.isInvalid(12345));
    }

    @Test
    void detectsProductIdsMadeOfASequenceRepeatedAtLeastTwice() {
        assertTrue(RepeatedPatternProductId.isInvalidWithRepeatedSequence(12341234));
        assertTrue(RepeatedPatternProductId.isInvalidWithRepeatedSequence(123123123));
        assertTrue(RepeatedPatternProductId.isInvalidWithRepeatedSequence(1212121212L));
        assertTrue(RepeatedPatternProductId.isInvalidWithRepeatedSequence(1111111));
    }

    @Test
    void ignoresProductIdsWithoutARepeatedSequence() {
        assertFalse(RepeatedPatternProductId.isInvalidWithRepeatedSequence(101));
        assertFalse(RepeatedPatternProductId.isInvalidWithRepeatedSequence(123124));
    }
}
