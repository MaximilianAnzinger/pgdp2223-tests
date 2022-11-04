package pgdp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.fail;
import static pgdp.arrayfun.ArrayFunctions.filter;
import static pgdp.arrayfun.ArrayFunctions.quantities;
import static pgdp.arrayfun.ArrayFunctions.rotate;
import static pgdp.arrayfun.ArrayFunctions.sumOfSquares;
import static pgdp.arrayfun.ArrayFunctions.zip;
import static pgdp.arrayfun.ArrayFunctions.zipMany;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnitTests {

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

    private void assertArrayEquals(int[] expected, int[] actual, String message) {
        if (!Arrays.equals(expected, actual)) {
            fail(message + "\nExpected: " + Arrays.toString(expected) + "\nActual: " + Arrays.toString(actual));
        }
    }

    private void assertArrayEquals(int[][] expected, int[][] actual, String message) {
        if (!Arrays.deepEquals(expected, actual)) {
            fail(message + "\nExpected: " + Arrays.deepToString(expected) + "\nActual: " + Arrays.deepToString(actual));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Tests for sumOfSquares(..)">
    @Test
    @DisplayName("sumOfSquares: Empty array returns 0")
    void sumOfSquares_EmptyArray() {
        int[] in = array();

        long result = sumOfSquares(in);

        assertEquals(0, result, "Empty array should cause method to return 0");
    }

    @Test
    @DisplayName("sumOfSquares: Overflow returns -1 and prints message")
    void sumOfSquares_Overflow() {
        int[] in = array(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        long result = sumOfSquares(in);

        List<String> console = lines();
        assertEquals(-1, result, "Overflow should cause method to return -1");
        assertLinesMatch(console, List.of("Overflow!"), "Console should have printed \"Overflow!\"");
    }

    @Test
    @DisplayName("sumOfSquares: Overflow by Integer.MIN_VALUEs returns -1 and prints message")
    void sumOfSquares_OverflowByNegatives() {
        int[] in = array(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

        long result = sumOfSquares(in);

        List<String> console = lines();
        assertEquals(-1, result, "Overflow should cause method to return -1");
        assertLinesMatch(console, List.of("Overflow!"), "Console should have printed \"Overflow!\"");
    }

    @Test
    @DisplayName("sumOfSquares: Integer.MAX_VALUE")
    void sumOfSquares_IntegerMax() {
        int[] in = array(Integer.MAX_VALUE);

        long result = sumOfSquares(in);

        assertEquals(4611686014132420609L, result, "Integer.MAX_VALUE as the only input should return 4611686014132420609");
    }

    @Test
    @DisplayName("sumOfSquares: Integer.MAX_VALUE, Integer.MAX_VALUE")
    void sumOfSquares_IntegerMaxTwice() {
        int[] in = array(Integer.MAX_VALUE, Integer.MAX_VALUE);

        long result = sumOfSquares(in);

        assertEquals(9223372028264841218L, result, "Integer.MAX_VALUE twice as input should return 9223372028264841218");
    }

    @Test
    @DisplayName("sumOfSquares: Different input combinations")
    void sumOfSquares_differentInputCombinations() {
        assertEquals(0, sumOfSquares(array(0)), "[0] should return 0");
        assertEquals(1, sumOfSquares(array(1)), "[1] should return 1");
        assertEquals(1, sumOfSquares(array(0, 1)), "[0,1] should return 1");
        assertEquals(1, sumOfSquares(array(1, 0)), "[1,0] should return 1");
        assertEquals(2, sumOfSquares(array(1, 1)), "[1,1] should return 2");
        assertEquals(5, sumOfSquares(array(1, 2)), "[1,2] should return 5");
        assertEquals(55, sumOfSquares(array(1, 2, 3, 4, 5)), "[1,2,3,4,5] should return 55");
        assertEquals(1, sumOfSquares(array(-1)), "[-1] should return 1");
        assertEquals(4, sumOfSquares(array(-2)), "[-2] should return 4");
        assertEquals(55, sumOfSquares(array(-1, -2, -3, -4, -5)), "[-1,-2,-3,-4,-5] should return 55");
    }
    //</editor-fold>

    //<editor-fold desc="Tests for zip(.., ..)">
    @Test
    @DisplayName("zip: Empty arrays zip to empty array")
    void zip_emptyArrays() {
        assertArrayEquals(array(), zip(array(), array()),
            "Zipping two empty arrays should result in an empty array");
    }

    @Test
    @DisplayName("zip: One sided array only takes that array into account")
    void zip_oneSided() {
        assertArrayEquals(array(1, 2, 3), zip(array(1, 2, 3), array()),
            "One sided zipping should only take contents from that array");
    }

    @Test
    @DisplayName("zip: Unequal sized arrays can still be zipped")
    void zip_unequalArrays() {
        assertArrayEquals(array(1, 2, 3, 4, 5), zip(array(1, 3, 5), array(2, 4)),
            "Arrays with different sizes should still zip together");
    }

    @Test
    @DisplayName("zip: Uses order given by parameter arrays")
    void zip_randomUnsortedNumbers() {
        assertArrayEquals(array(1, 5, 0, 10, 2, 23, 100), zip(array(1, 0, 2), array(5, 10, 23, 100)),
            "Shouldn't have ordered entries by size");
    }

    @Test
    @DisplayName("zip: Can work with identical numbers")
    void zip_identicalNumbers() {
        assertArrayEquals(array(1, 1, 1, 1, 1, 1), zip(array(1, 1, 1), array(1, 1, 1)),
            "Should have copied over all 1s into the zipped array");
    }

    @Test
    @DisplayName("zip: Can work with negative numbers")
    void zip_negativeNumbers() {
        assertArrayEquals(array(-5, -10, -2, -1, -3), zip(array(-5, -2), array(-10, -1, -3)),
            "Should be able to zip negative numbers");
    }

    @Test
    @DisplayName("zip: Artemis Example")
    void zip_artemisExample() {
        assertArrayEquals(array(1, 2, 3, 4), zip(array(1, 3), array(2, 4)),
            "The example given on Artemis should work for you");
    }
    //</editor-fold>

    //<editor-fold desc="Tests for zipMany(.., ..)">
    @Test
    @DisplayName("zipMany: No arrays zip to empty array")
    void zipMany_noArrays() {
        assertArrayEquals(array(), zipMany(array2d()),
            "An empty two-dimensional array should zip into an empty array");
    }

    @Test
    @DisplayName("zipMany: Empty arrays zip to empty array")
    void zipMany_emptyArrays() {
        assertArrayEquals(array(), zipMany(array2d(array(), array(), array(), array())),
            "Zipping many empty arrays should result in an empty array");
    }

    @Test
    @DisplayName("zipMany: One sided array only takes that array into account")
    void zipMany_oneSided() {
        assertArrayEquals(array(1, 2, 3), zipMany(array2d(array(1, 2, 3), array(), array(), array())),
            "One sided zipping should only take contents from that array");
    }

    @Test
    @DisplayName("zipMany: Unequal sized arrays can still be zipped")
    void zipMany_unequalArrays() {
        assertArrayEquals(array(1, 2, 3, 4, 5, 6), zipMany(array2d(array(1, 4, 6), array(2, 5), array(3), array())),
            "Arrays with different sizes should still zip together");
    }

    @Test
    @DisplayName("zipMany: Uses order given by parameter arrays")
    void zipMany_randomUnsortedNumbers() {
        assertArrayEquals(array(1, 5, 23, Integer.MIN_VALUE, 0, 10, 100, 2), zipMany(array2d(array(1, 0, 2), array(5, 10), array(23, 100), array(Integer.MIN_VALUE))),
            "Shouldn't have ordered entries by size");
    }

    @Test
    @DisplayName("zipMany: Can work with identical numbers")
    void zipMany_identicalNumbers() {
        assertArrayEquals(array(1, 1, 1, 1, 1, 1, 1, 1, 1), zipMany(array2d(array(1, 1, 1), array(1, 1, 1), array(1, 1, 1))),
            "Should have copied over all 1s into the zipped array");
    }

    @Test
    @DisplayName("zipMany: Can work with negative numbers")
    void zipMany_negativeNumbers() {
        assertArrayEquals(array(-5, -10, -2, -1, -3), zipMany(array2d(array(-5, -2), array(-10, -1, -3))),
            "Should be able to zip negative numbers");
    }

    @Test
    @DisplayName("zipMany: Artemis Example")
    void zipMany_artemisExample() {
        assertArrayEquals(array(1, 2, 3, 4, 5, 6), zipMany(array2d(array(1, 4), array(2, 5), array(3, 6))),
            "The example given on Artemis should work for you");
    }
    //</editor-fold>

    //<editor-fold desc="Tests for filter(.., .., ..)">
    @Test
    @DisplayName("filter: max < min")
    void filter_MaxSmallerThanMin() {
        assertArrayEquals(array(), filter(array(0, 1), 1, 0),
            "If 0 < max < min, then the resulting array must always be empty");
    }

    @Test
    @DisplayName("filter: max < min if max is negative")
    void filter_MaxSmallerThanMinMaxNegative() {
        assertArrayEquals(array(), filter(array(-1, 0, 1), 1, -1),
            "If max < 0 < min, then the resulting array must always be empty");
    }

    @Test
    @DisplayName("filter: max < min if both are negative")
    void filter_MaxSmallerThanMinBothNegative() {
        assertArrayEquals(array(), filter(array(-2, -1), -1, -2),
            "If max < min < 0, then the resulting array must always be empty");
    }

    @Test
    @DisplayName("filter: All entries are in range")
    void filter_AllInRange() {
        assertArrayEquals(array(0, 1, 2, 3), filter(array(0, 1, 2, 3), 0, 3),
            "If all entries are in range, then they should have been copied over to resulting array");
    }

    @Test
    @DisplayName("filter: Different input combinations")
    void filter_DifferentInputCombinations() {
        assertArrayEquals(array(0, 1, 2, 3), filter(array(0, 1, 2, 3, 4), 0, 3),
            "Should've filtered out [4] from [0, 1, 2, 3]");
        assertArrayEquals(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE),
            filter(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE), Integer.MIN_VALUE, Integer.MAX_VALUE),
            "Shouldn't have filtered out anything from [Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE]");
        assertArrayEquals(array(0, 69, 420, Integer.MAX_VALUE), filter(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE), 0, Integer.MAX_VALUE),
            "Should've filtered out [Integer.MIN_VALUE, -420, -69] from [Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE]");
        assertArrayEquals(array(Integer.MIN_VALUE, -420, -69, 0), filter(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE), Integer.MIN_VALUE, 0),
            "Should've filtered out [69, 420, Integer.MAX_VALUE] from [Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE]");
        assertArrayEquals(array(69, 420, Integer.MAX_VALUE), filter(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE), 1, Integer.MAX_VALUE),
            "Should've filtered out [Integer.MIN_VALUE, -420, -69, 0] from [Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE]");
        assertArrayEquals(array(Integer.MIN_VALUE, -420, -69), filter(array(Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE), Integer.MIN_VALUE, -1),
            "Should've filtered out [0, 69, 420, Integer.MAX_VALUE] from [Integer.MIN_VALUE, -420, -69, 0, 69, 420, Integer.MAX_VALUE]");
    }
    //</editor-fold>

    //<editor-fold desc="Tests for rotate(.., ..)">
    @Test
    @DisplayName("rotate: Empty array rotation")
    void rotate_EmptyArray() {
        int[] array = array();
        rotate(array, 5);
        assertArrayEquals(array(), array, "Rotating an empty array should do nothing");
    }

    @Test
    @DisplayName("rotate: Single element array rotation")
    void rotate_SingleElementArray() {
        int[] array = array(1);
        rotate(array, 3);
        assertArrayEquals(array(1), array, "Rotating an array with one element should do nothing");
    }

    @Test
    @DisplayName("rotate: Single element array inverse rotation")
    void rotate_SingleElementArrayInverse() {
        int[] array = array(1);
        rotate(array, -3);
        assertArrayEquals(array(1), array, "Inverse rotating an array with one element should do nothing");
    }

    @Test
    @DisplayName("rotate: Rotating by array length")
    void rotate_RotatingByArrayLength() {
        int[] array = array(1, 2, 3, 4, 5);
        rotate(array, array.length);
        assertArrayEquals(array(1, 2, 3, 4, 5), array, "Rotating an array by its length should do nothing");
    }

    @Test
    @DisplayName("rotate: Rotating by Integer.MAX_VALUE")
    void rotate_RotatingByIntegerMax() {
        int[] array = array(1, 2, 3, 4, 5);
        rotate(array, Integer.MAX_VALUE);
        assertArrayEquals(array(4, 5, 1, 2, 3), array, "Should be able to also rotate by large amounts");
    }

    @Test
    @DisplayName("rotate: Rotating by Integer.MIN_VALUE")
    void rotate_RotatingByIntegerMin() {
        int[] array = array(1, 2, 3, 4, 5);
        rotate(array, Integer.MIN_VALUE);
        assertArrayEquals(array(4, 5, 1, 2, 3), array, "Should be able to also inverse rotate by large amounts");
    }

    @Test
    @DisplayName("rotate: Artemis Examples")
    void rotateArtemisExamples() {
        int[] array;

        // First Example
        array = array(1, 2, 3, 4, 5);
        rotate(array, 2);
        assertArrayEquals(array(4, 5, 1, 2, 3), array,
            "Rotating [1, 2, 3, 4, 5] by 2 => [4, 5, 1, 2, 3]");

        // Second Example
        array = array(1, 2, 3, 4, 5);
        rotate(array, -1);
        assertArrayEquals(array(2, 3, 4, 5, 1), array,
            "Rotating [1, 2, 3, 4, 5] by -1 => [2, 3, 4, 5, 1]");

        // Third Example
        array = array(1, 2, 3, 4, 5);
        rotate(array, 6);
        assertArrayEquals(array(5, 1, 2, 3, 4), array,
            "Rotating [1, 2, 3, 4, 5] by 6 => [5, 1, 2, 3, 4]");
    }
    //</editor-fold>

    //<editor-fold desc="Tests for quantities(..)">
    @Test
    @DisplayName("quantities: No elements to count")
    void quantities_NoElements() {
        assertArrayEquals(array2d(), quantities(array()),
            "Empty array should result in empty two-dimensional array");
    }

    @Test
    @DisplayName("quantities: Identical elements")
    void quantities_IdenticalElements() {
        assertArrayEquals(array2d(array(1, 6)), quantities(array(1, 1, 1, 1, 1, 1)),
            "Should be able to work with identical numbers");
    }

    @Test
    @DisplayName("quantities: Negative elements")
    void quantities_NegativeElements() {
        assertArrayEquals(array2d(array(-1, 2), array(-2, 2), array(-3, 1)), quantities(array(-1, -2, -1, -2, -3)),
            "Should be able to work with negative numbers");
    }

    @Test
    @DisplayName("quantities: Preserve original order of numbers")
    void quantities_PreserveOrder() {
        assertArrayEquals(array2d(array(69, 2), array(-13, 1), array(420, 3), array(-666, 1)), quantities(array(69, -13, 420, -666, 420, 69, 420)),
            "Should keep the order of numbers after counting them");
    }

    @Test
    @DisplayName("quantities: Artemis examples")
    void quantities_ArtemisExamples() {
        assertArrayEquals(array2d(array(1, 4), array(2, 2), array(3, 1)), quantities(array(1, 1, 2, 1, 3, 2, 1)),
            "Should produce correct two-dimensional array from first Artemis example");
        assertArrayEquals(array2d(array(2, 3), array(0, 5), array(3, 2), array(5, 2)), quantities(array(2, 0, 3, 0, 2, 5, 5, 3, 2, 0, 0, 0)),
            "Should produce correct two-dimensional array from second Artemis example");
    }
    //</editor-fold>

}
