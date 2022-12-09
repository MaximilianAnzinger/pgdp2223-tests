package pgdp.tools;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

// Reflections API
import java.lang.reflect.Constructor;

// JUnit
import org.junit.jupiter.api.Test;

// PGDP
import pgdp.pools.TaskFunction;
import pgdp.pools.TaskPool;
import pgdp.pools.FunctionLib;
import pgdp.pools.Task;
import pgdp.pools.TaskFactory;

public class ExampleTest {
    @Test
    public void TaskFunctionTest() {
        TaskFunction<Integer, Integer> f1 = new TaskFunction<>(FunctionLib.SQUARE);
        TaskFunction<Integer, Integer> f2 = new TaskFunction<>(FunctionLib.SUM_OF_HALFS);
        TaskFunction<Integer, Integer> f3 = new TaskFunction<>(FunctionLib.SQUARE);

        assertFalse(f1.equals(f2));
        assertFalse(f1.equals(f3));
        assertTrue(f1.equals(f1));
        assertEquals(4, f1.apply(2));
    }

    @Test
    public void TaskTest() {
        TaskFunction<Integer, Integer> f1 = new TaskFunction<>(FunctionLib.INC);
        TaskFunction<Integer, Integer> f2 = new TaskFunction<>(FunctionLib.INC);

        var t1 = new Task<Integer, Integer>(1, f1);
        var t2 = new Task<Integer, Integer>(1, f1);
        var t3 = new Task<Integer, Integer>(1, f2);

        assertTrue(t1.equals(t2));
        assertFalse(t1.equals(t3));
        assertEquals(2, t1.getResult());
    }

    @Test
    public void TaskPoolTest() {
        try {

            TaskFunction<Integer, Integer> f = new TaskFunction<>(FunctionLib.SQUARE);

            // Access protected method
            // https://stackoverflow.com/questions/5629706/java-accessing-private-constructor-with-type-parameters

            Constructor<TaskPool> constructor = TaskPool.class.getDeclaredConstructor(Object.class);
            constructor.setAccessible(true);

            TaskPool<Integer, Integer> tp = constructor.newInstance();

            assertNull(tp.getByValue(1, f));

            Task<Integer, Integer> t1 = new Task<>(1, f);
            Task<Integer, Integer> t2 = new Task<>(1, f);

            assertEquals(t1, tp.insert(t1));
            assertEquals(t1, tp.insert(t2));
            assertEquals(t1, tp.getByValue(1, f));
        } catch (Exception e) {
            fail("TaskPoolTest failed with exception [" + e.getClass().getName() + "]");
            e.printStackTrace();
        }
    }

    @Test
    public void TaskFactoryTest() {
        TaskFactory<Integer, Integer> tf = new TaskFactory<>();
        TaskFunction<Integer, Integer> f = new TaskFunction<>(FunctionLib.SQUARE);

        Task<Integer, Integer> t1 = tf.create(5, f);
        Task<Integer, Integer> t2 = new Task<>(5, f);

        assertEquals(t1, tf.create(5, f));
        assertEquals(t1, tf.intern(t2));
    }
}
