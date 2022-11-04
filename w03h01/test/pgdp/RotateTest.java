package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.arrayfun.ArrayFunctions;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class RotateTest {
    @ParameterizedTest
    @DisplayName("Positive amount")
    @MethodSource
    void positiveAmount(int[] a, int amount, int[] r) {
        ArrayFunctions.rotate(a, amount);
        assertArrayEquals(r, a);
    }

    private static Stream<Arguments> positiveAmount() {
        return Stream.of(
                arguments(
                        new int[]{1, 2, 3, 4, 5},
                        1,
                        new int[]{5, 1, 2, 3, 4}
                ),
                arguments(
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                        3,
                        new int[]{8, 9, 10, 1, 2, 3, 4, 5, 6, 7}
                ),
                arguments(
                        new int[]{1, 2, 3, 4, 5},
                        12,
                        new int[]{4, 5, 1, 2, 3}
                ),
                arguments(
                        new int[]{1, 2},
                        2147483647,
                        new int[]{2, 1}
                )

        );
    }

    @ParameterizedTest
    @DisplayName("Negative amount")
    @MethodSource
    void negativeAmount(int[] a, int amount, int[] r) {
        ArrayFunctions.rotate(a, amount);
        assertArrayEquals(r, a);
    }

    private static Stream<Arguments> negativeAmount() {
        return Stream.of(
                arguments(
                        new int[]{1, 2, 3, 4, 5},
                        -1,
                        new int[]{2, 3, 4, 5, 1}
                ),
                arguments(
                        new int[]{1, 2},
                        -7,
                        new int[]{2, 1}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        -2147483648,
                        new int[]{3, 1, 2}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("No rotation")
    @MethodSource
    void noRotation(int[] a, int amount, int[] r) {
        ArrayFunctions.rotate(a, amount);
        assertArrayEquals(r, a);
    }

    private static Stream<Arguments> noRotation() {
        return Stream.of(
                arguments(
                        new int[]{1},
                        0,
                        new int[]{1}
                ),
                arguments(
                        new int[]{1},
                        -10,
                        new int[]{1}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        3,
                        new int[]{1, 2, 3}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        -9,
                        new int[]{1, 2, 3}
                )
        );
    }

    @Test
    @DisplayName("Empty array")
    void emptyArray() {
        int[] a = new int[0];

        ArrayFunctions.rotate(a, 0);
        ArrayFunctions.rotate(a, 1);
        ArrayFunctions.rotate(a, -12345);

        assertArrayEquals(new int[0], a);
    }
}
