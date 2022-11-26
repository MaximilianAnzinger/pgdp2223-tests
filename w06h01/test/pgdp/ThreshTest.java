package pgdp;

import pgdp.datastructures.lists.RecIntList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pgdp.TestAPI.RecList;

import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ThreshTest {
    @ParameterizedTest
    @MethodSource
    @DisplayName("should do the tresh function correctly")
    public void testCountThreash(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThreash() {
        return Stream.of(
                arguments(RecList(1, 2, 3, 4, 5), 3, new long[] { 3, 3, 9 }),
                arguments(RecList(), 3, new long[] { 0, 0, 0 }),
                arguments(RecList(1, 2, 3, 4, 5), 0, new long[] { 0, 0, 15 }),
                arguments(RecList(1, 2, 3, 4, 5), 5, new long[] { 10, 5, 0 }),
                arguments(RecList(2), 1, new long[] { 0, 0, 2 }),
                arguments(RecList(2), 2, new long[] { 0, 2, 0 }),
                arguments(RecList(2), 3, new long[] { 2, 0, 0 })
        );
    }
}
