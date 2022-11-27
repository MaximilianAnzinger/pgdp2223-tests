package pgdp.datastructures;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.datastructures.lists.RecIntList;

public class KinguinSortTest extends TestBase {

    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for different input combinations")
    public void testKinguinSort(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertListEquals(expected, r);
    }

    static Stream<Arguments> testKinguinSort() {
        return Stream.of(
            arguments(list(3, 4, 7, 9), list(3, 2, 4, 7, 1, 6, 5, 9, 8), true),
            arguments(list(3, 2, 1), list(3, 2, 4, 7, 1, 6, 5, 9, 8), false),
            arguments(list(-5, 0, 5), list(-5, 0, 5), true),
            arguments(list(-5), list(-5, 0, 5), false),
            arguments(list(0, 1), list(0, 1), true),
            arguments(list(0), list(0, 1), false),
            arguments(list(0), list(0), true),
            arguments(list(0), list(0), false),
            arguments(list(), list(), true),
            arguments(list(), list(), false),
            arguments(list(Integer.MAX_VALUE), list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), true),
            arguments(list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), false)
        );
    }
}
