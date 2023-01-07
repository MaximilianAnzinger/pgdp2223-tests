package pgdp.pools;

import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pgdp.pools.FunctionLib.INC;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskBehaviorTest {

    private static Field fieldInput;
    private static Field fieldResult;
    private static Field fieldTaskFunction;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException {
        fieldInput = Task.class.getDeclaredField("input");
        fieldInput.setAccessible(true);
        fieldResult = Task.class.getDeclaredField("result");
        fieldResult.setAccessible(true);
        fieldTaskFunction = Task.class.getDeclaredField("taskFunction");
        fieldTaskFunction.setAccessible(true);
    }

    @Test
    @DisplayName("Artemis Example #1")
    void artemisExample1() {
        var func = new TaskFunction<>(INC);
        var left = new Task<>(1, func);
        var right = new Task<>(1, func);

        assertEquals(left, right, "Two Task should be equal when their input and TaskFunction are equal");
    }

    @Test
    @DisplayName("Artemis Example #2")
    void artemisExample2() {
        var func1 = new TaskFunction<>(INC);
        var func2 = new TaskFunction<>(INC);
        var left = new Task<>(1, func1);
        var right = new Task<>(1, func2);

        assertNotEquals(left, right, "Two Task should not be equal when their input are equal, but their TaskFunctions are not");
    }

    @Test
    @DisplayName("Artemis Example #3")
    void artemisExample3() {
        var func1 = new TaskFunction<>(INC);
        var task = new Task<>(1, func1);

        assertEquals(2, task.getResult(), "An INC Task should increase their input by one");
    }

    @Test
    @DisplayName("Constructor sets input and taskFunction")
    void constructorSetsInputAndTaskFunction() throws IllegalAccessException {
        var func = new TaskFunction<>(INC);
        var input = 1;

        var task = new Task<>(input, func);

        assertEquals(input, fieldInput.get(task), "input should have been set");
        assertEquals(func, fieldTaskFunction.get(task), "taskFunction should have been set");
    }

    @Test
    @DisplayName("Result gets lazily initialized")
    void resultLazyInitialization() throws IllegalAccessException {
        final var interceptor = new Object() {
            boolean called = false;
        };
        final var newValue = 1;

        var func = new TaskFunction<>($ -> {
            interceptor.called = true;
            return newValue;
        });
        var task = new Task<>(0, func);

        assertFalse(interceptor.called, "The underlying function of TaskFunction should not have been called before invocation of getResult()");
        assertNull(fieldResult.get(task), "result should not be initialized before invocation of getResult()");
        int result = task.getResult();
        assertTrue(interceptor.called, "The underlying function of TaskFunction should have been called after invocation of getResult()");
        assertEquals(newValue, result, "getResult() should produce a result given by the underlying function");
        assertEquals(newValue, fieldResult.get(task), "result should be initialized after invocation of getResult()");
    }

    @Test
    @DisplayName("Only call underlying function once")
    void onlyCalculateResultOnce() {
        final var interceptor = new Object() {
            int invocations = 0;
        };
        final var newValue = 1;

        var func = new TaskFunction<>($ -> {
            interceptor.invocations++;
            return newValue;
        });
        var task = new Task<>(0, func);

        assertEquals(0, interceptor.invocations, "Underlying function should not have been invoked when getResult() hasn't been called");
        task.getResult();
        assertEquals(1, interceptor.invocations, "Underlying function should have been invoked once when getResult() has been called only once");
        task.getResult();
        assertEquals(1, interceptor.invocations, "Calling getResult() again should not re-invoke the underlying function");
    }

    // Requirements according to
    // - https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#hashCode()
    // - https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object)

    @Test
    @DisplayName("hashCode() is only dependent on ID")
    void hashCodeOnlyDependentOnID() {
        var firstHashCode = new Task<>(0, new TaskFunction<>(identity())).hashCode();
        var secondHashCode = new Task<>(0, new TaskFunction<>(identity())).hashCode();

        assertNotEquals(firstHashCode, secondHashCode, "hashCode() has to produce two different values for tasks that only differentiate themselves via the TaskFunction id");
    }

    @Test
    @DisplayName("hashCode() produces the same output for two invocations")
    void hashCodeDoubleInvocation() {
        var task = new Task<>(0, new TaskFunction<>(identity()));

        assertEquals(task.hashCode(), task.hashCode(), "hashCode() has to produce the same value for two or more invocations");
    }

    @Test
    @DisplayName("equals() is reflexive")
    void equalsIsReflexive() {
        var task = new Task<>(0, new TaskFunction<>(identity()));

        //noinspection SimplifiableAssertion
        assertTrue(task.equals(task), "equals should be reflexive: x.equals(x) has to yield true");
    }

    @Test
    @DisplayName("equals() is symmetric")
    void equalsIsSymmetric() {
        var func = new TaskFunction<>(identity());
        var task1 = new Task<>(0, func);
        var task2 = new Task<>(0, func);

        assertTrue(task1.equals(task2) && task2.equals(task1), "equals should be symmetric: task1.equals(task2) has to yield true");
    }

    @Test
    @DisplayName("equals() is transitive")
    void equalsIsTransitive() {
        var func = new TaskFunction<>(identity());
        var task1 = new Task<>(0, func);
        var task2 = new Task<>(0, func);
        var task3 = new Task<>(0, func);

        assertTrue(task1.equals(task2) && task2.equals(task3) && task1.equals(task3), "equals should be transitive: when task1.equals(task2) and task2.equals(task3), then task1.equals(task3)");
    }

    @Test
    @DisplayName("equals() should return true multiple times")
    void equalsInvokedMultipleTimes() {
        var func = new TaskFunction<>(identity());
        var task1 = new Task<>(0, func);
        var task2 = new Task<>(0, func);

        assertTrue(task1.equals(task2) & task1.equals(task2) & task1.equals(task2), "equals should yield true even if invoked multiple times the same way");
    }

    @Test
    @DisplayName("equals() should return false for null")
    void equalsNullParameter() {
        var task = new Task<>(0, new TaskFunction<>(identity()));

        //noinspection ConstantValue,SimplifiableAssertion
        assertFalse(task.equals(null), "equals should yield false for null as parameter");
    }

    @Test
    @DisplayName("equals() should return false for different input and identical function")
    void equalsDifferentInput() {
        var func = new TaskFunction<>(identity());
        var task1 = new Task<>(0, func);
        var task2 = new Task<>(1, func);

        assertNotEquals(task1, task2, "equals should yield false for task with different input");
    }

    @Test
    @DisplayName("equals() should return false for identical input and different TaskFunction")
    void equalsDifferentTaskFunction() {
        var func1 = new TaskFunction<>(identity());
        var func2 = new TaskFunction<>(identity());
        var task1 = new Task<>(0, func1);
        var task2 = new Task<>(1, func2);

        assertNotEquals(task1, task2, "equals should yield false for task with different TaskFunctions");
    }

    @Test
    @DisplayName("equals() should return false for identical input and different underlying function")
    void equalsDifferentUnderlyingFunction() {
        var func1 = new TaskFunction<>(identity());
        var func2 = new TaskFunction<>(INC);
        var task1 = new Task<>(0, func1);
        var task2 = new Task<>(1, func2);

        assertNotEquals(task1, task2, "equals should yield false for task with different underlying functions");
    }

    @Test
    @DisplayName("equals() should return false for identical input and different subclassed method")
    void equalsDifferentUnderlyingSubclassedFunction() {
        var func1 = new TaskFunction<Integer, Integer>(INC);
        var func2 = new TaskFunction<Integer, Integer>(INC) {
        };
        var task1 = new Task<>(1, func1);
        var task2 = new Task<>(1, func2);

        assertNotEquals(task1, task2, "equals should yield false for task with different underlying methods (subclassed)");
    }

    @Test
    @DisplayName("equals() should return false for identical input and overwritten hashCode method")
    void equalsDifferentUnderlyingHashCodeMethod() {
        var func1 = new TaskFunction<Integer, Integer>(INC);
        var func2 = new TaskFunction<Integer, Integer>(INC);

        var task1 = new Task<>(1, func1);
        var task2 = new Task<>(1, func2) {
            @Override
            public int hashCode() {
                return task1.hashCode();
            }
        };

        assertNotEquals(task1, task2, "equals should yield false for task with different hashCode method (subclassed)");
    }

}
