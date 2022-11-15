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

public class MergeRecursiveTest {
    @ParameterizedTest
    @DisplayName("Merge arrays")
    @MethodSource
    void mergeArrays(int[][] arrays, int from, int to, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).merge(arrays, from, to));
    }

    private static Stream<Arguments> mergeArrays() {
        return Stream.of(
                arguments(
                        new int[][]{
                                {1},
                                {2},
                                {3},
                                {4}
                        },
                        0,
                        4,
                        new int[]{1, 2, 3, 4}
                ),
                arguments(
                        new int[][]{
                                {3, 5},
                                {Integer.MAX_VALUE},
                                {-3, 8, 12},
                                {Integer.MIN_VALUE, Integer.MAX_VALUE},
                                {7, 11},
                                {Integer.MIN_VALUE},
                                {-1, 0, 5}
                        },
                        0,
                        7,
                        new int[]{
                                Integer.MIN_VALUE, Integer.MIN_VALUE,
                                -3, -1, 0, 3, 5, 5, 7, 8, 11, 12,
                                Integer.MAX_VALUE, Integer.MAX_VALUE
                        }
                ),
                arguments(
                        new int[][]{
                                {-7, 3, 10},
                                {0, 0, 0},
                                {2, 5, 6},
                                {1, 2, 3},
                                {120, 121},
                                {-1, 1},
                                {-5, 10, 15},
                                {2}
                        },
                        1,
                        5,
                        new int[]{0, 0, 0, 1, 2, 2, 3, 5, 6, 120, 121}
                ),
                arguments(
                        new int[][]{
                                {0},
                                {1, 2},
                                {3, 4},
                                {0}
                        },
                        1,
                        2,
                        new int[]{1, 2}
                )
        );
    }

    @Test
    @DisplayName("Empty range")
    void emptyRange() {
        assertArrayEquals(new int[0], (new MegaMergeSort()).merge(new int[10][10], 5, 5));
    }

    @ParameterizedTest
    @DisplayName("Empty arrays")
    @MethodSource
    void emptyArrays(int[][] array, int[] result) {
        assertArrayEquals(result, (new MegaMergeSort()).merge(array, 0, array.length));
    }

    private static Stream<Arguments> emptyArrays() {
        return Stream.of(
                arguments(
                        new int[10][0],
                        new int[0]
                ),
                arguments(
                        new int[0][10],
                        new int[0]
                ),
                arguments(
                        new int[][]{
                                {},
                                {3, 5},
                                {},
                                {},
                                {1},
                                {},
                                {2, 4},
                                {}
                        },
                        new int[]{1, 2, 3, 4, 5}
                )
        );
    }

    @ParameterizedTest
    @DisplayName("Merge order")
    @MethodSource
    void mergeOrder(int[][] array, int from, int to, int[][] expectedMergeSteps) {
        MergeWrapper wrapper = new MergeWrapper();

        for (int fromStep = to; fromStep >= from; fromStep--)
            wrapper.merge(array, fromStep, to);

        int[][] mergeSteps = wrapper.mergeSteps();
        int j = 0;

        assertArrayEquals(expectedMergeSteps, mergeSteps, "\nExpected merge steps:\t" + Arrays.deepToString(expectedMergeSteps) + "\nActual merge steps:\t\t" + Arrays.deepToString(mergeSteps));
    }

    private static void failMergeOrder(int[][] actual, int[][] expected, String msg) {
        msg += "\nExpected merge steps:\t" + Arrays.deepToString(expected) +
                "\nActual merge steps:\t\t" + Arrays.deepToString(actual);
        fail(msg);
    }

    private static Stream<Arguments> mergeOrder() {
        return Stream.of(
                arguments(
                        new int[][]{
                                {1},
                                {2},
                                {3},
                                {4}
                        },
                        0,
                        4,
                        new int[][]{
                                {},
                                {4},
                                {3, 4},
                                {2, 3, 4},
                                {1, 2, 3, 4}
                        }
                ),
                arguments(
                        new int[][]{
                                {-7, 3, 10},
                                {0, 0, 0},
                                {2, 5, 6},
                                {1, 2, 3},
                                {120, 121},
                                {-1, 1},
                                {-5, 10, 15}
                        },
                        1,
                        5,
                        new int[][]{
                                {},
                                {120, 121},
                                {1, 2, 3, 120, 121},
                                {1, 2, 2, 3, 5, 6, 120, 121},
                                {0, 0, 0, 1, 2, 2, 3, 5, 6, 120, 121}
                        }
                ),
                arguments(
                        new int[][]{
                                {1}
                        },
                        0,
                        1,
                        new int[][]{
                                {},
                                {1}
                        }
                )
        );
    }
}

class MergeWrapper extends MegaMergeSort {
    private final ArrayList<int[]> mergeSteps = new ArrayList<>();

    @Override
    protected int[] merge(int[][] arrays, int from, int to) {
        int[] result = super.merge(arrays, from, to);
        mergeSteps.add(result);
        return result;
    }

    public int[][] mergeSteps() {
        return mergeSteps.toArray(new int[0][]);
    }
}
