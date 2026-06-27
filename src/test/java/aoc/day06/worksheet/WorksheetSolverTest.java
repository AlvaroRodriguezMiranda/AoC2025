package aoc.day06.worksheet;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorksheetSolverTest {
    @Test
    void sumsProblemResults() {
        WorksheetSolver solver = new WorksheetSolver();

        long result = solver.grandTotal(List.of(
                new MathProblem(List.of(1L, 2L), MathOperation.ADDITION),
                new MathProblem(List.of(3L, 4L), MathOperation.MULTIPLICATION)
        ));

        assertEquals(15, result);
    }
}
