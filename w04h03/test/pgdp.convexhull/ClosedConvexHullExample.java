package pgdp.convexhull;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.util.Arrays;

class ClosedConvexHullExample {
    /**
     * Tests the example for creating a closed convex hull.
     * See here for a visualization of the example:
     * https://artemis.ase.in.tum.de/api/files/markdown/Markdown_2022-11-09T23-33-46-753_52aecae2.png
     */
    @Test()
    void quickHullExample() {
        int[] q = {4, 3};
        int[] p = {-3, 2};
        final var points = new int[][]{
                {-2, 3},
                {-2, 1},
                {-1, 4},
                {-1, 0},
                p,
                {-2, 5},
                {1, 6},
                {1, 4},
                {1, 1},
                {3, 5},
                {2, 3},
                {3, 2},
                q,
                {3, -1}
        };
        final var expectedHull = new int[][]{
                {-3, 2},
                {-2, 1},
                {-1, 0},
                {3, -1},
                {4, 3},
                {3, 5},
                {1, 6},
                {-2, 5},
                {-3, 2}
        };

        final var alternativeExpectedHull = new int[][]{
                {-3, 2},
                {-1, 0},
                {3, -1},
                {4, 3},
                {3, 5},
                {1, 6},
                {-2, 5},
                {-3, 2}
        };
        final var actual = QuickHull.quickHull(points);

        assertTrue(Arrays.deepEquals(actual, expectedHull) || Arrays.deepEquals(actual, alternativeExpectedHull));
    }
}