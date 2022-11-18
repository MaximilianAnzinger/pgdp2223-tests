package pgdp.megamerge;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class MegaMergeSortTest {
    @ParameterizedTest
    @DisplayName("Correct sorting")
    @MethodSource
    void correctSorting(int[] a, int div, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).megaMergeSort(a, div));
    }

    private static ArrayList<Arguments> correctSorting() {
        int[][] arrays = new int[][]{
                {7, 6, 1, 2, 5, 9, 3, 4, 8},
                {8, -6, 3, Integer.MAX_VALUE, Integer.MIN_VALUE, 2, 8, 0, -10, 11, 0},
                {1, 8, 6, -3, 7, -89, 784632, Integer.MIN_VALUE, 0, 0, 0, -1, 69, 2, 12345, -1}
        };

        ArrayList<Arguments> inputs = new ArrayList<>();

        for (int[] a : arrays) {
            int[] result = Arrays.copyOf(a, a.length);
            Arrays.sort(result);
            for (int i = 2; i <= a.length; i++) {
                inputs.add(arguments(a, i, result));
            }
            inputs.add(arguments(a, 1000, result));
        }

        return inputs;
    }

    @Test
    @DisplayName("Empty range")
    void emptyRange() {
        assertArrayEquals(new int[0], (new MegaMergeSort()).megaMergeSort(new int[10], 2, 5, 5));
    }

    @Test
    @DisplayName("Empty array")
    void emptyArray() {
        assertArrayEquals(new int[0], (new MegaMergeSort()).megaMergeSort(new int[0], 5));
    }

    @ParameterizedTest
    @DisplayName("Array split")
    @MethodSource
    void arraySplit(int[] a, int div, int[][] expectedSplit) {
        MergeWrapper wrapper = new MergeWrapper();
        wrapper.megaMergeSort(a, div);

        int[][] actualSplit = wrapper.split();

        if (!Arrays.deepEquals(expectedSplit, actualSplit)) {
            String msg = "Incorrect split!\n" +
                    "Expected:\t" + Arrays.deepToString(expectedSplit) +
                    "\nActual:\t\t" + Arrays.deepToString(actualSplit);
            fail(msg);
        }
    }

    private static Stream<Arguments> arraySplit() {
        return Stream.of(
                arguments(
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8},
                        3,
                        new int[][]{
                                {1, 2, 3}, {4, 5, 6}, {7, 8},
                        }
                ),
                arguments(
                        new int[]{1, 2, 3},
                        3,
                        new int[][]{
                                {1}, {2}, {3}
                        }
                ),
                arguments(
                        new int[]{7, 8},
                        3,
                        new int[][]{
                                {7}, {8}, {}
                        }
                ),
                arguments(
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
                        3,
                        new int[][]{
                                {1, 2, 3, 4}, {5, 6, 7}, {8, 9, 10}
                        }
                ),
                arguments(
                        new int[]{1, 2},
                        5,
                        new int[][]{
                                {1}, {2}, {}, {}, {}
                        }
                ),
                arguments(
                        new int[]{1, 3, 4, 2},
                        2,
                        new int[][]{
                                {1, 3}, {2, 4}
                        }
                )
        );
    }
}

class MergeWrapper extends MegaMergeSort {
    private int[][] split = new int[0][];

    @Override
    protected int[] merge(int[][] arrays, int from, int to) {
        split = arrays;
        return super.merge(arrays, from, to);
    }

    public int[][] split() {
        return split;
    }
}
