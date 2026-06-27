package aoc.day01.dial;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SafeDialTest {
    @Test
    void rotatesRightInsideDialRange() {
        SafeDial safeDial = new SafeDial(11);

        safeDial.rotate(new DialRotation(RotationDirection.RIGHT, 8));

        assertEquals(19, safeDial.position());
    }

    @Test
    void rotatesLeftInsideDialRange() {
        SafeDial safeDial = new SafeDial(19);

        safeDial.rotate(new DialRotation(RotationDirection.LEFT, 19));

        assertEquals(0, safeDial.position());
    }

    @Test
    void wrapsFromZeroToNinetyNineWhenRotatingLeft() {
        SafeDial safeDial = new SafeDial(0);

        safeDial.rotate(new DialRotation(RotationDirection.LEFT, 1));

        assertEquals(99, safeDial.position());
    }

    @Test
    void wrapsFromNinetyNineToZeroWhenRotatingRight() {
        SafeDial safeDial = new SafeDial(99);

        safeDial.rotate(new DialRotation(RotationDirection.RIGHT, 1));

        assertEquals(0, safeDial.position());
    }

    @Test
    void rejectsInvalidInitialPosition() {
        assertThrows(IllegalArgumentException.class, () -> new SafeDial(100));
    }

    @Test
    void countsClicksLandingOnTargetDuringRotation() {
        SafeDial safeDial = new SafeDial(50);

        int clicksLandingOnZero = safeDial.countClicksLandingOn(0, new DialRotation(RotationDirection.LEFT, 68));

        assertEquals(1, clicksLandingOnZero);
    }

    @Test
    void countsRepeatedClicksLandingOnTargetDuringLongRotation() {
        SafeDial safeDial = new SafeDial(50);

        int clicksLandingOnZero = safeDial.countClicksLandingOn(0, new DialRotation(RotationDirection.RIGHT, 1000));

        assertEquals(10, clicksLandingOnZero);
    }

    @Test
    void doesNotCountCurrentPositionBeforeAnyClick() {
        SafeDial safeDial = new SafeDial(0);

        int clicksLandingOnZero = safeDial.countClicksLandingOn(0, new DialRotation(RotationDirection.LEFT, 1));

        assertEquals(0, clicksLandingOnZero);
    }
}
