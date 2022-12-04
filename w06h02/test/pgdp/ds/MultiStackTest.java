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
        assertEquals(mem[0], value);
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

    @Test
    void emptyStackShouldReturnMinValue() {
        assertEquals(Integer.MIN_VALUE, stack.top());
    }

    @Test
    void emptyStackShouldReturnMinValueOnPop() {
        assertEquals(Integer.MIN_VALUE, stack.pop());
    }

    @Test
    void pushShouldNotChangeStacks() {
        final var stacks = getHead();
        stack.push(200);
        assertEquals(stacks, getHead());
    }

    @Test
    void popShouldNotChangeStacks() {
        final var stacks = getHead();
        stack.push(200);
        stack.pop();
        assertEquals(stacks, getHead());
    }

    @Test
    void appendedStacksShouldAlwaysHaveDoubleCapacity() {
        final var stacksToCreate = 10;
        var valuesToPush = 1;
        for (int i = 0; i < stacksToCreate; i++) {
            for (int j = 0; j < valuesToPush; j++) {
                stack.push(j);
            }
            valuesToPush *= 2;
        }
        var current = getHead();
        var expectedCapacity = 1;
        for (int i = 0; i < stacksToCreate; i++) {
            assertEquals(expectedCapacity, getMem(current).length);
            current = getNext(current);
            expectedCapacity *= 2;
        }
    }

    @Test
    void topShouldNotRemoveValue() {
        final var value = 200;
        stack.push(value);
        stack.top();
        assertArrayEquals(array(value), getMem(getHead()));
    }

    @Test
    void pushShouldNotChangeHead() {
        final var head = getHead();
        stack.push(200);
        stack.push(200);
        stack.push(200);
        assertEquals(head, getHead());
    }

    @Test
    void topShouldReturnCorrectValues() {
        final var value = 10;
        final var value2 = 20;
        final var value3 = 30;
        final var value4 = 40;
        
        assertEquals(Integer.MIN_VALUE, stack.top());
        stack.push(value);
        assertEquals(value, stack.top());
        stack.push(value2);
        assertEquals(value2, stack.top());
        stack.push(value3);
        assertEquals(value3, stack.top());
        stack.push(value4);
        assertEquals(value4, stack.top());

        assertArrayEquals(array(value), getMem(getHead()));
        assertArrayEquals(array(value2, value3), getMem(getNext(getHead())));
        assertEquals(value4, getMem(getNext(getNext(getHead())))[0]);
    }


    @Test
    void popShouldReturnCorrectValues() {
        final var value = 10;
        final var value2 = 20;
        final var value3 = 30;
        final var value4 = 40;

        stack.push(value);
        assertEquals(value, stack.pop());

        stack.push(value2);
        stack.push(value3);
        assertEquals(value3, stack.pop());

        assertArrayEquals(array(value2), getMem(getHead()));

        stack.push(value4);
        stack.push(value3);
        assertEquals(value3, stack.pop());
        assertEquals(value4, stack.pop());
        assertEquals(value2, stack.pop());

        assertEquals(Integer.MIN_VALUE, stack.pop());
    }

    @Test
    void shouldNotCreateNewStackWhenCurrentStackIsNotFull() {
        stack.push(200);
        stack.push(200);
        stack.push(200);
        stack.pop();
        stack.push(200);
        final var next = getNext(getHead());
        assertNull(getNext(next));
    }

}
