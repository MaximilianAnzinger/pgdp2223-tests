package pgdp.convexhull;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

public class OpenConvexHullExample {
    /**
     * Tests the example for creating an open convex hull.
     * See here for a visualization of the example:
     * https://artemis.ase.in.tum.de/api/files/markdown/Markdown_2022-11-09T23-29-59-040_44379b52.png
     */
    @Test()
    void quickHullLeftOfExample() {
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
        final var expectedLeftHull = new int[][]{
                q,
                {3, 5},
                {1, 6},
                {-2, 5},
                p
        };
        final var actual = QuickHull.quickHullLeftOf(points, p, q);
        assertArrayEquals(expectedLeftHull, actual);
    }


}
