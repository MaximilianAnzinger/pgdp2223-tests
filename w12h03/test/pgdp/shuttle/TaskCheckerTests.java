package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleOutput;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskChecker;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.shuttle.ReflectionHelper.getTaskQueue;

public class TaskCheckerTests {

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
    @DisplayName("Check single task successful")
    public void testSingleTaskSuccessful() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        TestTaskGenerator gen = new TestTaskGenerator(4, 5, 0);
        ShuttleOutput so = new ShuttleOutput();
        TaskChecker tc = new TaskChecker(null, so);

        tc.addTask(gen.generateTask());
        tc.start();

        Thread.sleep(10);
        var task = getTaskQueue(so).poll();
        assertNotNull(task);

        tc.shutDown();

        Thread.sleep(10);
        assertEquals("TaskChecker shutting down.\n", out.toString());
    }


    @Test
    @DisplayName("Check single task not ready.")
    public void testSingleTaskIgnore() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        TestTaskGenerator gen = new TestTaskGenerator();
        TaskChecker tc = new TaskChecker(null, null);

        var task = gen.generateTask();
        tc.addTask(task);
        tc.start();

        //if this throws a nullpointer exception, you called a method on shuttleProcessor or Shuttleoutput even though the task should be ignored

        Thread.sleep(10);

        assertNull(getTaskQueue(tc).poll(), "Task should have been ignored and left in the task queue");

        tc.shutDown();

        Thread.sleep(10);
        assertEquals("TaskChecker shutting down.\n", out.toString());
        assertFalse(tc.isAlive());

    }

    @Test
    @DisplayName("Check single task fail.")
    public void testSingleTaskFail() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        TestTaskGenerator gen = new TestTaskGenerator(2, 3, 0);
        var list = List.of(new ShuttleProcessor(null), new ShuttleProcessor(null));
        TaskChecker tc = new TaskChecker(list, null);

        var task = gen.generateTask();
        ReflectionHelper.getResults(task).addAll(List.of("nuh", "uh"));

        tc.addTask(task);
        tc.start(); //if this throws a nullpointer exception, you called a method on shutt

        Thread.sleep(10);

        assertEquals(task, ReflectionHelper.getPrioTaskQueue(list.get(0)).poll(), "Did not add failed task to priority queue of processor 1");
        assertEquals(task, ReflectionHelper.getPrioTaskQueue(list.get(1)).poll(), "Did not add failed task to priority queue of processor 2");

        tc.shutDown();

        Thread.sleep(10);
        assertEquals("TaskChecker shutting down.\n", out.toString());
        assertFalse(tc.isAlive());

    }

    @Test
    @DisplayName("Should output correct message when interrupted")
    public void interruptTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var tc = new TaskChecker(null, new ShuttleOutput());
        var taskGen = new TestTaskGenerator( 0, 5, 0);

        tc.start();
        for(int i = 0; i < 1000; i++) tc.addTask(taskGen.generateTask());

        Thread.sleep(5);
        tc.interrupt();
        Thread.sleep(5);
        assertEquals("TaskChecker was interrupted. Shutting down.\n", out.toString());
        assertFalse(tc.isAlive());
    }

    @Test
    @DisplayName("Should only check new tasks when notified")
    public void waitNotifyTest() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var tc = new TaskChecker(null, new ShuttleOutput());
        var taskGen = new TestTaskGenerator( 0, 5, 0);

        tc.start();
        Thread.sleep(5);

        getTaskQueue(tc).add(taskGen.generateTask());
        Thread.sleep(5);

        assertEquals("", out.toString(), "TaskChecker should have waited until notified before processing new tasks!");
        synchronized (tc) {
            tc.notify();
        }

        Thread.sleep(5);
        assertEquals(0, getTaskQueue(tc).size());
        tc.shutDown();
    }
}
