package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.dummies.ProcessorDummy;
import pgdp.shuttle.dummies.TaskCheckerDummy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TaskDistributorTests {

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
    @DisplayName("Should generate all tasks and shut down, with two shuttle processors")
    public void testFinishGenerating() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        ProcessorDummy pd1 = new ProcessorDummy(null);
        ProcessorDummy pd2 = new ProcessorDummy(null);
        var td = new TaskDistributer(5,
                List.of(pd1, pd2),
                new TestTaskGenerator(new Random(69), 0, 1));

        td.start();

        Thread.sleep(30);
        assertEquals("TaskDistributer starting to generate tasks.\n" +
                "TaskDistributer finished generating 5/5 tasks. Shutting down.\n", out.toString());

        var tq1 = pd1.getTaskQueue();
        var tq2 =  pd2.getTaskQueue();

        for(int i = 1; i <= 5; i++) {
            assertEquals("Test Task Nr. " + i, tq1.poll().toString());
            assertEquals("Test Task Nr. " + i, tq2.poll().toString());
        }

        assertTrue(tq1.isEmpty());
        assertTrue(tq2.isEmpty());
    }

    @Test
    @DisplayName("Should shut down correctly")
    public void testShutDown() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        var td = new TaskDistributer(1000,
                List.of(new ProcessorDummy(null), new ProcessorDummy(null), new ProcessorDummy(null), new ProcessorDummy(null)),
                new TestTaskGenerator(new Random(69), 0, 1));

        td.start();

        Thread.sleep(5);
        assertEquals("TaskDistributer starting to generate tasks.\n", out.toString());

        out.reset();
        td.shutDown();

        Thread.sleep(10);
        long taskCount = getCurrentTaskCount(td);
        assertTrue(0 < taskCount && taskCount <= 1000); //Sollte irgendwo im dreistelligen bereich sein

        String output = out.toString();
        assertTrue(output.startsWith("TaskDistributer finished generating "));
        assertTrue(output.endsWith("/1000 tasks. Shutting down.\n"));
    }


    @Test
    @DisplayName("Should shut down correctly when interrupted")
    public void testInterrupt() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        var td = new TaskDistributer(1000,
                List.of(new ProcessorDummy(null), new ProcessorDummy(null), new ProcessorDummy(null), new ProcessorDummy(null)),
                new TestTaskGenerator(new Random(69), 0, 1));

        td.start();

        Thread.sleep(5);
        assertEquals("TaskDistributer starting to generate tasks.\n", out.toString());

        out.reset();
        td.interrupt();

        Thread.sleep(10);
        long taskCount = getCurrentTaskCount(td);
        assertTrue(0 < taskCount && taskCount <= 1000); //Sollte irgendwo im dreistelligen bereich sein
        assertFalse(td.isAlive());
        String output = out.toString();
        assertTrue(output.startsWith("TaskDistributer was interrupted after "));
        assertTrue(output.endsWith(" tasks!\n"));
    }

    private static long getCurrentTaskCount(TaskDistributer td) throws NoSuchFieldException, IllegalAccessException {
        var f = TaskDistributer.class.getDeclaredField("currentTaskCount");
        f.setAccessible(true);
        return (long) f.get(td);
    }
}
