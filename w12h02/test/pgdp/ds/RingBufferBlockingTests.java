package pgdp.ds;

import org.junit.jupiter.api.Test;
import pgdp.ds.RingBuffer;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferBlockingTests {

    @Test
    public void twoThreadedTest() throws InterruptedException {
        var rb = new RingBuffer(2);

        //test get method

        Thread consumerThread = new Thread(() -> {
            try {
                assertEquals(0x10101, rb.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        consumerThread.start();
        Thread.sleep(5);

        assertTrue(consumerThread.isAlive());
        rb.put(0x10101);

        Thread.sleep(5);
        assertFalse(consumerThread.isAlive());

        //test put method

        rb.put(0); rb.put(0); //fill up

        Thread producerThread = new Thread(() -> {
            try {
                rb.put(0xBEEF);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        Thread.sleep(5);

        assertTrue(producerThread.isAlive());

        rb.get();
        Thread.sleep(5);
        assertFalse(producerThread.isAlive());
        rb.get();
        assertEquals(0xBEEF, rb.get());
    }

    @Test
    public void multiThreadedTest() throws InterruptedException {
        var rb = new RingBuffer(10);

        //test get method

        var consumerPool = Executors.newFixedThreadPool(3);
        var producerPool = Executors.newFixedThreadPool(3);
        final int iterations = 300;

        for(int i = 1; i < 4 ; i++) {
            final int ii = i;
            consumerPool.submit(() -> {
                try {
                    for(int j = 0; j < iterations; j++) assertNotEquals(Integer.MIN_VALUE, rb.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            producerPool.submit(() -> {
                try {
                    for(int j = 0; j < iterations; j++) rb.put(ii);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        consumerPool.shutdown();
        producerPool.shutdown();
        consumerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        producerPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        assertTrue(rb.isEmpty());
    }
}
