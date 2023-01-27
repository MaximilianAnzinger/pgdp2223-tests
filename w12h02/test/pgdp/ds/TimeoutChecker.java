package pgdp.ds;

import java.sql.Time;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/*How to use this class:
1. Directly after submitting Tasks:
    Instantiate it with a timeout and an array of futures 
    (return values of pool.submit(Callable))
2. After pool.shutdown():
    Call isTimeoutReached() to check if the timeout was reached.
    This method will block until all tasks are finished or timeout is reached.
*/

public class TimeoutChecker extends Thread{
    private long timeout;
    private Future<?>[] futures;
    private boolean active = true;
    private boolean timeoutReached = false;
    private boolean cancelFuturesOnTimeout;
    private long startTime;
    private int dt = 20;
    public TimeoutChecker(long timeout, Future<?>[] futures, boolean cancelFuturesOnTimeout) {
        this.timeout = timeout;
        this.futures = futures;
        this.cancelFuturesOnTimeout = cancelFuturesOnTimeout;
        this.startTime = System.currentTimeMillis();
        this.start();
    }
    public TimeoutChecker(long timeout, Future<?>[] futures) {
        this(timeout, futures, true);
    }
    @Override
    public void run() {
        while(active) {
            boolean done = true;
            for(Future<?> f : futures) {
                if(!f.isDone()) {
                    done = false;
                }
            }
            if(done) {
                active = false;
            }
            if(System.currentTimeMillis() - startTime > timeout) {
                timeoutReached = true;
                if (this.cancelFuturesOnTimeout) for(Future<?> f : futures) {
                    f.cancel(true);
                }
                timeoutReached = true;
                active = false;
            }
            try{
                Thread.sleep(dt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isTimeoutReached(){
        return isTimeoutReached(true);
    }
    public boolean isTimeoutReached(boolean wait) {
        while(wait && active){
            try {
                Thread.sleep(dt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return timeoutReached;
    }
    public void deactivate() {
        active = false;
    }
}
