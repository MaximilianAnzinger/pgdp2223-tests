package pgdp.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest {

    private RingBuffer createRB(int capacity) {
        return new RingBuffer(capacity);
    }

    @Test
    void isEmptyShouldInitiallyBeTrue() {
        final var rb = createRB(2);
        assertTrue(rb.isEmpty(), "Ringer buffer should be empty if no value was added");
    }

    @Test
    void isEmtpyShouldBeFalseWhenValueWasAdded() {
        final var rb = createRB(2);
        rb.put(2);
        assertFalse(rb.isEmpty());
    }

    @Test
    void isFullShouldBeTrueWhenCapacityIsZero() {
        final var rb = createRB(0);
        assertTrue(rb.isFull());
    }

    @Test
    void isFullShouldBeTrueWhenCapacityIsReached() {
        final var capacity = 3;
        final var rb = createRB(capacity);
        for (int i = 0; i < capacity; i++) {
            rb.put(2);
        }
        assertTrue(rb.isFull());
    }

    @Test
    void putShouldReturnTrueWhenValueCouldBeAdded() {
        final var rb = createRB(1);
        final var result = rb.put(1);
        assertTrue(result);
    }

    @Test
    void putShouldReturnFalseWhenValueCouldNotBeAdded() {
        final var rb = createRB(1);
        rb.put(1);
        final var result = rb.put(1);
        assertFalse(result);
    }

    @Test
    void getShouldReturnPutValue() {
        final var rb = createRB(1);
        final var value = 3;
        rb.put(value);
        final var actual = rb.get();
        assertEquals(value, actual);
    }

    @Test
    void getShouldReturnLongestAliveValue() {
        final var longestAlive = 4;
        final var rb =  createRB(longestAlive);
        for (int i = longestAlive; i > 0; i--) {
            rb.put(i);
        }
        final var actual = rb.get();
        assertEquals(longestAlive, actual);
    }

    @Test
    void getShouldReturnMinValueWhenThereIsNoValue() {
        final var rb = createRB(2);
        final var actual = rb.get();
        assertEquals(Integer.MIN_VALUE, rb.get());
    }

    @Test
    void shouldHandleOverflow() {
        final var rb = createRB(2);
        rb.put(1);
        rb.put(2);
        rb.put(3);
        assertEquals(1, rb.get());
        assertEquals(2, rb.get());
        assertEquals(Integer.MIN_VALUE, rb.get());
    }

    // Some integration tests

    @Test
    void integrationTest1() {
        final var rb = createRB(4);
        final var expected = 10;
        rb.put(100);
        rb.put(2);
        rb.put(9);
        rb.get();
        rb.get();
        rb.put(expected);
        rb.get();
        final var actual = rb.get();
        assertEquals(expected, actual);
    }

    @Test
    void integrationTest2() {
        final var rb = createRB(2);
        final var expected = 100;
        rb.put(10);
        rb.put(2);
        rb.get();
        rb.put(expected);
        rb.get();
        final var actual = rb.get();
        assertEquals(expected, actual);
    }

}
