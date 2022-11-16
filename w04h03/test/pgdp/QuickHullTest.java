package w04h03.test.pgdp;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import w04h03.src.pgdp.convexhull.QuickHull;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class QuickHullTest {
    /**
     * Tests the example for creating a closed convex hull.
     * See here for a visualization of the example:
     * https://artemis.ase.in.tum.de/api/files/markdown/Markdown_2022-11-09T23-33-46-753_52aecae2.png
     */
    @Test
    @DisplayName("Testing combineHulls method for open hulls.")
    public void testCombineHullsOpenHull() {
        int[][] firstHull = new int[][] {{4, 3}, {3, 5}, {1, 6}};
        int[][] secondHull = new int[][] {{1, 6}, {-2, 5}, {-3, 2}};
        int[][] expected = new int[][] {{4, 3}, {3, 5}, {1, 6}, {-2, 5}, {-3, 2}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing combineHulls method for two points.")
    public void testCombineHullsOpenHull2() {
        int[][] firstHull = new int[][] {{0,1},{1,1}};
        int[][] secondHull = new int[][] {{1,0},{0,1}};
        int[][] expected = new int[][] {{0,1},{1,1},{0,1}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);

        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing combineHulls method for points on one line points.")
    public void testCombineHullsOpenHull3() {
        int[][] firstHull = new int[][] {{0,1},{0,1},{1,1}};
        int[][] secondHull = new int[][] {{1,1},{0,1},{0,1}};
        int[][] expected = new int[][] {{0,1},{0,1},{1,1},{0,1},{0,1}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);

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
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
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
        int[][] actual = QuickHull.quickHullLeftOf(points, p, q);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Testing quickHullLeftOf method with 2 input.")
    public void testQuickHullLeftOf2(){
        int[][] points = new int[][] {
                {-3, 2},
                {4, 3}
        };
        int[] p = new int[] {-3, 2};
        int[] q = new int[] {4, 3};
        int[][] expected = new int[][] {{4,3},{-3,2}};
        int[][] actual = QuickHull.quickHullLeftOf(points, p, q);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }


    @Test
    @DisplayName("Testing quickHull method 2 array")
    public void testQuickHull2() {
        int[][] points = new int[][]{
                {-3, 2},
                {4, 3}
        };
        int[][] expected = new int[][]{{-3, 2}, {4, 3}, {-3, 2}};
        int[][] actual = QuickHull.quickHull(points);


        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
    }

    @Test
    @DisplayName("Testing quickHull method on one line")
    public void testQuickHull3(){
        int[][] points = new int[][] {
                {1, 1},
                {2, 2},{3,3}
        };
        int[][] expected = new int[][] { {1, 1},
                {3,3}, {1, 1}};
        int[][] actual = QuickHull.quickHull(points);



        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }

        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");

    }

    @Test
    @DisplayName("Testing quickHull method 3 one x=0")
    public void testQuickHull4() {
        int[][] points = new int[][]{
                {0, 1},
                {0, 2}, {0, 3}, {1, 2}
        };
        int[][] expected = new int[][]{{0, 1},
                {1, 2}, {0, 3}, {0, 1}};
        int[][] actual = QuickHull.quickHull(points);


        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
    }

    @Test
    @DisplayName("Testing quickHull method 3 one x=0")
    public void testQuickHull5(){
        int[][] points = new int[][] {
                {10, 1},
                {10, 2},{10,3},{9,15}
        };
        int[][] expected = new int[][] { {9,15},
                {10, 1},{10,3},{9,15}};
        int[][] actual = QuickHull.quickHull(points);



        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }




        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");


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
        int[][] actual = QuickHull.quickHull(points);

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


    /*
     * Dominik Gleissner Tests
     * die edgecases haben ggf. auch andere erlaubte outputs
     * sind zwei punkte auf einer linie, wo wird hier nur eine variante getestet
     * also auch obwohl ein test fehlschlägt, kann er richtig sein
     */

    @Test//DG_TEST
    @DisplayName("combine 2 empty")
    public void testCombine1(){
        int[][] firstHull = new int[][] {};
        int[][] secondHull = new int[][] {};
        int[][] expected = new int[][] {};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine 1 empty at position 1")
    public void testCombine2(){
        int[][] firstHull = new int[][] {};
        int[][] secondHull = new int[][] {{1,1}, {1,2}};
        int[][] expected = new int[][] {{1,1}, {1,2}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine 1 empty at position 2")
    public void testCombine3(){
        int[][] firstHull = new int[][] {{1,1}, {1,2}};
        int[][] secondHull = new int[][] {};
        int[][] expected = new int[][] {{1,1}, {1,2}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine hulls with same length")
    public void testCombine4(){
        int[][] firstHull = new int[][] {{-1,1}, {0,0}, {1,2}};
        int[][] secondHull = new int[][] {{1,2}, {2,3}, {4,4}};
        int[][] expected = new int[][] {{-1,1}, {0,0}, {1,2}, {2,3}, {4,4}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine hulls with different length")
    public void testCombine5(){
        int[][] firstHull = new int[][] {{-1,1}, {0,0}, {1,2}};
        int[][] secondHull = new int[][] {{1,2}, {2,3}, {4,4}, {5,8}};
        int[][] expected = new int[][] {{-1,1}, {0,0}, {1,2}, {2,3}, {4,4}, {5,8}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine hulls with hulls on one line")
    public void testCombine6(){
        int[][] firstHull = new int[][] {{0,0}, {1,1}, {2,2}};
        int[][] secondHull = new int[][] {{2,2}, {3,3}, {4,4}};
        int[][] expected = new int[][] {{0,0}, {1,1}, {2,2}, {3,3}, {4,4}};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("combine hulls with not matching endings")
    public void testCombine7(){
        int[][] firstHull = new int[][] {{0,0}, {1,1}};
        int[][] secondHull = new int[][] {{2,2}, {3,3}};
        int[][] expected = new int[][] {};
        int[][] actual = QuickHull.combineHulls(firstHull, secondHull);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with multiple points diagonal")
    public void quickHull1(){
        int[][] points = new int[][] {{1,1},{2,2},{3,3},{4,4}};
        int[][] expected = new int[][] {{1,1},{4,4},{1,1}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with 2 points diagonal")
    public void quickHull2(){
        int[][] points = new int[][] {{1,1},{4,4}};
        int[][] expected = new int[][] {{1,1},{4,4},{1,1}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with multiple points horizontally")
    public void quickHull3(){
        int[][] points = new int[][] {{1,0},{2,0},{3,0},{4,0}};
        int[][] expected = new int[][] {{1,0},{4,0},{1,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with 2 points horizontally")
    public void quickHull4(){
        int[][] points = new int[][] {{1,0},{4,0}};
        int[][] expected = new int[][] {{1,0},{4,0},{1,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with multiple points upwards")
    public void quickHull5(){
        int[][] points = new int[][] {{0,0},{0,1},{0,2},{0,3}};
        int[][] expected = new int[][] {{0,0},{0,0},{0,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull on one line with 2 points upwards")
    public void quickHull6(){
        int[][] points = new int[][] {{0,0},{0,3}};
        int[][] expected = new int[][] {{0,0},{0,0},{0,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull simple triangle")
    public void quickHull7(){
        int[][] points = new int[][] {{0,0},{3,0},{1,2}};
        int[][] expected = new int[][] {{0,0},{3,0},{1,2},{0,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull simple trapez")
    public void quickHull8(){
        int[][] points = new int[][] {{0,0},{3,0},{1,2},{2,2}};
        int[][] expected = new int[][] {{0,0},{3,0},{2,2},{1,2},{0,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull one point")
    public void quickHull9(){
        int[][] points = new int[][] {{1,1}};
        int[][] expected = new int[][] {{1,1},{1,1},{1,1}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }

    @Test//DG_TEST
    @DisplayName("quickHull wild mix")
    public void quickHull10(){
        int[][] points = new int[][] {{0,0},{6,-2}, {6,0}, {6,2}, {5,4}, {3,5}, {1,7}, {1,-4}, {3,-6}, {2,2}, {4,1}, {1,1}};
        int[][] expected = new int[][] {{0,0},{1,-4},{3,-6},{6,-2},{6,2},{5,4},{1,7},{0,0}};
        int[][] actual = QuickHull.quickHull(points);
        boolean matchesExpected = true;
        for (int i = 0; i < expected.length; i++) {
            int[] expectedPoint = expected[i];
            int[] actualPoint = actual[i];
            if (expectedPoint[0] != actualPoint[0] || expectedPoint[1] != actualPoint[1]) {
                matchesExpected = false;
                break;
            }
        }
        Assertions.assertFalse(!matchesExpected , "Output of quickHull is not correct!");
    }
}