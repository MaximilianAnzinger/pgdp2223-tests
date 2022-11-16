package pgdp.megamerge;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Task1Tests {

    private MegaMergeSort fixture;

    @BeforeEach
    void setUp() {
        fixture = new MegaMergeSort();
    }

    @DisplayName("Normal use cases")
    @ParameterizedTest(name = "merge({0}, {1}) = {2}")
    @MethodSource("arguments")
    void mergeTwoArrays(int[] arr1, int[] arr2, int[] expected) {
        int[] actual = fixture.merge(arr1, arr2);
        Assertions.assertArrayEquals(expected, actual,
            "Your implementation of the merge(" + Arrays.toString(arr1) + ", " + Arrays.toString(arr2) + ") method did not return the correct result!\n" +
            "------------\n" +
            "Your result: " + Arrays.toString(actual) + "\n" +
            "Expected result: " + Arrays.toString(expected) + "\n\n");
    }

    private static Stream<Arguments> arguments() {
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        return Stream.of(
            Arguments.arguments(array(1, 3), array(2, 4), array(1, 2, 3, 4)),
            Arguments.arguments(array(1, 3, 5), array(2, 4, 6), array(1, 2, 3, 4, 5, 6)),
            Arguments.arguments(array(1, 2, 3), array(4, 5, 6), array(1, 2, 3, 4, 5, 6)),
            Arguments.arguments(array(1, 6), array(2, 3, 4, 5), array(1, 2, 3, 4, 5, 6)),
            Arguments.arguments(array(-5, -2), array(-10, -4, 0), array(-10, -5, -4, -2, 0)),
            Arguments.arguments(array(maxValue), array(minValue), array(minValue, maxValue)),
            Arguments.arguments(array(minValue, minValue, maxValue), array(minValue, maxValue, maxValue), array(minValue, minValue, minValue, maxValue, maxValue, maxValue)),
            Arguments.arguments(array(), array(1), array(1)),
            Arguments.arguments(array(), array(1, 2, 3, 4, 5), array(1, 2, 3, 4, 5)),
            Arguments.arguments(array(1), array(), array(1)),
            Arguments.arguments(array(1, 2, 3, 4, 5), array(), array(1, 2, 3, 4, 5)),
            Arguments.arguments(array(), array(), array())
        );
    }

    //<editor-fold desc="Helper methods">
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
    //</editor-fold>
}
