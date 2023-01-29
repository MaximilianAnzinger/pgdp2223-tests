package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskDistributer;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.shuttle.ReflectionHelper.getCurrentTaskCount;
import static pgdp.shuttle.ReflectionHelper.getTaskQueue;

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
        ShuttleProcessor ps1 = new ShuttleProcessor(null);
        ShuttleProcessor ps2 = new ShuttleProcessor(null);
        var td = new TaskDistributer(5,
                List.of(ps1, ps2),
                new TestTaskGenerator());

        td.start();

        Thread.sleep(30);
        assertEquals("TaskDistributer starting to generate tasks.\n" +
                "TaskDistributer finished generating 5/5 tasks. Shutting down.\n", out.toString());

        var tq1 = getTaskQueue(ps1);
        var tq2 =  getTaskQueue(ps2);

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

        var td = new TaskDistributer(100000,
                List.of(new ShuttleProcessor(null), new ShuttleProcessor(null),
                        new ShuttleProcessor(null), new ShuttleProcessor(null)),
                new TestTaskGenerator());

        td.start();

        Thread.sleep(5);
        assertEquals("TaskDistributer starting to generate tasks.\n", out.toString());

        out.reset();
        td.shutDown();

        Thread.sleep(10);
        long taskCount = getCurrentTaskCount(td);
        assertTrue(0 < taskCount && taskCount <= 100000); //Sollte irgendwo im drei bis vierstelligen bereich sein

        String output = out.toString();
        assertTrue(output.startsWith("TaskDistributer finished generating "));
        assertTrue(output.endsWith("/100000 tasks. Shutting down.\n"));
        assertFalse(td.isAlive());
    }


    @Test
    @DisplayName("Should shut down correctly when interrupted")
    public void testInterrupt() throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        var td = new TaskDistributer(100000,
                List.of(new ShuttleProcessor(null), new ShuttleProcessor(null),
                        new ShuttleProcessor(null), new ShuttleProcessor(null)),
                new TestTaskGenerator());

        td.start();

        Thread.sleep(3);
        assertEquals("TaskDistributer starting to generate tasks.\n", out.toString());

        out.reset();
        td.interrupt();

        Thread.sleep(10);
        long taskCount = getCurrentTaskCount(td);
        assertTrue(0 < taskCount && taskCount <= 100000); //Sollte irgendwo im dreistelligen bereich sein
        assertFalse(td.isAlive());
        String output = out.toString();
        assertTrue(output.startsWith("TaskDistributer was interrupted after "));
        assertTrue(output.endsWith(" tasks!\n"));
    }

    @Test
    @DisplayName("Should quit immediately when there are to tasks to distribute")
    public void testZeroTasks() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        var td = new TaskDistributer(0,
                List.of(new ShuttleProcessor(null), new ShuttleProcessor(null),
                        new ShuttleProcessor(null), new ShuttleProcessor(null)), null);

        try {
            td.run();
        } catch(NullPointerException e) {
            fail("Called generateTask was called even though there was no task to generate");
        }

        assertEquals("TaskDistributer starting to generate tasks.\n" +
                "TaskDistributer finished generating 0/0 tasks. Shutting down.\n", out.toString());
        assertFalse(td.isAlive());
    }
}
