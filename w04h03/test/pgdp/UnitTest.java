package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.convexhull.QuickHull.*;

class UnitTest {

    @Test
    @DisplayName("Testing combineHulls method for open hulls.")
    public void testCombineHullsOpenHull() {
        int[][] firstHull = new int[][] {{4, 3}, {3, 5}, {1, 6}};
        int[][] secondHull = new int[][] {{1, 6}, {-2, 5}, {-3, 2}};
        int[][] expected = new int[][] {{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing combineHulls method for closed hulls.")
    public void testCombineHulls() {
        int[][] firstHull = new int[][] {{-3, 2}, {-1, 0}, {3, -1}, {4, 3}};
        int[][] secondHull = new int[][] {{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] expected = new int[][] {{-3, 2}, {-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing quickHullLeftOf method.")
    public void testQuickHullLeftOf(){
        int[][] points = new int[][] {
                {-3, 2},
                {-2, 1}, {-2, 3}, {-2, 5},
                {-1, 0}, {-1, 4},
                {0, 4},
                {1, 1}, {1, 4}, {1, 6},
                {2, 3}, {2, 5},
                {3, -1}, {3, 2}, {3, 5},
                {4, 3}
        };
        int[] p = new int[] {-3, 2};
        int[] q = new int[] {4, 3};
        int[][] expected = new int[][] {{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = quickHullLeftOf(points, p, q);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing quickHull method")
    public void testQuickHull(){
        int[][] points = new int[][] {
                {-3, 2},
                {-2, 1}, {-2, 3}, {-2, 5},
                {-1, 0}, {-1, 4},
                {0, 4},
                {1, 1}, {1, 4}, {1, 6},
                {2, 3}, {2, 5},
                {3, -1}, {3, 2}, {3, 5},
                {4, 3}
        };
        int[][] expected = new int[][] {{-3, 2}, {-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] expectedAlternative = new int[][] {{-3, 2}, {-2, 1},{-1, 0}, {3, -1}, {4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = quickHull(points);

        /* You can include or exclude a point that is on a line between to other points
         Both answers will be graded as correct:
         https://zulip.in.tum.de/#narrow/stream/1451-PGdP-W04H03/topic/.C3.9Cberfl.C3.BCssige.20Ecken/near/785061
        */

        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        
        boolean matchesAlternative = true;
        for (int i = 0; i < actual.length; i++) {
            int[] expectedPoint = expectedAlternative[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesAlternative = false;
                break;
            }
        }


        Assertions.assertFalse(!matchesExpected && !matchesAlternative, "Output of quickHull is not correct!");


    }
}