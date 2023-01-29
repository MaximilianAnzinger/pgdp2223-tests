package pgdp.shuttle;

import org.junit.jupiter.api.*;
import pgdp.shuttle.computer.ShuttleComputer;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.tasks.ErrorProneTaskGenerator;
import pgdp.shuttle.tasks.ErrorlessTaskGenerator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ShuttleComputerTests {


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
    @DisplayName("Errorless Shuttle Computer test")
    public void testShuttleComputerErrorless() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        ShuttleComputer sc = new ShuttleComputer(4, new TestTaskGenerator(), 100);
        sc.start();

        Thread.sleep(150);
        String[] lines = out.toString().split("\n");

        assertEquals("ShuttleComputer booting up.", lines[0]);
        assertEquals("TaskDistributer starting to generate tasks.", lines[1]);


        //the next 7 entries should contain "Test task x" 4 times and TaskDistributor shutdown
        boolean foundTDShutdownSequence = false;
        boolean foundSCShutdownSequence = false;
        boolean foundSOShutdownSequence = false;
        boolean foundTCShutdownSequence = false;

        int shuttleProcessorShutdownSequences = 0;

        int task1 = 0;
        int task2 = 0;
        int task3 = 0;
        int task4 = 0;

        for (int i = 2; i < lines.length; i++) {
            String l = lines[i];
            if (l.equals("TaskDistributer finished generating 4/4 tasks. Shutting down.")) {
                foundTDShutdownSequence = true;
            } else if (l.equals("Result: Test Task Nr. 1 completed successfully!")) {
                task1++;
            } else if (l.equals("Result: Test Task Nr. 2 completed successfully!")) {
                task2++;
            } else if (l.equals("Result: Test Task Nr. 3 completed successfully!")) {
                task3++;
            } else if (l.equals("Result: Test Task Nr. 4 completed successfully!")) {
                task4++;
            } else if (l.equals("ShuttleOutput shutting down.")) {
                foundSOShutdownSequence = true;
            } else if (l.equals("TaskChecker shutting down.")) {
                foundTCShutdownSequence = true;
            } else if (l.equals("ShuttleComputer shutting down.")) {
                foundSCShutdownSequence = true;
            } else if (l.equals("ShuttleProcessor shutting down.")) {
                shuttleProcessorShutdownSequences++;
            } else {
                fail("Unexpected output: " + l);
            }
        }

        assertEquals(1, task1);
        assertEquals(1, task2);
        assertEquals(1, task3);
        assertEquals(1, task4);

        assertTrue(foundTDShutdownSequence);
        assertTrue(foundSCShutdownSequence);
        assertTrue(foundSOShutdownSequence);
        assertTrue(foundTCShutdownSequence);

        assertEquals(4, shuttleProcessorShutdownSequences);
    }


    @Test
    @DisplayName("Errorprone Shuttle Computer test")
    public void testShuttleComputerErrorprone() throws InterruptedException, NoSuchFieldException, IllegalAccessException {
        ShuttleComputer sc = new ShuttleComputer(69, new TestTaskGenerator(0, 1, 30), 100);
        sc.start();

        Thread.sleep(150);
        String[] lines = out.toString().split("\n");

        assertEquals("ShuttleComputer booting up.", lines[0]);
        assertEquals("TaskDistributer starting to generate tasks.", lines[1]);


        //the next 7 entries should contain "Test task x" 4 times and TaskDistributor shutdown
        boolean foundTDShutdownSequence = false;
        boolean foundSCShutdownSequence = false;
        boolean foundSOShutdownSequence = false;
        boolean foundTCShutdownSequence = false;

        int shuttleProcessorShutdownSequences = 0;

        int error = 0;
        int noError = 0;


        for (int i = 2; i < lines.length; i++) {
            String l = lines[i];
            if (l.equals("TaskDistributer finished generating 69/69 tasks. Shutting down.")) {
                foundTDShutdownSequence = true;
            } else if (l.equals("Result: Error")) {
                error++;
            } else if (l.startsWith("Result: Test Task Nr. ")) {
                noError++;
            } else if (l.equals("ShuttleOutput shutting down.")) {
                foundSOShutdownSequence = true;
            } else if (l.equals("TaskChecker shutting down.")) {
                foundTCShutdownSequence = true;
            } else if (l.equals("ShuttleComputer shutting down.")) {
                foundSCShutdownSequence = true;
            } else if (l.equals("ShuttleProcessor shutting down.")) {
                shuttleProcessorShutdownSequences++;
            } else {
                fail("Unexpected output: " + l);
            }
        }

        assertEquals(69, error + noError);
        assertTrue(error < 25, "this might fail by chance, but its very unlikely.");


        assertTrue(foundTDShutdownSequence);
        assertTrue(foundSCShutdownSequence);
        assertTrue(foundSOShutdownSequence);
        assertTrue(foundTCShutdownSequence);

        assertEquals(4, shuttleProcessorShutdownSequences);
    }
}
