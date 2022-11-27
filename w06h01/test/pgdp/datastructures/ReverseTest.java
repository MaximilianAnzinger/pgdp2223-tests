package pgdp.datastructures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReverseTest extends TestBase {

    @Test
    @DisplayName("should reverse an empty list")
    public void empty() {
        var list = list();
        list.reverse();
        assertListEquals(list(), list);
    }

    @Test
    @DisplayName("should reverse a list with one element")
    public void singleElement() {
        var list = list(0);
        list.reverse();
        assertListEquals(list(0), list);
    }

    @Test
    @DisplayName("should reverse a list with two elements")
    public void twoElements() {
        var list = list(1, 2);
        list.reverse();
        assertListEquals(list(2, 1), list);
    }

    @Test
    @DisplayName("should reverse example list")
    public void example() {
        var list = list(1, 2, 3, 4, 5);
        list.reverse();
        assertListEquals(list(5, 4, 3, 2, 1), list);
    }

    @Test
    @DisplayName("should reverse list with even amount of elements")
    public void evenElementCount() {
        var list = list(1, 2, 3, 4, 5, 6);
        list.reverse();
        assertListEquals(list(6, 5, 4, 3, 2, 1), list);
    }
}
