package pgdp.ds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pgdp.ds.MultiStack;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class MultiStackSyncTests {

    TimeoutChecker t;
    long timeout = 1000;

    @Test
    public void multiStackPushTest() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(3);
        MultiStack ms = new MultiStack();
        final int iterations = 1000;

        Future<?>[] futures = new Future[3];
        for(int i = 0; i < 3; i++) {
            final int ii = i;
            futures[i] = pool.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.push(ii);
                }
                return null;
            });
        }
        t = new TimeoutChecker(timeout, futures);
        pool.shutdown();
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Pushing");
        assertEquals(3000, ms.size());
        System.out.println("done");
    }

    @Test
    public void multiStackPopTest() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(3);
        MultiStack ms = new MultiStack();
        final int iterations = 1000;

        for(int i = 0; i < iterations * 3; i++) {
            ms.push(i);
        }

        Future<?>[] futures = new Future[3];
        for(int i = 0; i < 3; i++) {
            futures[i] = pool.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    if(ms.pop() == Integer.MIN_VALUE) fail("popped to often");
                }
            });
        }

        t = new TimeoutChecker(timeout, futures);
        pool.shutdown();
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Popping");
        assertEquals(0, ms.size());
    }

    @Test
    public void multiStackPushPopMixTest() throws InterruptedException {
        var pushers = Executors.newFixedThreadPool(3);
        var poppers = Executors.newFixedThreadPool(3);

        MultiStack ms = new MultiStack();
        final int iterations = 1000;

        for(int i = 0; i < iterations * 3; i++) {
            ms.push(i);
        }

        Future<?>[] futures = new Future[6];
        for(int i = 0; i < 3; i++) {
            final int ii = i;
            futures[2*i] = pushers.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.push(ii);
                }
            });

            futures[2*i+1] = poppers.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.pop();
                }
            });
        }

        t = new TimeoutChecker(timeout, futures);
        pushers.shutdown();
        poppers.shutdown();
        assertFalse(t.isTimeoutReached(), "Timeout reached: Deadlock while Pushing and Popping");

        assertEquals(iterations * 3, ms.size());
    }
}