package pgdp.convexhull;
import org.junit.jupiter.api.Test;
import pgdp.convexhull.QuickHull;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class QuickHullTest {

    public static int test = 6;
    @Test
    public void testMultipleSamePoints() {
        int[][] input = {
                {0,0}, {0,1}, {0,2}, {0,3}, {0,4},
                {1,0}, {1,1}, {1,2}, {1,3}, {1,4},
                {2,0}, {2,1}, {2,2}, {2,3}, {2,4},
                {3,0}, {3,1}, {3,2}, {3,3}, {3,4},
                {4,0}, {4,1}, {4,2}, {4,3}, {4,4},
                //again
                {0,0}, {0,1}, {0,2}, {0,3}, {0,4},
                {1,0}, {1,1}, {1,2}, {1,3}, {1,4},
                {2,0}, {2,1}, {2,2}, {2,3}, {2,4},
                {3,0}, {3,1}, {3,2}, {3,3}, {3,4},
                {4,0}, {4,1}, {4,2}, {4,3}, {4,4},
                //and again
                {0,0}, {0,1}, {0,2}, {0,3}, {0,4},
                {1,0}, {1,1}, {1,2}, {1,3}, {1,4},
                {2,0}, {2,1}, {2,2}, {2,3}, {2,4},
                {3,0}, {3,1}, {3,2}, {3,3}, {3,4},
                {4,0}, {4,1}, {4,2}, {4,3}, {4,4}
        };
        int[][] expectedOutput = {
                {0,0}, {4,0}, {4,4}, {0, 4}, {0, 0}
        };
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.quickHull(input)))) {
            System.out.println("Test 1/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.quickHull(input)));
        } else {
            System.out.println("Test 1/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.quickHull(input));
    }

    @Test
    public void testTwoPointsOnRepeat() {
        int[][] input = {
                {-3, 5}, {5, -3}, {-3, 5}, {5, -3}, {-3, 5}, {5, -3}, {-3, 5}, {5, -3}, {-3, 5}, {5, -3},
        };
        int[][] expectedOutput = {{-3, 5}, {5, -3}, {-3, 5}};
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.quickHull(input)))) {
            System.out.println("Test 2/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.quickHull(input)));
        } else {
            System.out.println("Test 2/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.quickHull(input));
    }

    @Test
    public void testTwoPointsWithSameDistanceFromPQ() {
        int[][] input = {
                {0,0}, {10,10},
                {1,5}, {3,7},
                {7,3}, {9,5},
                {1,1}, {5,5}, {0,0}, {2,4}, {7,8}, {9,9}, {2,5}
        };
        int[][] expectedOutput = {
                {0,0},
                {7,3}, {9,5},
                {10,10},
                {3,7}, {1,5},
                {0,0}
        };
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.quickHull(input)))) {
            System.out.println("Test 3/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.quickHull(input)));
        } else {
            System.out.println("Test 3/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.quickHull(input));
    }

    @Test
    public void testCombineHulls() {
        int[][] in1 = {
                {0,0},
                {7,3}, {9,5},
                {10,10},
        };
        int[][] in2 = {
                {10,10},
                {3,7}, {1,5},
                {0,0}
        };
        int[][] expectedOutput = {
                {0,0},
                {7,3}, {9,5},
                {10,10},
                {3,7}, {1,5},
                {0,0}
        };
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.combineHulls(in1, in2)))) {
            System.out.println("Test 4/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.combineHulls(in1, in2)));
        } else {
            System.out.println("Test 4/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.combineHulls(in1, in2));
    }

    @Test
    public void testTwoPoints() {
        int[][] input = {
                { 0,0}, {-3,-4}
        };
        int[][] expectedOutput = {
                {-3,-4}, { 0,0}, {-3,-4}
        };
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.quickHull(input)))) {
            System.out.println("Test 5/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.quickHull(input)));
        } else {
            System.out.println("Test 5/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.quickHull(input));
    }

    @Test
    public void testCombineHullsWithShortArrays() {
        int[][] in1 = {
                {0,0},
                {7,3}
        };
        int[][] in2 = {
                {7, 3}
        };
        int[][] expectedOutput = {
                {0,0},
                {7,3}
        };
        if(!Arrays.deepToString(expectedOutput).equals(Arrays.deepToString(QuickHull.combineHulls(in1, in2)))) {
            System.out.println("Test 6/" + test + ": " + "Expected:\n" + Arrays.deepToString(expectedOutput) + "\n but was returned:\n" + Arrays.deepToString(QuickHull.combineHulls(in1, in2)));
        } else {
            System.out.println("Test 6/" + test + " behaves like expected, great job!");
        }
        assertArrayEquals(expectedOutput, QuickHull.combineHulls(in1, in2));
    }
}