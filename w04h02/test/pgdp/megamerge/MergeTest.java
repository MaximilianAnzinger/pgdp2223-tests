package pgdp.megamerge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MergeTest {
    @ParameterizedTest
    @DisplayName("Arrays with equal length")
    @MethodSource
    void equalLengthArrays(int[] a, int[] b, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).merge(a, b));
    }

    private static Stream<Arguments> equalLengthArrays() {
        return Stream.of(
                arguments(
                        new int[]{1, 3},
                        new int[]{2, 4},
                        new int[]{1, 2, 3, 4}
                ),
                arguments(
                        new int[]{-2, 5, 10},
                        new int[]{-3, 0, 1},
                        new int[]{-3, -2, 0, 1, 5, 10}
                ),
                arguments(
                        new int[]{Integer.MIN_VALUE, 15, Integer.MAX_VALUE},
                        new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, 10},
                        new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, 10, 15, Integer.MAX_VALUE}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Arrays with different lengths")
    @MethodSource
    void differentLengthArrays(int[] a, int[] b, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).merge(a, b));
    }

    private static Stream<Arguments> differentLengthArrays() {
        return Stream.of(
                arguments(
                        new int[]{0, 0, 0, 0, 0},
                        new int[]{1, 2, 3},
                        new int[]{0, 0, 0, 0, 0, 1, 2, 3}
                ),
                arguments(
                        new int[]{1, 2, 4, 5},
                        new int[]{3},
                        new int[]{1, 2, 3, 4, 5}
                ),
                arguments(
                        new int[]{Integer.MIN_VALUE},
                        new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE},
                        new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}
                ),
                arguments(
                        new int[]{-4, -3, 1},
                        new int[]{5},
                        new int[]{-4, -3, 1, 5}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Empty arrays")
    @MethodSource
    void emptyArrays(int[] a, int[] b, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).merge(a, b));
    }

    private static Stream<Arguments> emptyArrays() {
        return Stream.of(
                arguments(
                        new int[0],
                        new int[0],
                        new int[0]
                ),
                arguments(
                        new int[0],
                        new int[]{0, 1, 2},
                        new int[]{0, 1, 2}
                ),
                arguments(
                        new int[]{0},
                        new int[0],
                        new int[]{0}
                )
        );
    }
}
