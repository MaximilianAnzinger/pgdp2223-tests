package pgdp;

import pgdp.datastructures.lists.RecIntList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pgdp.TestAPI.RecList;

import org.junit.jupiter.api.*;

public class ReverseTest {
    @Test
    @DisplayName("should reverse an empty list")
    public void empty() {
        assertEquals("Empty list", RecList().toString());
    }

    @Test
    @DisplayName("should reverse example list")
    public void example() {
        var list = RecList(1, 2, 3, 4, 5);
        list.reverse();
        assertEquals("List: [5, 4, 3, 2, 1]", list.toString());
    }

    @Test
    @Timeout(1)
    @DisplayName("should pass performance test")
    public void performance_test() {
        // TODO: FIX PERFORMANCE TEST

        var list = RecList();
        var expected = RecList();

        int length = 10000;
        for (int i = 0; i < length; i++) {
            list.append(i);
            expected.append(length - i - 1);
        }
        
        list.reverse();
        assertEquals(expected.toString(), list.toString());
    }
}
