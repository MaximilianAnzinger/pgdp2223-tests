package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.arrayfun.ArrayFunctions;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SumOfSquaresTest {
    @ParameterizedTest
    @DisplayName("Check result")
    @MethodSource
    void checkResult(int[] a, long result) {
        assertEquals(result, ArrayFunctions.sumOfSquares(a));
    }

    private static Stream<Arguments> checkResult() {
        return Stream.of(
                arguments(
                        new int[]{0, 1, 2, 3, 4, 5},
                        55L
                ),
                arguments(
                        new int[]{-0, -1, -2, -3, -4, -5},
                        55L
                ),
                arguments(
                        new int[]{1, -100, 5, 1000},
                        1010026
                ),
                arguments(
                        new int[]{0},
                        0
                ),
                arguments(
                        new int[]{1},
                        1
                ),
                arguments(
                        // TODO: make use of negative integer limit
                        new int[]{2147483647, 2147483647, 92681, -408, 19, 1, 1, 1},
                        9223372036854775807L
                )
        );
    }

    @Test
    @DisplayName("Empty array")
    void emptyArray() {
        assertEquals(0, ArrayFunctions.sumOfSquares(new int[0]));
    }

    @ParameterizedTest
    @DisplayName("Result overflow")
    @MethodSource
    void overflow(int[] a) {
        OutputStream out = new ByteArrayOutputStream();
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(out));

        assertEquals(
                -1,
                ArrayFunctions.sumOfSquares(a)
        );

        // TODO: newline?
        assertEquals("Overflow!" + System.lineSeparator(), out.toString());

        System.setOut(stdout);
    }

    private static Stream<int[]> overflow() {
        return Stream.of(
                new int[]{2147483647, 2147483647, 92681, 408, 19, 1, 1, 1, 1},
                new int[]{2147483647, 2147483647, 92681, 408, 19, 1, 1, 1, -2147483648},
                new int[]{2147483647, 2147483647, 2147483647, 2147483647, -2147483648}
        );
    }
}
