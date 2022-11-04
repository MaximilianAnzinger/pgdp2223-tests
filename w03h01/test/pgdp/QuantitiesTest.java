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

public class QuantitiesTest {
    @ParameterizedTest
    @DisplayName("Examples from Artemis")
    @MethodSource
    void artemisExamples(int[] a, int[][] r) {
        assertArrayEquals(r, ArrayFunctions.quantities(a));
    }

    private static Stream<Arguments> artemisExamples() {
        return Stream.of(
                arguments(
                        new int[]{1, 1, 2, 1, 3, 2, 1},
                        new int[][]{
                                {1, 4},
                                {2, 2},
                                {3, 1}
                        }
                ),
                arguments(
                        new int[]{2, 0, 3, 0, 2, 5, 5, 3, 2, 0, 0, 0},
                        new int[][]{
                                {2, 3},
                                {0, 5},
                                {3, 2},
                                {5, 2}
                        }
                )
        );
    }

    @Test
    @DisplayName("Unique elements")
    void uniqueElements() {
        assertArrayEquals(
                new int[][]{
                        {-2147483648, 1},
                        {2147483647, 1},
                        {0, 1},
                        {1, 1},
                        {2, 1},
                        {3, 1}
                },
                ArrayFunctions.quantities(
                        new int[]{-2147483648, 2147483647, 0, 1, 2, 3}
                )
        );
    }

    @Test
    @DisplayName("Empty array")
    void emptyArray() {
        assertArrayEquals(new int[0][], ArrayFunctions.quantities(new int[0]));
    }
}
