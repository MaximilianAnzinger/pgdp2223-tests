package pgdp.pools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pgdp.pools.FunctionLib.SQUARE;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskFactoryBehaviorTest {

    private static Field fieldPool;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException {
        fieldPool = TaskFactory.class.getDeclaredField("pool");
        fieldPool.setAccessible(true);
    }

    @Test
    @DisplayName("Artemis Example #1")
    void artemisExample1() {
        var factory = new TaskFactory<Integer, Integer>();
        var func = new TaskFunction<>(SQUARE);
        var task = factory.create(5, func);

        assertEquals(task, factory.create(5, func), "Creating a task again should yield the already existing Task.");
    }

    @Test
    @DisplayName("Artemis Example #2")
    void artemisExample2() {
        var factory = new TaskFactory<Integer, Integer>();
        var func = new TaskFunction<>(SQUARE);
        var task1 = factory.create(5, func);
        var task2 = new Task<>(5, func);

        assertEquals(task1, factory.intern(task2), "intern should return the equal, already existing task instead of the method parameter.");
    }

    @Test
    @DisplayName("intern on non-existant object causes it to get created")
    void internOnNonExistant() throws IllegalAccessException {
        var factory = new TaskFactory<Integer, Integer>();
        var func = new TaskFunction<>(SQUARE);
        var task = new Task<>(5, func);

        //noinspection unchecked
        var pool = (TaskPool<Integer, Integer>) fieldPool.get(factory);
        assertNull(pool.getByValue(task.getInput(), task.getTaskFunction()), "Expected task not to exist in the pool yet");
        assertEquals(task, factory.intern(task), "Expected intern to return registered task");
        assertEquals(task, pool.getByValue(task.getInput(), task.getTaskFunction()), "Expected task to have been registered by factory");
    }

}
