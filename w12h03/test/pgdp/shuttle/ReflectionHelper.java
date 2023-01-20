package pgdp.shuttle;

import pgdp.shuttle.computer.ShuttleOutput;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskChecker;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.tasks.ShuttleTask;

import java.util.concurrent.LinkedBlockingQueue;

public class ReflectionHelper {

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(TaskChecker tc) throws NoSuchFieldException, IllegalAccessException {
        var f = TaskChecker.class.getDeclaredField("taskQueue");
        f.setAccessible(true);
        return (LinkedBlockingQueue<ShuttleTask<?, ?>>) f.get(tc);
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(ShuttleProcessor sp) throws NoSuchFieldException, IllegalAccessException {
        var f = ShuttleProcessor.class.getDeclaredField("taskQueue");
        f.setAccessible(true);
        return (LinkedBlockingQueue<ShuttleTask<?, ?>>) f.get(sp);
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(ShuttleOutput so) throws NoSuchFieldException, IllegalAccessException {
        var f = ShuttleOutput.class.getDeclaredField("taskQueue");
        f.setAccessible(true);
        return (LinkedBlockingQueue<ShuttleTask<?, ?>>) f.get(so);
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getPrioTaskQueue(ShuttleProcessor sp) throws NoSuchFieldException, IllegalAccessException {
        var f = ShuttleProcessor.class.getDeclaredField("priorityTaskQueue");
        f.setAccessible(true);
        return (LinkedBlockingQueue<ShuttleTask<?, ?>>) f.get(sp);
    }

    public static long getCurrentTaskCount(TaskDistributer td) throws NoSuchFieldException, IllegalAccessException {
        var f = TaskDistributer.class.getDeclaredField("currentTaskCount");
        f.setAccessible(true);
        return (long) f.get(td);
    }
}
