package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleOutput;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static pgdp.shuttle.ReflectionHelper.getTaskQueue;

public class ShuttleOutputTests {

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
    @DisplayName("Should output and shut down correctly when all tasks are added before starting")
    public void testOutput1() throws InterruptedException {
        var so = new ShuttleOutput();
        var taskGen = new TestTaskGenerator(4, 5, 0);

        for(int i = 0; i < 5; i++) {
            var task = taskGen.generateTask();
            task.computationSuccessfull();
            so.addTask(task);
        }

        so.start();
        Thread.sleep(30);

        assertEquals("Result: Test Task Nr. 1 completed successfully!\n" +
                "Result: Test Task Nr. 2 completed successfully!\n" +
                "Result: Test Task Nr. 3 completed successfully!\n" +
                "Result: Test Task Nr. 4 completed successfully!\n" +
                "Result: Test Task Nr. 5 completed successfully!\n", out.toString());

        out.reset();
        so.shutDown();
        Thread.sleep(30);

        assertFalse(so.isAlive());
        assertEquals("ShuttleOutput shutting down.\n", out.toString());
    }

    @Test
    @DisplayName("Should output and shut down correctly when tasks are added after starting")
    public void testOutput2() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var so = new ShuttleOutput();
        var taskGen = new TestTaskGenerator(4, 5, 0);

        so.start();
        for(int i = 0; i < 5; i++) {
            Thread.sleep(5);

            var task = taskGen.generateTask();
            task.computationSuccessfull();
            getTaskQueue(so).add(task);

            synchronized (so) {
                so.notify();
            }
        }

        Thread.sleep(30);

        assertEquals("Result: Test Task Nr. 1 completed successfully!\n" +
                "Result: Test Task Nr. 2 completed successfully!\n" +
                "Result: Test Task Nr. 3 completed successfully!\n" +
                "Result: Test Task Nr. 4 completed successfully!\n" +
                "Result: Test Task Nr. 5 completed successfully!\n", out.toString());

        out.reset();
        so.shutDown();
        Thread.sleep(30);

        assertEquals("ShuttleOutput shutting down.\n", out.toString());
    }

    @Test
    @DisplayName("Should shut down correctly after interrupt")
    public void testInterrupt() throws InterruptedException {
        var so = new ShuttleOutput();

        so.start();

        Thread.sleep(5);

        so.interrupt();
        Thread.sleep(10);

        assertEquals("ShuttleOutput was interrupted. Shutting down.\n", out.toString());
        assertFalse(so.isAlive());
    }

    @Test
    @DisplayName("Should wait when idle and only process new tasks when notified")
    public void testWaitWhenIdle() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var so = new ShuttleOutput();

        so.start();
        Thread.sleep(5);

        assertEquals(Thread.State.WAITING, so.getState());
        var task = new TestTaskGenerator(4, 5, 0).generateTask();
        task.computationSuccessfull();
        getTaskQueue(so).add(task);

        Thread.sleep(5);
        assertEquals("", out.toString(), "Should have waited until notified of a change.");

        synchronized (so) {
            so.notify();
        }

        Thread.sleep(5);
        assertEquals("Result: Test Task Nr. 1 completed successfully!\n", out.toString(), "Should have processed task after being notified.");
        so.shutDown();
    }
}
