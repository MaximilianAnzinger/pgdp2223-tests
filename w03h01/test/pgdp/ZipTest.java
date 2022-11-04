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

public class ZipTest {
    @Test
    @DisplayName("Example from Artemis")
    void artemisExample() {
        assertArrayEquals(
                new int[]{1, 2, 3, 4},
                ArrayFunctions.zip(
                        new int[]{1, 3},
                        new int[]{2, 4}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Arrays with different lengths")
    @MethodSource
    void differentLengthArrays(int[] a, int[] b, int[] r) {
        assertArrayEquals(r, ArrayFunctions.zip(a, b));
    }

    private static Stream<Arguments> differentLengthArrays() {
        return Stream.of(
                arguments(
                        new int[]{1, 3},
                        new int[]{2, 4, 5},
                        new int[]{1, 2, 3, 4, 5}
                ),
                arguments(
                        new int[]{1, 3, 4, 5},
                        new int[]{2},
                        new int[]{1, 2, 3, 4, 5}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Empty arrays")
    @MethodSource
    void emptyArrays(int[] a, int[] b, int[] r) {
        assertArrayEquals(r, ArrayFunctions.zip(a, b));
    }

    private static Stream<Arguments> emptyArrays() {
        return Stream.of(
                arguments(
                        new int[]{},
                        new int[]{1, -2, 3},
                        new int[]{1, -2, 3}
                ),
                arguments(
                        new int[]{1},
                        new int[]{},
                        new int[]{1}
                ),
                arguments(
                        new int[]{},
                        new int[]{},
                        new int[]{}
                )
        );
    }

    @Test
    @DisplayName("IdenticalNumbers")
    void identicalNumbers() {
        assertArrayEquals(
                new int[]{1, 1, 1,},
                ArrayFunctions.zip(
                        new int[]{1, 1},
                        new int[]{1}
                )
        );
    }
}
