package aoc.day01.password;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.RotationProgram;
import aoc.day01.dial.SafeDial;

public final class ClickPasswordSolver implements PasswordSolver {
    private static final int INITIAL_POSITION = 50;
    private static final int PASSWORD_POSITION = 0;

    @Override
    public int solve(RotationProgram rotationProgram) {
        SafeDial safeDial = new SafeDial(INITIAL_POSITION);
        int timesLandingOnZero = 0;

        for (DialRotation rotation : rotationProgram) {
            timesLandingOnZero += safeDial.countClicksLandingOn(PASSWORD_POSITION, rotation);
            safeDial.rotate(rotation);
        }

        return timesLandingOnZero;
    }
}
