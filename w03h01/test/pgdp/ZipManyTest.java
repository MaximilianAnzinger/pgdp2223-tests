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

public class ZipManyTest {
    @Test
    @DisplayName("Example from Artemis")
    void artemisExample() {
        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5, 6},
                ArrayFunctions.zipMany(new int[][]{
                        {1, 4},
                        {2, 5},
                        {3, 6}
                })
        );
    }

    @ParameterizedTest
    @DisplayName("Arrays with different lengths")
    @MethodSource
    void differentLengthArrays(int[][] a, int[] r) {
        assertArrayEquals(r, ArrayFunctions.zipMany(a));
    }

    private static Stream<Arguments> differentLengthArrays() {
        return Stream.of(
                arguments(
                        new int[][]{
                                {1},
                                {2, 5},
                                {3, 6, 7, 8, 9, 10},
                                {4}
                        },
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
                ),
                arguments(
                        new int[][]{
                                {1, 5, 7, 9, 10, 11, 12},
                                {2},
                                {3, 6, 8},
                                {4}
                        },
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}
                ),
                arguments(
                        new int[][]{
                                {1, 3},
                                {2, 4, 5}
                        },
                        new int[]{1, 2, 3, 4, 5}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Empty arrays")
    @MethodSource
    void emptyArrays(int[][] a, int[] r) {
        assertArrayEquals(r, ArrayFunctions.zipMany(a));
    }

    private static Stream<Arguments> emptyArrays() {
        return Stream.of(
                arguments(
                        new int[][]{
                                {1, 3},
                                {},
                                {-2},
                                {}
                        },
                        new int[]{1, -2, 3}
                ),
                arguments(
                        new int[0][],
                        new int[0]
                ),
                arguments(
                        new int[10][0],
                        new int[0]
                )
        );
    }

    @Test
    @DisplayName("Identical numbers")
    void identicalNumbers() {
        assertArrayEquals(
                new int[]{1, 1, 1, 1, 1},
                ArrayFunctions.zipMany(
                        new int[][]{
                                {1},
                                {1, 1, 1},
                                {1}
                        }
                )
        );
    }
}
