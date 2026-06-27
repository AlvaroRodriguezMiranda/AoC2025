package aoc.day01.password;

import aoc.day01.dial.DialRotation;
import aoc.day01.input.RotationParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordSolverTest {
    private final RotationParser parser = new RotationParser();
    private final PasswordSolver solver = new PasswordSolver();

    @Test
    void countsOfficialExampleRotationsEndingAtZero() {
        List<DialRotation> rotations = parser.parseLines(List.of(
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"
        ));

        int password = solver.countRotationsEndingAtZero(rotations);

        assertEquals(3, password);
    }

    @Test
    void doesNotCountInitialPosition() {
        List<DialRotation> rotations = parser.parseLines(List.of("R1"));

        int password = solver.countRotationsEndingAtZero(rotations);

        assertEquals(0, password);
    }

    @Test
    void countsEveryRotationThatFinishesAtZero() {
        List<DialRotation> rotations = parser.parseLines(List.of("R50", "L100", "R200"));

        int password = solver.countRotationsEndingAtZero(rotations);

        assertEquals(3, password);
    }

    @Test
    void countsOfficialExampleClicksLandingOnZero() {
        List<DialRotation> rotations = parser.parseLines(List.of(
                "L68",
                "L30",
                "R48",
                "L5",
                "R60",
                "L55",
                "L1",
                "L99",
                "R14",
                "L82"
        ));

        int password = solver.countClicksLandingOnZero(rotations);

        assertEquals(6, password);
    }

    @Test
    void countsMultipleZeroLandingsInsideSingleLongRotation() {
        List<DialRotation> rotations = parser.parseLines(List.of("R1000"));

        int password = solver.countClicksLandingOnZero(rotations);

        assertEquals(10, password);
    }
}
