package pgdp.ds;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MultiStackSyncTests {

    @Test
    public void multiStackPushTest() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(3);
        MultiStack ms = new MultiStack();
        final int iterations = 1000;

        for(int i = 0; i < 3; i++) {
            final int ii = i;
            pool.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.push(ii);
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        assertEquals(3000, ms.size());
    }

    @Test
    public void multiStackPopTest() throws InterruptedException {
        var pool = Executors.newFixedThreadPool(3);
        MultiStack ms = new MultiStack();
        final int iterations = 1000;

        for(int i = 0; i < iterations * 3; i++) {
            ms.push(i);
        }

        for(int i = 0; i < 3; i++) {
            pool.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    if(ms.pop() == Integer.MIN_VALUE) fail("popped to often");
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

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

        for(int i = 0; i < 3; i++) {
            final int ii = i;
            pushers.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.push(ii);
                }
            });

            poppers.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    ms.pop();
                }
            });
        }

        pushers.shutdown();
        poppers.shutdown();
        pushers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        poppers.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        assertEquals(iterations * 3, ms.size());
    }
}
