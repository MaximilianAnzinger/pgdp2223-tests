package pgdp.ds;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MultiStackMultithreadTest {
    private static final int NUM_TESTCASES = 10000;
    private static String task = "";
    private static String lastTask = "";

    private class TimeoutCheck implements Runnable {
        private final int timeOutMS;
        private final Thread threadToStop;
        private boolean active = true;
        private boolean timeouted = false;
        public TimeoutCheck(int timeOutMS, Thread threadToStop) {
            this.timeOutMS = timeOutMS;
            this.threadToStop = threadToStop;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(timeOutMS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (active) timeout();
        }
        public void timeout() {
            System.out.println("Timeout reached, while '" + task + "' after having done '" + lastTask + "'.");
            threadToStop.stop();
            timeouted = true;
        }

        public boolean hasTimeouted(){
            return timeouted;
        }
        public void deactivate(){
            active = false;
        }
    }

    @DisplayName("Forgotten Locks")
    @Test
    void testSimpleDeadlockForgottenUnlocks(){
        // one Thread, doing all the 
        // pop() should return Integer.MIN_VALUE
        // top() should return Integer.MIN_VALUE
        // size() should return 0
        // search(0) should return -1
        // push(69) should push 69
        // top() should return 69
        // size() should return 1
        // pop() should return 69
        // size() should return 0
        final int timeOutMS = 500;
        MultiStack stack = new MultiStack();
        Runnable routine = () -> {
            lastTask = task;
            task = "popping...";
            assertEquals(stack.pop(), Integer.MIN_VALUE);
            lastTask = task;
            task = "getting top...";
            assertEquals(stack.top(), Integer.MIN_VALUE);
            lastTask = task;
            task = "getting size...";
            assertEquals(stack.size(), 0);
            lastTask = task;
            task = "searching for 0...";
            assertEquals(stack.search(0), -1);
            lastTask = task;
            task = "pushing 69...";
            stack.push(69);
            lastTask = task;
            task = "getting top...";
            assertEquals(stack.top(), 69);
            lastTask = task;
            task = "getting size...";
            assertEquals(stack.size(), 1);
            lastTask = task;
            task = "popping...";
            assertEquals(stack.pop(), 69);
            lastTask = task;
            task = "getting size...";
            assertEquals(stack.size(), 0);
            lastTask = task;
            task = "done.";
        };
        Thread t1 = new Thread(routine);
        TimeoutCheck timeoutChecker = new TimeoutCheck(timeOutMS, t1);
        Thread timeoutThread = new Thread(timeoutChecker);
        timeoutThread.start();
        t1.start();
        try{
            t1.join();
            timeoutChecker.deactivate();
            timeoutThread.join();
            String msg = "Timeout reached, while '" + task + "' after having done '" + lastTask + "'.";
            System.out.println(msg);
            assertFalse(timeoutChecker.hasTimeouted(), msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    void testRaceConditionsPush(){
        // two Threads, each pushing 1000 elements
        // the first thread should push 0-999
        // the second thread should push 1000-1999
        // the final stack should contain 0-1999
        MultiStack stack = new MultiStack();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < NUM_TESTCASES; i++) {
                stack.push(i);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = NUM_TESTCASES; i < 2*NUM_TESTCASES; i++) {
                stack.push(i);
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean[] found = new boolean[2*NUM_TESTCASES];
        for (int i = 0; i < 2*NUM_TESTCASES; i++) {
            int value = stack.pop();
            if (value >= 2*NUM_TESTCASES || value < 0) {
                System.out.println("Value out of range: " + value);
            } else found[value] = true;
        }
        for (int i = 0; i < 2*NUM_TESTCASES; i++) {
            assertEquals(true, found[i]);
        }
    }

    @Test
    void testRaceConditionsPop(){
        // two Threads, each popping 1000 elements
        // the first thread should pop 0-999
        // the second thread should pop 1000-1999
        // the final stack should be empty
        MultiStack stack = new MultiStack();
        for (int i = 0; i < 2*NUM_TESTCASES; i++) {
            stack.push(i);
        }
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < NUM_TESTCASES; i++) {
                stack.pop();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = NUM_TESTCASES; i < 2*NUM_TESTCASES; i++) {
                stack.pop();
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, stack.size());
    }

    @Test
    void testRaceConditionsPushPop(){
        // two Threads:
        // the first thread should push 0-999
        // the second thread should pop 0-999
        // the final stack should be empty
        // no index out of bounds exception should occur
        MultiStack stack = new MultiStack();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < NUM_TESTCASES; i++) {
                stack.push(i);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < NUM_TESTCASES; i++) {
                if(stack.pop() == Integer.MIN_VALUE) {
                    i--; // stack was empty, try again
                }
            }
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, stack.size());
    }

}
