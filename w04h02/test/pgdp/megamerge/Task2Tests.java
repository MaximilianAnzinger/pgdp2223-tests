package pgdp.megamerge;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Task2Tests {

    private MegaMergeSort fixture;

    @BeforeEach
    void setUp() {
        fixture = new MegaMergeSort();
    }

    @Test
    @DisplayName("[Edge Case] from == to")
    void fromEqualsToTo() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] actual = fixture.merge(input, 0, 0);
        assertArrayEquals(array(), actual, fancyErrorMessage(input, 0, 0, array(), actual));
    }

    @Test
    @DisplayName("[Edge Case] from > to")
    void fromBiggerThanTo() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] actual = fixture.merge(input, 1, 0);
        assertArrayEquals(array(), actual, fancyErrorMessage(input, 1, 0, array(), actual));
    }

    @Test
    @DisplayName("[Edge Case] from < 0")
    void fromIsNegative() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] expected = array(1, 2, 3);
        int[] actual = fixture.merge(input, -1, 2);
        assertArrayEquals(expected, actual, fancyErrorMessage(input, -1, 0, expected, actual));
    }

    @Test
    @DisplayName("[Edge Case] from < 0, to < 0")
    void fromAndToAreNegative() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] expected = array();
        int[] actual = fixture.merge(input, -2, -1);
        assertArrayEquals(expected, actual, fancyErrorMessage(input, -2, -1, expected, actual));
    }

    @Test
    @DisplayName("[Edge Case] from < 0, to == 0")
    void fromIsNegativeAndToIsZero() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] expected = array();
        int[] actual = fixture.merge(input, -2, 0);
        assertArrayEquals(expected, actual, fancyErrorMessage(input, -2, 0, expected, actual));
    }

    @Test
    @DisplayName("[Edge Case] to == Integer.MAX_VALUE")
    void toIsIntegerMaxValue() {
        int[][] input = array2d(array(1), array(2, 3));
        int[] expected = array(1, 2, 3);
        int[] actual = fixture.merge(input, 0, Integer.MAX_VALUE);
        assertArrayEquals(expected, actual, fancyErrorMessage(input, 0, Integer.MAX_VALUE, expected, actual));
    }

    @DisplayName("Normal use cases")
    @ParameterizedTest(name = "merge({0}, {1}, {2}) = {3}")
    @MethodSource("arguments")
    void mergeTwoArrays(int[][] arr, int from, int to, int[] expected) {
        int[] actual = fixture.merge(arr, from, to);
        assertArrayEquals(expected, actual, fancyErrorMessage(arr, from, to, expected, actual));
    }

    private static Stream<Arguments> arguments() {
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        return Stream.of(
            Arguments.arguments(array2d(array(1), array(2), array(3), array(4)), 0, 4, array(1, 2, 3, 4)),
            Arguments.arguments(array2d(array(1), array(2), array(3), array(4)), 0, 4, array(1, 2, 3, 4)),
            Arguments.arguments(array2d(array(1), array(2), array(3), array(4)), 0, 3, array(1, 2, 3)),
            Arguments.arguments(array2d(array(1), array(2), array(3), array(4)), 1, 3, array(2, 3)),
            Arguments.arguments(array2d(array(1, 2), array(3, 4), array(5, 6), array(7, 8)), 0, 4, array(1, 2, 3, 4, 5, 6, 7, 8)),
            Arguments.arguments(array2d(array(1, 2), array(3, 4), array(5), array()), 0, 4, array(1, 2, 3, 4, 5)),
            Arguments.arguments(array2d(array(1, 2), array(3, 4), array(), array(5)), 0, 4, array(1, 2, 3, 4, 5))
        );
    }

    //<editor-fold desc="Helper methods">
    private static String fancyErrorMessage(int[][] arr, int from, int to, int[] expected, int[] actual) {
        return "Your implementation of the merge(" + Arrays.deepToString(arr) + ", " + from + ", " + to + ") method did not return the correct result!\n" +
               "------------\n" +
               "Your result: " + Arrays.toString(actual) + "\n" +
               "Expected result: " + Arrays.toString(expected) + "\n\n";
    }

    private static int[] array(int i) {
        return new int[]{i};
    }

    private static int[] array(int... ints) {
        return ints;
    }

    private static int[][] array2d(int[] arrays) {
        return new int[][]{arrays};
    }

    private static int[][] array2d(int[]... arrays) {
        return arrays;
    }

//    private static void assertArrayEquals(int[][] expected, int[][] actual, String message) {
//        assertEquals(expected.length, actual.length, message);
//        for (int i = 0; i < expected.length; i++) {
//            Assertions.assertArrayEquals(expected[i], actual[i]);
//        }
//    }
    //</editor-fold>
}
