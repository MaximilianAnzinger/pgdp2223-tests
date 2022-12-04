package pgdp;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.datastructures.lists.RecIntList;

public class KinguinSortTest extends TestBase {

    // Edge Case 1
    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for single element lists")
    public void testKinguinSortSingleElementList(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertListEquals(expected, r);
    }

    static Stream<Arguments> testKinguinSortSingleElementList() {
        return Stream.of(
            arguments(list(0), list(0), true),
            arguments(list(0), list(0), false)
        );
    }

    // Edge Case 2
    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for double element lists")
    public void testKinguinSortDoubleElementList(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertListEquals(expected, r);
    }

    static Stream<Arguments> testKinguinSortDoubleElementList() {
        return Stream.of(
            arguments(list(0, 1), list(0, 1), true),
            arguments(list(0), list(0, 1), false)
        );
    }

    // Edge Case 3
    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for empty lists")
    public void testKinguinSortEmptyList(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertListEquals(expected, r);
    }

    static Stream<Arguments> testKinguinSortEmptyList() {
        return Stream.of(
            arguments(list(), list(), true),
            arguments(list(), list(), false)
        );
    }

    // Edge Case 4
    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for lists with integer border elements")
    public void testKinguinSortIntegerBorderElements(RecIntList expected, RecIntList r, boolean increasing) {
        r.kinguinSort(increasing);
        assertListEquals(expected, r);
    }

    static Stream<Arguments> testKinguinSortIntegerBorderElements() {
        return Stream.of(
            arguments(list(Integer.MAX_VALUE), list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), true),
            arguments(list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), list(Integer.MAX_VALUE, 0, Integer.MIN_VALUE), false)
        );
    }

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
            arguments(list(-5), list(-5, 0, 5), false)
        );
    }

    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort works for equals elements")
    public void testKinguinSortEqualsElements(RecIntList expected, RecIntList actual, boolean increasing) {
        actual.kinguinSort(increasing);
        assertListEquals(expected, actual);
    }

    static Stream<Arguments> testKinguinSortEqualsElements() {
        return Stream.of(
                arguments(list(3,4,4,7,9,9), list(3,2,4,4,7,1,6,5,9,9,8), true),
                arguments(list(3,2,1,1), list(3,2,4,4,7,1,1,6,5,9,9,8), false)
        );
    }

    @ParameterizedTest(name = "{1} | Increasing: {2}")
    @MethodSource
    @DisplayName("kinguinSort set links correctly")
    public void testKinguinSortSetLinks(RecIntList expected, RecIntList actual, boolean increasing) {
        actual.kinguinSort(increasing);
        assertListEquals(expected, actual);
        assertEquals(expected.toConnectionString(), actual.toConnectionString());
    }

    static Stream<Arguments> testKinguinSortSetLinks() {
        return Stream.of(
                arguments(list(3,4,7,9), list(3,2,4,7,1,6,5,9,8), true),
                arguments(list(3,2,1), list(3,2,4,4,7,1,6,5,9,9,8), false)
        );
    }
}
