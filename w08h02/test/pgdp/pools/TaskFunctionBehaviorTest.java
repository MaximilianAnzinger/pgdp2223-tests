package pgdp.pools;

import static java.util.function.Function.identity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pgdp.pools.FunctionLib.SQUARE;
import static pgdp.pools.FunctionLib.SUM_OF_HALFS;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskFunctionBehaviorTest {

    private static Field fieldID;

    // Required to account for static attribute inconsistencies
    private int idShift;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException {
        fieldID = TaskFunction.class.getDeclaredField("ID");
        fieldID.setAccessible(true);
    }

    @Test
    @DisplayName("Artemis Example #1")
    void artemisExample1() {
        var left = new TaskFunction<>(SQUARE);
        var right = new TaskFunction<>(SUM_OF_HALFS);

        assertNotEquals(left, right, "TaskFunction for SQUARE should not be equal to TaskFunction for SUM_OF_HALFS");
    }

    @Test
    @DisplayName("Artemis Example #2")
    void artemisExample2() {
        var left = new TaskFunction<>(SQUARE);
        var right = new TaskFunction<>(SQUARE);

        assertNotEquals(left, right, "Different TaskFunction objects should not be equal even-though they do use the same function");
    }

    @Test
    @DisplayName("Artemis Example #3")
    void artemisExample3() {
        var obj = new TaskFunction<>(SQUARE);

        assertTrue(obj.equals(obj), "Self-referencing object has to be equal");
    }

    @Test
    @DisplayName("Artemis Example #4")
    void artemisExample4() {
        var obj = new TaskFunction<>(SQUARE);

        assertEquals(4, obj.apply(2), "TaskFunction.apply(..) should use the underlying function (SQUARE) to produce a result");
    }

    @Test
    @DisplayName("Check that ID starts at 0")
    void checkIdStart() throws IllegalAccessException {
        idShift = (int) fieldID.get(new TaskFunction<>(identity())) + 1;
        var t = new TaskFunction<>(identity());

        assertEquals(idShift, fieldID.get(t), "TaskFunction ID needs to start at 0");
    }

    @Test
    @DisplayName("Check that ID counts up")
    void checkIdSequence() throws IllegalAccessException {
        idShift = (int) fieldID.get(new TaskFunction<>(identity())) + 1;
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            var t = new TaskFunction<>(identity());

            assertEquals(idShift + i, fieldID.get(t), "TaskFunction ID needs to count up with every new instance (failed at i = " + i + " of " + Short.MAX_VALUE + ")");
        }
    }

    // Requirements according to
    // - https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#hashCode()
    // - https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object)

    @Test
    @DisplayName("hashCode() is only dependent on ID")
    void hashCodeOnlyDependentOnID() {
        var firstHashCode = new TaskFunction<>(identity()).hashCode();
        var secondHashCode = new TaskFunction<>(identity()).hashCode();

        assertNotEquals(firstHashCode, secondHashCode, "hashCode() has to produce two different values for the same function");
    }

    @Test
    @DisplayName("hashCode() produces the same output for two invocations")
    void hashCodeDoubleInvocation() {
        var task = new TaskFunction<>(identity());

        assertEquals(task.hashCode(), task.hashCode(), "hashCode() has to produce the same value for two or more invocations");
    }

    @Test
    @DisplayName("equals() is reflexive")
    void equalsReflexivity() {
        var task = new TaskFunction<>(identity());

        //noinspection SimplifiableAssertion
        assertTrue(task.equals(task), "equals should be reflexive: x.equals(x) has to yield true");
    }

    /*
    symmetric and transitive property of equals(..) not testable, as
    new instances of TaskFunction have to use an auto incremented ID
     */

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
        var task = new TaskFunction<>(identity());

        //noinspection ConstantValue
        assertFalse(task.equals(null), "equals should yield false for null as parameter");
    }

    @Test
    @DisplayName("equals() should return false for two different TaskFunctions with same function")
    void equalsTwoDifferentTaskFunctions() {
        var task1 = new TaskFunction<>(identity());
        var task2 = new TaskFunction<>(identity());

        assertFalse(task1.equals(task2), "equals should yield false for two different TaskFunction instances");
    }

    @Test
    @DisplayName("equals() should return false for identical input and manipulated subclassed function")
    void equalsUnderlyingSubclassedFunction() {
        final int idOfFunc1;
        var func1 = new TaskFunction<Integer, Integer>(SQUARE);

        Class obj = func1.getClass();
        try {
            Field field = obj.getDeclaredField("ID");
            field.setAccessible(true);
            idOfFunc1 = field.getInt(func1);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("ID not found in TaskFunction: " + e);
        }

        var func2 = new TaskFunction<Integer, Integer>(SQUARE) {
            // In case TaskFunction::equals uses getID() which changes symmetry of equals
            public int getID() {
                return idOfFunc1;
            }

            @Override
            // In case TaskFunction::equals uses hashCode() which changes symmetry of equals
            public int hashCode() {
                return func1.hashCode();
            }
        };

        assertFalse(func1.equals(func2), "equals should yield false for task with overwritten methods");
    }

}
