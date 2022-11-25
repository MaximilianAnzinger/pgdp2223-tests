package pgdp.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MultiStackTest {


    private static int[] array(int... values) {
        return values;
    }

    private static boolean arrayContains(int[] arr, int value) {
        return Arrays.stream(arr).anyMatch(value1 -> value1 == value);
    }

    private MultiStack stack;

    @BeforeEach
    void setUp() {
        stack = createMultiStack();
    }

    Stack getHead() {
        try {
            final var field = stack.getClass().getDeclaredField("stacks");
            field.setAccessible(true);
            return (Stack) field.get(stack);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field stacks is missing on class MultiStack");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    Stack getNext(Stack s) {
        try {
            final var field = s.getClass().getDeclaredField("next");
            field.setAccessible(true);
            return (Stack) field.get(s);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    int[] getMem(Stack s) {
        try {
            final var field = s.getClass().getDeclaredField("mem");
            field.setAccessible(true);
            return (int[]) field.get(s);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    MultiStack createMultiStack() {
        return new MultiStack();
    }

    @Test
    void pushShouldAddValue() {
        final var value = 20;
        stack.push(value);
        final var expectedMem = array(value);
        assertArrayEquals(expectedMem, getMem(getHead()));
    }

    @Test
    void pushOutOfCapacityShouldCreateNewStack() {
        final var value = 20;
        stack.push(-1);
        stack.push(value);
        final var next = getNext(getHead());
        final var mem = getMem(next);
        assertTrue(arrayContains(mem, value));
    }

    @Test
    void topShouldReturnLastPushedValue() {
        final var expected = 100;
        stack.push(200);
        stack.push(600);
        stack.push(expected);
        assertEquals(expected, stack.top());
    }

    @Test
    void popShouldNotRemoveFirstStack() {
        stack.pop();
        assertNotNull(getHead());
    }

    @Test
    void popShouldReturnLastPushedValue() {
        final var expected = 100;
        stack.push(200);
        stack.push(600);
        stack.push(expected);
        assertEquals(expected, stack.pop());
    }

    @Test
    void popShouldRemoveValueFromStack() {
        final var val1 = 200;
        final var val2 = 600;
        stack.push(val1);
        stack.push(val2);
        stack.push(600);
        stack.pop();
        assertArrayEquals(array(val1), getMem(getHead()));
        assertTrue(arrayContains(getMem(getNext(getHead())), val2));
    }

    @Test
    void popShouldRemoveEmptyStack() {
        stack.push(200);
        stack.push(200);
        stack.pop();
        assertNull(getNext(getHead()));
    }

}
