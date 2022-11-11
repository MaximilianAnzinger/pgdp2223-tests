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

    @DisplayName("Integer.MAX_VALUE amount")
    @MethodSource
    void maxValueAmount(int[] input, int amount, int[] expected) {
        ArrayFunctions.rotate(input, amount);
        assertArrayEquals(expected, input);
    }

    private static Stream<Arguments> maxValueAmount() {
        return Stream.of(
                arguments(
                        new int[]{1, 2},
                        Integer.MAX_VALUE,
                        new int[]{2, 1}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        Integer.MAX_VALUE,
                        new int[]{3, 1, 2}
                ),
                arguments(
                        new int[]{1, 2, 3, 4},
                        Integer.MAX_VALUE,
                        new int[]{4, 1, 2, 3}
                )
        );
    }

    @DisplayName("Integer.MIN_VALUE amount")
    @MethodSource
    void minValueAmount(int[] input, int amount, int[] expected) {
        ArrayFunctions.rotate(input, amount);
        assertArrayEquals(expected, input);
    }

    private static Stream<Arguments> minValueAmount() {
        return Stream.of(
                arguments(
                        new int[]{1, 2},
                        Integer.MIN_VALUE,
                        new int[]{1, 2}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        Integer.MIN_VALUE,
                        new int[]{3, 1, 2}
                ),
                arguments(
                        new int[]{1, 2, 3, 4},
                        Integer.MIN_VALUE,
                        new int[]{1, 2, 3, 4}
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
                ),
                arguments(
                        new int[]{1},
                        Integer.MAX_VALUE,
                        new int[]{1}
                ),
                arguments(
                        new int[]{1},
                        Integer.MIN_VALUE,
                        new int[]{1}
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
        ArrayFunctions.rotate(a, Integer.MAX_VALUE);
        ArrayFunctions.rotate(a, Integer.MIN_VALUE);

        assertArrayEquals(new int[0], a);
    }

}
