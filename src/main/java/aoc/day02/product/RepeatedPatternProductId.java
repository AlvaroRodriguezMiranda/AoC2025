package aoc.day02.product;

public final class RepeatedPatternProductId {
    private RepeatedPatternProductId() {
    }

    public static boolean isInvalid(long productId) {
        String text = Long.toString(productId);
        if (text.length() % 2 != 0) {
            return false;
        }

        int middle = text.length() / 2;
        String firstHalf = text.substring(0, middle);
        String secondHalf = text.substring(middle);
        return firstHalf.equals(secondHalf);
    }

    public static boolean isInvalidWithRepeatedSequence(long productId) {
        String text = Long.toString(productId);

        for (int sequenceLength = 1; sequenceLength <= text.length() / 2; sequenceLength++) {
            if (text.length() % sequenceLength == 0 && isMadeOfRepeatedSequence(text, sequenceLength)) {
                return true;
            }
        }

        return false;
    }

    public static long fromRepeatedHalf(long half, int halfLength) {
        long multiplier = powerOfTen(halfLength);
        return half * multiplier + half;
    }

    public static long fromRepeatedSequence(long sequence, int sequenceLength, int repetitions) {
        long result = 0;
        long multiplier = powerOfTen(sequenceLength);

        for (int index = 0; index < repetitions; index++) {
            result = result * multiplier + sequence;
        }

        return result;
    }

    public static long powerOfTen(int exponent) {
        long result = 1;
        for (int index = 0; index < exponent; index++) {
            result *= 10;
        }
        return result;
    }

    private static boolean isMadeOfRepeatedSequence(String text, int sequenceLength) {
        String sequence = text.substring(0, sequenceLength);

        for (int index = sequenceLength; index < text.length(); index += sequenceLength) {
            if (!text.startsWith(sequence, index)) {
                return false;
            }
        }

        return true;
    }
}
