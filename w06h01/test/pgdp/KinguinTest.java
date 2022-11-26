package pgdp;

import pgdp.datastructures.lists.RecIntList;

import static pgdp.TestAPI.RecList;

import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class KinguinTest {
    @ParameterizedTest
    @MethodSource
    @DisplayName("should test the kinguin sort function")
    public void testKinguinSort(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertEquals(expected.toString(), r.toString());
    }

    static Stream<Arguments> testKinguinSort() {
        return Stream.of(
                arguments(RecList(3,4,7,9), RecList(3,2,4,7,1,6,5,9,8), true),
                arguments(RecList(3,2,1), RecList(3,2,4,7,1,6,5,9,8), false),
                arguments(RecList(-5, 0, 5), RecList(-5, 0, 5), true),
                arguments(RecList(-5), RecList(-5, 0, 5), false),
                arguments(RecList(), RecList(), true),
                arguments(RecList(Integer.MAX_VALUE), RecList(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), true),
                arguments(RecList(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), RecList(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), false)
        );
    }
}
