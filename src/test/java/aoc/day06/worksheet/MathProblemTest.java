package aoc.day06.worksheet;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MathProblemTest {
    @Test
    void solvesAdditionProblem() {
        MathProblem problem = new MathProblem(List.of(328L, 64L, 98L), MathOperation.ADDITION);

        assertEquals(490, problem.solve());
    }

    @Test
    void solvesMultiplicationProblem() {
        MathProblem problem = new MathProblem(List.of(123L, 45L, 6L), MathOperation.MULTIPLICATION);

        assertEquals(33210, problem.solve());
    }

    @Test
    void rejectsProblemWithoutNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new MathProblem(List.of(), MathOperation.ADDITION));
    }
}
