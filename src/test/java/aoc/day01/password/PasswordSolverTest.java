package aoc.day01.password;

import aoc.day01.dial.RotationProgram;
import aoc.day01.input.RotationParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordSolverTest {
    private final RotationParser parser = new RotationParser();

    @Test
    void countsOfficialExampleRotationsEndingAtZero() {
        RotationProgram rotationProgram = parser.parseProgram(List.of(
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

        int password = new FinalPositionPasswordSolver().solve(rotationProgram);

        assertEquals(3, password);
    }

    @Test
    void doesNotCountInitialPosition() {
        RotationProgram rotationProgram = parser.parseProgram(List.of("R1"));

        int password = new FinalPositionPasswordSolver().solve(rotationProgram);

        assertEquals(0, password);
    }

    @Test
    void countsEveryRotationThatFinishesAtZero() {
        RotationProgram rotationProgram = parser.parseProgram(List.of("R50", "L100", "R200"));

        int password = new FinalPositionPasswordSolver().solve(rotationProgram);

        assertEquals(3, password);
    }

    @Test
    void countsOfficialExampleClicksLandingOnZero() {
        RotationProgram rotationProgram = parser.parseProgram(List.of(
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

        int password = new ClickPasswordSolver().solve(rotationProgram);

        assertEquals(6, password);
    }

    @Test
    void countsMultipleZeroLandingsInsideSingleLongRotation() {
        RotationProgram rotationProgram = parser.parseProgram(List.of("R1000"));

        int password = new ClickPasswordSolver().solve(rotationProgram);

        assertEquals(10, password);
    }
}
