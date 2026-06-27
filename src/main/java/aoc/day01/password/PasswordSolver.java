package aoc.day01.password;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.SafeDial;

import java.util.List;

public final class PasswordSolver {
    private static final int INITIAL_POSITION = 50;
    private static final int PASSWORD_POSITION = 0;

    public int countRotationsEndingAtZero(List<DialRotation> rotations) {
        SafeDial safeDial = new SafeDial(INITIAL_POSITION);
        int timesEndingAtZero = 0;

        for (DialRotation rotation : rotations) {
            safeDial.rotate(rotation);
            if (safeDial.position() == PASSWORD_POSITION) {
                timesEndingAtZero++;
            }
        }

        return timesEndingAtZero;
    }

    public int countClicksLandingOnZero(List<DialRotation> rotations) {
        SafeDial safeDial = new SafeDial(INITIAL_POSITION);
        int timesLandingOnZero = 0;

        for (DialRotation rotation : rotations) {
            timesLandingOnZero += safeDial.countClicksLandingOn(PASSWORD_POSITION, rotation);
            safeDial.rotate(rotation);
        }

        return timesLandingOnZero;
    }
}
