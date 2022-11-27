package pgdp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pgdp.TestBase.list;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.datastructures.lists.RecIntList;

public class CountTreshTest {

    // Edge Case 1
    @ParameterizedTest(name = "{0} | Threshold: {1} | Result: {2}")
    @MethodSource
    @DisplayName("countTresh works for single input lists")
    public void testCountThreshSingleElement(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThreshSingleElement() {
        return Stream.of(
            arguments(list(2), 1, new long[]{0, 0, 2}),
            arguments(list(2), 2, new long[]{0, 2, 0}),
            arguments(list(2), 3, new long[]{2, 0, 0})
        );
    }

    // Edge Case 2
    @Test
    @DisplayName("countTresh works for empty list")
    public void testCountThreshEmptyList() {
        assertArrayEquals(new long[]{0, 0, 0}, list().countThresh(3));
    }

    // Edge Case 3
    @ParameterizedTest(name = "{0} | Threshold: {1} | Result: {2}")
    @MethodSource
    @DisplayName("countTresh works for list with int border values")
    public void testCountThreshWithIntBorders(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThreshWithIntBorders() {
        return Stream.of(
            arguments(list(1, 2, 3, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE, 6}),
            arguments(list(1, 2, 3, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{6, Integer.MAX_VALUE, 0})
        );
    }

    // Edge Case 4
    // Auszug aus Artemis:
    // "Die Methode kann auf jeder möglichen Liste aufgerufen werden, aber es kommt in unseren Tests nicht zu long-Overflows."
    // => Die Methode muss auch mit Int Overflows umgehen können!
    @ParameterizedTest(name = "{0} | Threshold: {1} | Result: {2}")
    @MethodSource
    @DisplayName("countTresh works for integer over- and underflow")
    public void testCountThreshProvokingIntegerOverUnderflow(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThreshProvokingIntegerOverUnderflow() {
        return Stream.of(
            arguments(list(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{0, Integer.MAX_VALUE * 3L, 0}),
            arguments(list(1, 2, 3, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{6, Integer.MAX_VALUE * 3L, 0}),
            arguments(list(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE * 3L, 0}),
            arguments(list(1, 2, 3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE * 3L, 6})
        );
    }

    @ParameterizedTest(name = "{0} | Threshold: {1} | Result: {2}")
    @MethodSource
    @DisplayName("countTresh works for different input combinations")
    public void testCountThresh(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThresh() {
        return Stream.of(
            arguments(list(1, 2, 3, 4, 5), 3, new long[]{3, 3, 9}),
            arguments(list(1, 2, 3, 4, 5), 0, new long[]{0, 0, 15}),
            arguments(list(1, 2, 3, 4, 5), 5, new long[]{10, 5, 0})
        );
    }
}
