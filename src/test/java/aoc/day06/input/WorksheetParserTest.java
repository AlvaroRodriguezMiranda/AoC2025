package aoc.day06.input;

import aoc.day06.worksheet.MathOperation;
import aoc.day06.worksheet.MathProblem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorksheetParserTest {
    private final WorksheetParser parser = new WorksheetParser();

    @Test
    void parsesProblemsSeparatedByEmptyColumns() {
        List<MathProblem> problems = parser.parseLines(List.of(
                "123 328",
                " 45 64 ",
                "  6 98 ",
                "*   +  "
        ));

        assertEquals(2, problems.size());
        assertEquals(List.of(123L, 45L, 6L), problems.get(0).numbers());
        assertEquals(MathOperation.MULTIPLICATION, problems.get(0).operation());
        assertEquals(List.of(328L, 64L, 98L), problems.get(1).numbers());
        assertEquals(MathOperation.ADDITION, problems.get(1).operation());
    }

    @Test
    void parsesOfficialExampleProblemResults() {
        List<MathProblem> problems = parser.parseLines(List.of(
                "123 328  51 64 ",
                " 45 64  387 23 ",
                "  6 98  215 314",
                "*   +   *   +  "
        ));

        assertEquals(33210, problems.get(0).solve());
        assertEquals(490, problems.get(1).solve());
        assertEquals(4243455, problems.get(2).solve());
        assertEquals(401, problems.get(3).solve());
    }

    @Test
    void parsesOfficialExampleRightToLeftProblemResults() {
        List<MathProblem> problems = parser.parseRightToLeftLines(List.of(
                "123 328  51 64 ",
                " 45 64  387 23 ",
                "  6 98  215 314",
                "*   +   *   +  "
        ));

        assertEquals(8544, problems.get(0).solve());
        assertEquals(625, problems.get(1).solve());
        assertEquals(3253600, problems.get(2).solve());
        assertEquals(1058, problems.get(3).solve());
    }

    @Test
    void readsNumbersFromColumnsInRightToLeftMode() {
        List<MathProblem> problems = parser.parseRightToLeftLines(List.of(
                "12",
                "34",
                "* "
        ));

        assertEquals(List.of(24L, 13L), problems.getFirst().numbers());
        assertEquals(MathOperation.MULTIPLICATION, problems.getFirst().operation());
    }

    @Test
    void rejectsWorksheetWithoutOperation() {
        assertThrows(IllegalArgumentException.class, () -> parser.parseLines(List.of("123")));
    }
}
