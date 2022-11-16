package w04h02.test.pgdp.megamerge;

import jdk.jfr.Name;
import org.junit.jupiter.api.*;
import w04h02.src.pgdp.megamerge.MegaMergeSort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class MegaMergeSortTestDG {

    //<editor-fold desc="Test setup stuff">
    private static final ByteArrayOutputStream out = new ByteArrayOutputStream() {
        // returns platform independent captured output
        @Override
        public synchronized String toString() {
            return super.toString().replace("\r", "");
        }
    };
    private static final PrintStream stdOut = System.out;

    @BeforeAll
    static void beforeAll() {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    static void afterAll() throws IOException {
        out.close();
        System.setOut(stdOut);
    }

    @AfterEach
    void tearDown() {
        out.reset();
    }
    //</editor-fold>

    //<editor-fold desc="Utility Methods">
    // Nicer way to construct arrays from varargs
    private int[] array(int... values) {
        return values;
    }

    private int[][] array2d(int[]... values) {
        return values;
    }

    private List<String> lines() {
        return out.toString().lines().toList();
    }

    private void assertArrayEquals(int[] actual, int[] expected, String message) {
        if (!Arrays.equals(expected, actual)) {
            fail(message + "\nExpected: " + Arrays.toString(expected) + "\nActual: " + Arrays.toString(actual));
        }
    }

    private void assertArrayEquals(int[][] actual, int[][] expected, String message) {
        if (!Arrays.deepEquals(expected, actual)) {
            fail(message + "\nExpected: " + Arrays.deepToString(expected) + "\nActual: " + Arrays.deepToString(actual));
        }
    }

    public static MegaMergeSort instance = new MegaMergeSort();

    @Test
    @Name("Should merge Artemis example")
    public void mergeTwoArraysArtemis() {
        assertArrayEquals(instance.merge(array(1, 3), array(2, 4)), array(1, 2, 3, 4), "Artemis example: ");
    }

    @Test
    @Name(" merge empty example")//Mayatest
    public void mergeTwoArraysempty() {
        assertArrayEquals(instance.merge(array(), array()), array(), "Artemis example: ");
    }

    @Test
    @Name(" merge one empty example")//Mayatest
    public void mergeTwoArraysemptyone() {
        assertArrayEquals(instance.merge(array(1, 2), array()), array(1, 2), "Artemis example: ");
    }

    @Test
    @Name(" merge one empty example at second place")//Mayatest
    public void mergeTwoArraysemptytwo() {
        assertArrayEquals(instance.merge(array(), array(1, 2)), array(1, 2), "Artemis example: ");
    }

    @Test
    @Name("Should merge two arrays of same length")
    public void mergeTwoArraysSameLength() {
        assertArrayEquals(instance.merge(array(), array()), array(), "Should merge empty arrays");
        assertArrayEquals(instance.merge(array(1, 2), array(3, 4)), array(1, 2, 3, 4), "Should merge two arrays of same length");
        assertArrayEquals(instance.merge(array(4, 3), array(2, 1)), array(2, 1, 4, 3), "Should merge two arrays of same length");
        assertArrayEquals(instance.merge(array(3, 2), array(4, 1)), array(3, 2, 4, 1), "Should merge two arrays of same length");
    }

    @Test
    @Name("Should merge two arrays of different lengths")
    public void mergeTwoArraysDifferentLengths() {
        assertArrayEquals(instance.merge(array(1, 4), array(2)), array(1, 2, 4), "Should merge arrays of different length");
        assertArrayEquals(instance.merge(array(-1, 10, 1, 0, -3, 2, 3), array(1, -3, -2, 2)), array(-1, 1, -3, -2, 2, 10, 1, 0, -3, 2, 3), "Should merge arrays of different length");
        assertArrayEquals(instance.merge(array(9, -111, 33), array(5, 50, 3, 6, -99)), array(5, 9, -111, 33, 50, 3, 6, -99), "Should merge arrays of different length");
    }

    //merge of multiple arrays

    @Test
    @DisplayName("Should merge Artemis example")
    public void mergeMultipleArtemis() {
        assertArrayEquals(instance.merge(array2d(array(1), array(2), array(3), array(4)), 0, 4), array(1, 2, 3, 4), "Should merge artemis example");
    }

    @Test
    @Name(" merge empty 2d example")//Mayatest
    public void mergeTwoArraysempty2d() {
        assertArrayEquals(instance.merge(array2d(array(), array(), array(), array()), 0, 4), array(), "Should merge empty example");
    }

    @Test
    @Name(" merge invalid input to == from: should put empty array")//Mayatest
    public void mergeTwoArraysin() {
        assertArrayEquals(instance.merge(array2d(array(1, 2, 3, 4), array(5, 6, 7), array(8, 9), array(10, 11)), 0, 0), array(), "Should merge and return empty example");
    }

    @Test
    @Name(" merge invalid input to < from: should put empty array")//Mayatest
    public void mergeTwoArraysfrom() {
        assertArrayEquals(instance.merge(array2d(array(1, 2, 3, 4), array(5, 6, 7), array(8, 9), array(10, 11)), 2, 1), array(), "Should merge and return empty example");
    }

    @Test
    @DisplayName("Should merge arrays of same length")
    public void mergeMultipleSameLength() {
        assertArrayEquals(instance.merge(array2d(array(1, 2), array(2, 3), array(3, 4), array(4, 5)), 0, 4),
                array(1, 2, 2, 3, 3, 4, 4, 5), "Should merge arrays of same length");
        assertArrayEquals(instance.merge(array2d(array(5, -5, 4), array(69, 420, 0), array(-1, -2, -3)), 0, 3),
                array(-1, -2, -3, 5, -5, 4, 69, 420, 0), "Should merge arrays of same length");
        assertArrayEquals(instance.merge(array2d(array(Integer.MIN_VALUE, 1, 99, 9999, Integer.MAX_VALUE), array(-9999, 2, 100, 8888, -1)), 0, 2),
                array(Integer.MIN_VALUE, -9999, 1, 2, 99, 100, 8888, -1, 9999, Integer.MAX_VALUE), "Should merge arrays of same length");
    }

    @Test
    @DisplayName("Should merge arrays of different length")
    public void mergeMultipleDifferentLength() {
        assertArrayEquals(instance.merge(array2d(array(1, 3), array(), array(3, 40, 99), array(-1, 4, 5)), 0, 4),
                array(-1, 1, 3, 3, 4, 5, 40, 99), "Should merge arrays of different length");
        assertArrayEquals(instance.merge(array2d(array(0, 1), array(), array(-40, -33, -1, 0, 15, 69, 99), array(-8, 27)), 0, 4),
                array(-40, -33, -8, -1, 0, 0, 1, 15, 27, 69, 99), "Should merge arrays of different length");
    }


    @Test
    @DisplayName("div 1")//Mayatest
    public void megamerge() {
        assertArrayEquals(instance.megaMergeSort(array(4, 3, 2, 1), 1),
                new int[]{}, "Should merge arrays of div 1");

    assertArrayEquals(instance.megaMergeSort(array(4, 3, 2, 1), 4),
    array(1,2,3,4), "Should merge arrays of div length");
        assertArrayEquals(instance.megaMergeSort(array(1), 1),
                new int[]{}, "Should merge array of one element and div ==1");
}

    /*
     * DOMINIK GLEISSNER TESTS
     * tests jedes gegebene array u.a. mit div == -1, div == 0; div == 2, div == 3, div == 4
     */

    @Test//DG_TEST
    @DisplayName("DG_TEST megaMerge 1")
    public void megaMerge1() {
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, -1),
                new int[]{},
                "Should merge arrays of div -1");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 0),
                new int[]{},
                "Should merge arrays of div 0");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 1),
                new int[]{},
                "Should merge arrays of div 1");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 2),
                new int[]{-1,0,1,2,3,4,5,6},
                "Should merge arrays of div 2");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 3),
                new int[]{-1,0,1,2,3,4,5,6},
                "Should merge arrays of div 3");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 4),
                new int[]{-1,0,1,2,3,4,5,6},
                "Should merge arrays of div 4");
        assertArrayEquals(instance.megaMergeSort(new int[]{6,5,4,3,2,1,0,-1}, 5),
                new int[]{-1,0,1,2,3,4,5,6},
                "Should merge arrays of div 5");
    }

    @Test//DG_TEST
    @DisplayName("DG_TEST megaMerge 2")
    public void megaMerge2() {
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, -1),
                new int[]{},
                "Should merge arrays of div -1");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 0),
                new int[]{},
                "Should merge arrays of div 0");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 1),
                new int[]{},
                "Should merge arrays of div 1");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 2),
                new int[]{-1,0,1,2,3,4,5},
                "Should merge arrays of div 2");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 3),
                new int[]{-1,0,1,2,3,4,5},
                "Should merge arrays of div 3");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 4),
                new int[]{-1,0,1,2,3,4,5},
                "Should merge arrays of div 4");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,0,-1}, 5),
                new int[]{-1,0,1,2,3,4,5},
                "Should merge arrays of div 5");
    }

    @Test//DG_TEST
    @DisplayName("DG_TEST megaMerge 3")
    public void megaMerge3() {
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, -1),
                new int[]{},
                "Should merge arrays of div -1");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 0),
                new int[]{},
                "Should merge arrays of div 0");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 1),
                new int[]{},
                "Should merge arrays of div 1");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 2),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 2");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 3),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 3");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 4),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 4");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 5),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 5");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 100),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 100");
        assertArrayEquals(instance.megaMergeSort(new int[]{5,4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 101),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,5,6,7,8,9,100},
                "Should merge arrays of div 101");
    }

    @Test//DG_TEST
    @DisplayName("DG_TEST megaMerge 4")
    public void megaMerge4() {
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, -1),
                new int[]{},
                "Should merge arrays of div -1");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 0),
                new int[]{},
                "Should merge arrays of div 0");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 1),
                new int[]{},
                "Should merge arrays of div 1");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 2),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 2");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 3),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 3");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 4),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 4");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 5),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 5");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 100),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 100");
        assertArrayEquals(instance.megaMergeSort(new int[]{4,3,2,1,9,8,7,6,0,-1,-2,-3,-2,-1,0,1,2,100}, 101),
                new int[]{-3,-2,-2,-1,-1,0,0,1,1,2,2,3,4,6,7,8,9,100},
                "Should merge arrays of div 101");
    }




    }




