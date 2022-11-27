package pgdp;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static pgdp.TestBase.list;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.datastructures.lists.RecIntList;

public class CountTreshTest {

    @ParameterizedTest(name = "{0} | Threshold: {1} | Result: {2}")
    @MethodSource
    @DisplayName("countTresh works for different input combinations")
    public void testCountThresh(RecIntList r, int t, long[] expected) {
        assertArrayEquals(expected, r.countThresh(t));
    }

    static Stream<Arguments> testCountThresh() {
        return Stream.of(
            arguments(list(1, 2, 3, 4, 5), 3, new long[]{3, 3, 9}),
            arguments(list(), 3, new long[]{0, 0, 0}),
            arguments(list(1, 2, 3, 4, 5), 0, new long[]{0, 0, 15}),
            arguments(list(1, 2, 3, 4, 5), 5, new long[]{10, 5, 0}),
            arguments(list(2), 1, new long[]{0, 0, 2}),
            arguments(list(2), 2, new long[]{0, 2, 0}),
            arguments(list(2), 3, new long[]{2, 0, 0}),
            arguments(list(1, 2, 3, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE, 6}),
            arguments(list(1, 2, 3, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{6, Integer.MAX_VALUE, 0}),
            // "Die Methode kann auf jeder möglichen Liste aufgerufen werden, aber es kommt in unseren Tests nicht zu long-Overflows."
            // => Die Methode muss auch mit Int Overflows umgehen können!
            arguments(list(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{0, Integer.MAX_VALUE * 3L, 0}),
            arguments(list(1, 2, 3, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE, new long[]{6, Integer.MAX_VALUE * 3L, 0}),
            arguments(list(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE * 3L, 0}),
            arguments(list(1, 2, 3, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE), Integer.MIN_VALUE, new long[]{0, Integer.MIN_VALUE * 3L, 6})
        );
    }
}
