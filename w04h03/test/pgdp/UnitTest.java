package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.convexhull.QuickHull.*;

class UnitTest {

    @Test
    @DisplayName("Testing combineHulls method for open hulls.")
    public void testCombineHullsOpenHull() {
        int[][] firstHull = new int[][]{{4, 3}, {3, 5}, {1, 6}};
        int[][] secondHull = new int[][]{{1, 6}, {-2, 5}, {-3, 2}};
        int[][] expected = new int[][]{{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = combineHulls(firstHull, secondHull);
        assertArrayEquals(expected, actual, "Points in hull are not correct! Got: " + Arrays.deepToString(actual) + " Expected: " + Arrays.deepToString(expected));
    }

    @Test
    @DisplayName("Testing combineHulls method for closed hulls.")
    public void testCombineHulls() {
        int[][] firstHull = new int[][]{{-3, 2}, {-1, 0}, {3, -1}, {4, 3}};
        int[][] secondHull = new int[][]{{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] expected = new int[][]{{-3, 2}, {-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = combineHulls(firstHull, secondHull);
        assertArrayEquals(expected, actual, "Points in hull are not correct! Got:" + Arrays.deepToString(actual) + " Expected: " + Arrays.deepToString(expected));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("[OPTIONAL] Testing combineHulls edge-cases - You do not have to pass this test because secondHull always starts with the last element of lastHull")
    public void testEmptyHulls(int[][] firstHull, int[][] secondHull, int[][] expected, String message) {
        int[][] actual = combineHulls(firstHull, secondHull);
        assertArrayEquals(expected, actual, message + " Got: " + Arrays.deepToString(actual));
    }

    public static Stream<Arguments> testEmptyHulls() {
        return Stream.of(
                Arguments.arguments(
                        new int[][]{},
                        new int[][]{{1, 2}, {2, 3}},
                        new int[][]{{1, 2}, {2, 3}},
                        "Incorrect result when first hull is empty."
                ),
                Arguments.arguments(
                        new int[][]{{1, 2}, {2, 3}},
                        new int[][]{},
                        new int[][]{{1, 2}, {2, 3}},
                        "Incorrect result when second hull is empty."
                ),
                Arguments.arguments(
                        new int[][]{},
                        new int[][]{},
                        new int[][]{},
                        "Incorrect result when both hulls are empty."
                )
        );
    }

    @Test
    @DisplayName("Testing quickHullLeftOf method.")
    public void testQuickHullLeftOf() {
        int[][] points = new int[][]{
                {-3, 2},
                {-2, 1}, {-2, 3}, {-2, 5},
                {-1, 0}, {-1, 4},
                {0, 4},
                {1, 1}, {1, 4}, {1, 6},
                {2, 3}, {2, 5},
                {3, -1}, {3, 2}, {3, 5},
                {4, 3}
        };
        int[] p = new int[]{-3, 2};
        int[] q = new int[]{4, 3};
        int[][] expected = new int[][]{{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = quickHullLeftOf(points, p, q);
        assertArrayEquals(expected, actual, "Points in hull are not correct! Got: " + Arrays.deepToString(actual) + " Expected: " + Arrays.deepToString(expected));
    }

    @Test
    @DisplayName("Testing quickHull method")
    public void testQuickHull() {
        int[][] points = new int[][]{
                {-3, 2},
                {-2, 1}, {-2, 3}, {-2, 5},
                {-1, 0}, {-1, 4},
                {0, 4},
                {1, 1}, {1, 4}, {1, 6},
                {2, 3}, {2, 5},
                {3, -1}, {3, 2}, {3, 5},
                {4, 3}
        };

        int[][] expected = new int[][]{{-3, 2}, {-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] expectedAlternative = new int[][]{{-3, 2}, {-2, 1}, {-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = quickHull(points);

        /* You can include or exclude a point that is on a line between to other points
         Both answers will be graded as correct:
         https://zulip.in.tum.de/#narrow/stream/1451-PGdP-W04H03/topic/.C3.9Cberfl.C3.BCssige.20Ecken/near/785061
        */
        boolean equals = Arrays.deepEquals(expected, actual) || Arrays.deepEquals(expectedAlternative, actual);
        Assertions.assertTrue(equals, "Output of quickHull is not correct! Got: " + Arrays.deepToString(actual) + " Expected: " + Arrays.deepToString(expected) + " or " + Arrays.deepToString(expectedAlternative));

    }

}