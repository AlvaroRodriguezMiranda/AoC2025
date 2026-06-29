package aoc.day01.password;

import aoc.day01.dial.DialRotation;
import aoc.day01.dial.RotationProgram;
import aoc.day01.dial.SafeDial;

public final class FinalPositionPasswordSolver implements PasswordSolver {
    private static final int INITIAL_POSITION = 50;
    private static final int PASSWORD_POSITION = 0;

    @Override
    public int solve(RotationProgram rotationProgram) {
        SafeDial safeDial = new SafeDial(INITIAL_POSITION);
        int timesEndingAtZero = 0;

        for (DialRotation rotation : rotationProgram) {
            safeDial.rotate(rotation);
            if (safeDial.position() == PASSWORD_POSITION) {
                timesEndingAtZero++;
            }
        }

        return timesEndingAtZero;
    }
}
