package pgdp.pools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pgdp.pools.FunctionLib.SQUARE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskPoolBehaviorTest {

    @Test
    @DisplayName("Artemis Example #1")
    void artemisExample1() {
        var func = new TaskFunction<>(SQUARE);
        var pool = new TaskPool<Integer, Integer>();

        assertNull(pool.getByValue(1, func), "If a TaskFunction was not inserted into a pool yet, the pool must return null.");
    }

    @Test
    @DisplayName("Artemis Example #2")
    void artemisExample2() {
        var func = new TaskFunction<>(SQUARE);
        var task = new Task<>(1, func);
        var pool = new TaskPool<Integer, Integer>();

        assertEquals(task, pool.insert(task), "If inserting a Task for the first time, it should return the passed parameter.");
    }

    @Test
    @DisplayName("Artemis Example #3")
    void artemisExample3() {
        var func = new TaskFunction<>(SQUARE);
        var task1 = new Task<>(1, func);
        var task2 = new Task<>(1, func);
        var pool = new TaskPool<Integer, Integer>();
        pool.insert(task1);

        assertEquals(task1, pool.insert(task2), "If inserting an identical task again, it should return the already existing task.");
    }

    @Test
    @DisplayName("Artemis Example #4")
    void artemisExample4() {
        var func = new TaskFunction<>(SQUARE);
        var task = new Task<>(1, func);
        var pool = new TaskPool<Integer, Integer>();
        pool.insert(task);

        assertEquals(task, pool.getByValue(1, func), "Querying a Task with getByValue which was added before should yield that task.");
    }

    @Test
    @DisplayName("Check if Compare works right")
    void compareCheck() {

        TaskFunction<Integer, Integer> f = new TaskFunction<>(FunctionLib.SQUARE);
        TaskPool<Integer, Integer> tp = new TaskPool<>();

        Task<Integer, Integer> t1 = new Task<>(1, f);
        Task<Integer, Integer> t2 = new Task<>(1, f);

        assert(t1 == tp.insert(t1));
        assert(t1 == tp.insert(t2));
        assert(t1 == tp.getByValue(1, f));
    }

    // Test written by https://github.com/flennart
    // Adjusted for readability and changed assertion a bit
    @Test
    @DisplayName("Taskpool should not return null for getByValue() for these two Tasks")
    void getByValueTest() {
        var tp = new TaskPool<Integer, Integer>();

        var f1 = new TaskFunction<>(FunctionLib.SQUARE);
        var f2 = new TaskFunction<>(FunctionLib.SUM_OF_HALFS);
        var tf3 = new Task<>(1, f2);
        var tf4 = new Task<>(-1108378656, f1);

        tp.insert(tf3);
        tp.insert(tf4);

        assertEquals(tf4, tp.getByValue(-1108378656, f1));
        assertEquals(tf4, tp.getByValue(-1108378656, f1));
    }

}
