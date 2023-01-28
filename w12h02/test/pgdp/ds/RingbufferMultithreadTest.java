package pgdp.ds;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;

public class RingbufferMultithreadTest {
    int N_REPS = 100000;
    @Test
    void testForgottenUnlocksWrite() throws InterruptedException{
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(3);
        Runnable routine = () -> {
            try{ringBuffer.put(1);
            ringBuffer.get();
            ringBuffer.put(1);
            ringBuffer.get();

            ringBuffer.put(1);
            ringBuffer.put(2);
            ringBuffer.put(3);
            ringBuffer.get();
            ringBuffer.get();
            ringBuffer.get();}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[]{pool.submit(routine)};
        TimeoutChecker t = new TimeoutChecker(1000, futures);
        pool.shutdown();
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Putting-Getting");
    }

    @Test
    void testForgottenUnlocksRead(){
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(1);
        Runnable routine = () -> {
            try{ringBuffer.put(1);
            ringBuffer.isFull();
            ringBuffer.isEmpty();
            ringBuffer.get();
            ringBuffer.isEmpty();
            ringBuffer.isFull();}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[1];
        futures[0] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(1000, futures);
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Reading");
    }

    @Test
    void testRaceConditionsPush(){
        var pool = Executors.newFixedThreadPool(2);
        RingBuffer ringBuffer = new RingBuffer(2*N_REPS);
        Runnable routine = () -> {
            for (int i = 0; i < N_REPS; i++) {
                try{ringBuffer.put(1);}catch(InterruptedException e){}
            }
        };
        Future<?>[] futures = new Future<?>[2];
        futures[0] = pool.submit(routine);
        futures[1] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(1000, futures);
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Putting");
        assertTrue(ringBuffer.isFull());
    }

    @Test
    void testRaceConditionsGet() throws InterruptedException{
        var pool = Executors.newFixedThreadPool(2);
        RingBuffer ringBuffer = new RingBuffer(2*N_REPS);
        for (int i = 0; i < 2*N_REPS; i++) {
            ringBuffer.put(i);
        }
        Runnable routine = () -> {
            for (int i = 0; i < N_REPS; i++) {
                try{ringBuffer.get();}catch(InterruptedException e){}
            }
        };
        Future<?>[] futures = new Future<?>[2];
        futures[0] = pool.submit(routine);
        futures[1] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(1000, futures);
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Putting");
        assertTrue(ringBuffer.isEmpty());
    }

    @Test
    void testRaceConditionsPushGet(){
        var pool = Executors.newFixedThreadPool(2);
        RingBuffer ringBuffer = new RingBuffer(2*N_REPS);
        Runnable routine = () -> {
            for (int i = 0; i < N_REPS; i++) {
                try{ringBuffer.put(1);
                ringBuffer.get();}catch(InterruptedException e){}
            }
        };
        Future<?>[] futures = new Future<?>[2];
        futures[0] = pool.submit(routine);
        futures[1] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(1000, futures);
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Putting");
        assertTrue(ringBuffer.isEmpty());
    }

    @Test
    void shouldWaitWhenFull() throws InterruptedException{
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(1);
        ringBuffer.put(1);
        Runnable routine = () -> {
            try{ringBuffer.put(1);}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[1];
        futures[0] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(500, futures);
        pool.shutdown();
        assertTrue(t.isTimeoutReached(), "Does not wait.");
        assertTrue(ringBuffer.isFull());
    }

    @Test
    void shouldWaitWhenEmpty(){
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(1);
        Runnable routine = () -> {
            try{ringBuffer.get();}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[1];
        futures[0] = pool.submit(routine);
        TimeoutChecker t = new TimeoutChecker(500, futures);

        assertTrue(t.isTimeoutReached(), "Does not wait.");
        assertTrue(ringBuffer.isEmpty());
    }

    @Test
    void shouldAwakeWhenNotEmptyAnymore() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(1);
        Runnable routine = () -> {
            try{ringBuffer.get();}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[1];
        futures[0] = pool.submit(routine);
        TimeoutChecker t1 = new TimeoutChecker(100, futures, false);

        try{Thread.sleep(200);}catch(InterruptedException e){}
        boolean b = t1.isTimeoutReached(false);
        System.out.println(b);
        assertTrue(b, "Does not wait");

        TimeoutChecker t2 = new TimeoutChecker(1000, futures);
        ringBuffer.put(1);
        assertFalse(t2.isTimeoutReached(), "Does not proceed wait.");
    }

    @Test
    void shouldAwakeWhenNotFullAnymore() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(1);
        RingBuffer ringBuffer = new RingBuffer(1);
        ringBuffer.put(1);
        Runnable routine = () -> {
            try{ringBuffer.put(2);}catch(InterruptedException e){}
        };
        Future<?>[] futures = new Future<?>[1];
        futures[0] = pool.submit(routine);
        TimeoutChecker t1 = new TimeoutChecker(100, futures, false);

        try{Thread.sleep(200);}catch(InterruptedException e){}
        boolean b = t1.isTimeoutReached();
        System.out.println(b);
        assertTrue(b, "Does not wait");

        TimeoutChecker t2 = new TimeoutChecker(1000, futures);
        ringBuffer.get();
        assertFalse(t2.isTimeoutReached(), "Does not proceed wait.");
    }
}
