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

public class FilterTest {
    @ParameterizedTest
    @DisplayName("Correct filtering")
    @MethodSource
    void filter(int[] a, int min, int max, int[] r) {
        assertArrayEquals(r, ArrayFunctions.filter(a, min, max));
    }

    private static Stream<Arguments> filter() {
        return Stream.of(
                arguments(
                        new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                        3,
                        7,
                        new int[]{3, 4, 5, 6, 7}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        3,
                        3,
                        new int[]{3}
                ),
                arguments(
                        new int[]{1, 2, 3},
                        -5,
                        5,
                        new int[]{1, 2, 3}
                ),
                arguments(
                        new int[]{2147483647, -2147483648, -5, 8, 10},
                        -2147483648,
                        2147483647,
                        new int[]{2147483647, -2147483648, -5, 8, 10}
                )
        );
    }

    @Test
    @DisplayName("Min larger than max")
    void minLargerThanMax() {
        assertArrayEquals(
                new int[0],
                ArrayFunctions.filter(
                        new int[]{-5, -3, 0, 1, 2, 3},
                        1,
                        -1
                )
        );
    }

    @Test
    @DisplayName("Empty array")
    void emptyArray() {
        assertArrayEquals(
                new int[0],
                ArrayFunctions.filter(
                        new int[0],
                        -10,
                        10
                )
        );
    }
}
