package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleOutput;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskChecker;
import pgdp.shuttle.tasks.ShuttleTask;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.shuttle.ReflectionHelper.getTaskQueue;

public class ShuttleProcessorTests {

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream() {
        // returns platform independent captured output
        @Override
        public synchronized String toString() {
            return super.toString().replace("\r", "");
        }
    };
    private static final PrintStream stdOut = System.out;

    @AfterEach
    public void resetBuffer() {
        out.reset();
    }

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(stdOut);
    }

    @Test
    @DisplayName("Should evaluate priority task first")
    public void testEvaluatePriority() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var taskchecker = new TaskChecker(null, null);
        var sp = new ShuttleProcessor(taskchecker);
        var taskGen = new TestTaskGenerator(new Random(69), 0, 5);

        var task1 = taskGen.generateTask();
        var task2 = taskGen.generateTask();
        var task3 = taskGen.generateTask();
        var task4 = taskGen.generateTask();

        var taskList = List.of(task1, task3, task2, task4);

        sp.addPriorityTask(task1);
        sp.addTask(task2);
        sp.addPriorityTask(task3);
        sp.addTask(task4);

        sp.start();
        Thread.sleep(30);

        var taskQueue = getTaskQueue(taskchecker);
        for(int i = 0; i < 4; i++) {
            assertEquals(taskList.get(i), taskQueue.poll());
        }

        sp.shutDown();
        Thread.sleep(30);

        assertEquals("ShuttleProcessor shutting down.\n", out.toString());
    }


    @Test
    @DisplayName("Should still work when task are added while running")
    public void testAddTaskWhileRunning() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var taskchecker = new TaskChecker(null, null);
        var sp = new ShuttleProcessor(taskchecker);
        var taskGen = new TestTaskGenerator(new Random(69), 0, 5);

        sp.start();
        Thread.sleep(10);

        var task1 = taskGen.generateTask();
        var task2 = taskGen.generateTask();
        var task3 = taskGen.generateTask();
        var task4 = taskGen.generateTask();

        var taskList = List.of(task1, task3, task2, task4);

        synchronized (sp) {
            sp.addPriorityTask(task1);
            sp.addTask(task2);
            sp.notify();
            Thread.sleep(5);
            sp.addPriorityTask(task3);
            sp.addTask(task4);
            sp.notify();
        }

        Thread.sleep(30);

        var taskQueue = ReflectionHelper.getTaskQueue(taskchecker);
        for(int i = 0; i < 4; i++) {
            assertEquals(taskList.get(i), taskQueue.poll());
        }

        sp.shutDown();
        Thread.sleep(30);

        assertEquals("ShuttleProcessor shutting down.\n", out.toString());
    }

    @Test
    @DisplayName("Should wait until current task has finished evaluating when shut down")
    public void shutDownTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var taskchecker = new TaskChecker(null, null);
        var sp = new ShuttleProcessor(taskchecker);
        var taskGen = new TestTaskGenerator(new Random(69), 0, 5);

        sp.start();
        sp.addTask(taskGen.generateSlowTask());

        Thread.sleep(10);
        sp.shutDown();

        Thread.sleep(10);

        assertEquals("ShuttleProcessor shutting down.\n", out.toString());
        assertTrue(sp.isAlive(), "Should have waited for slow task to finish evaluating.");

        Thread.sleep(35);

        assertFalse(sp.isAlive(), "Slow task finished, the thread should have shut down by now.");
    }

    @Test
    @DisplayName("Should output correct message when interrupted")
    public void interruptTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var taskchecker = new TaskChecker(null, null);
        var sp = new ShuttleProcessor(taskchecker);
        var taskGen = new TestTaskGenerator(new Random(69), 0, 5);

        sp.start();
        for(int i = 0; i < 1000; i++) sp.addTask(taskGen.generateTask());

        Thread.sleep(5);
        sp.interrupt();
        Thread.sleep(5);
        assertEquals("ShuttleProcessor was interrupted. Shutting down.\n", out.toString());
        assertFalse(sp.isAlive());
    }

    @DisplayName("Should only process new tasks when notified + should notify taskchecker")
    public void waitNotifyTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var taskchecker = new TaskChecker(null, null);
        var sp = new ShuttleProcessor(taskchecker);
        var taskGen = new TestTaskGenerator(new Random(69), 0, 5);

        sp.start();
        for(int i = 0; i < 1000; i++) sp.addTask(taskGen.generateTask());

        Thread.sleep(5);
        sp.interrupt();
        Thread.sleep(5);
        assertEquals("ShuttleProcessor was interrupted. Shutting down.\n", out.toString());
        assertFalse(sp.isAlive());
    }}
