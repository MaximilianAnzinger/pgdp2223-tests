package pgdp.megamerge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    // Source: https://zulip.in.tum.de/#narrow/stream/1450-PGdP-W04H02/topic/to.20.3C.20from
    @DisplayName(".merge(int[][] arrays, int from, int to) should handle negativ from values")
    @Test
    void testNegativeFromValues() {
        int[][] arrays = new int[][]{
                new int[]{5},
                new int[]{6},
                new int[]{7},
        };

        int[] mergedArray = new MegaMergeSort().merge(arrays, -2, 2);
        
        // arrays[0] and arrays[1] are in the range of -2 and 2. 
        assertArrayEquals(mergedArray, new int[] { 5, 6 });
    }

    // Source: https://zulip.in.tum.de/#narrow/stream/1450-PGdP-W04H02/topic/to.20.3C.20from
    @DisplayName(".merge(int[][] arrays, int from, int to) should handle to values greater than the length of the arrays")
    @Test
    void testToValuesGreaterThanLength() {
        int[][] arrays = new int[][]{
            new int[]{5},
            new int[]{6},
            new int[]{7},
       };
       
       int[] mergedArray = new MegaMergeSort().merge(arrays, 0, 5);
       
       // arrays[0], arrays[1] and arrays[2] are in the range of 0 and 5.
       assertArrayEquals(mergedArray, new int[] { 5, 6, 7 });
    }

    // Source: https://zulip.in.tum.de/#narrow/stream/1450-PGdP-W04H02/topic/to.20.3C.20from
    @DisplayName(".merge(int[][] arrays, int from, int to) should handle a negative range")
    @Test
    void testNegativeRange() {
        int[][] arrays = new int[][]{
            new int[]{5},
            new int[]{6},
            new int[]{7},
       };
       
       int[] mergedArray = new MegaMergeSort().merge(arrays, -3, -1);
       
       // Nothings is in the range of -3 and -1. Therefore, the returned array
       // should be empty.
       assertArrayEquals(mergedArray, new int[] {});
    }

    // Source: https://zulip.in.tum.de/#narrow/stream/1450-PGdP-W04H02/topic/to.20.3C.20from
    @DisplayName(".merge(int[][] arrays, int from, int to) should handle a range is out of bounds")
    @Test
    void testRangeOutside() {
        int[][] arrays = new int[][]{
            new int[]{5},
            new int[]{6},
            new int[]{7},
       };
       
       int[] mergedArray = new MegaMergeSort().merge(arrays, 3, 5);
       
       // Nothings is in the range of 3 and 5. Therefore, the returned array
       // should be empty.
       assertArrayEquals(mergedArray, new int[] {});
    }

    // Source: https://zulip.in.tum.de/#narrow/stream/1450-PGdP-W04H02/topic/to.20.3C.20from
    @DisplayName(".merge(int[][] arrays, int from, int to) should handle a range where from is greater than to")
    @Test
    void testToIsSmallerThanFrom() {
        int[][] arrays = new int[][]{
            new int[]{5},
            new int[]{6},
            new int[]{7},
       };
       
       int[] mergedArray = new MegaMergeSort().merge(arrays, 2, 1);
       
       // Nothings is in the range of 2 and 1. Therefore, the returned array
       // should be empty.
       assertArrayEquals(mergedArray, new int[] {});
    }
}
