package pgdp.shuttle;

import pgdp.shuttle.computer.ShuttleOutput;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskChecker;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.tasks.ErrorProneTaskGenerator;
import pgdp.shuttle.tasks.ShuttleTask;

import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ReflectionHelper {

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(TaskChecker tc) throws NoSuchFieldException, IllegalAccessException {
        return getField(tc, TaskChecker.class, "taskQueue");
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(ShuttleProcessor sp) throws NoSuchFieldException, IllegalAccessException {
        return getField(sp, ShuttleProcessor.class, "taskQueue");
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue(ShuttleOutput so) throws NoSuchFieldException, IllegalAccessException {
        return getField(so, ShuttleOutput.class, "taskQueue");
    }

    public static LinkedBlockingQueue<ShuttleTask<?, ?>> getPrioTaskQueue(ShuttleProcessor sp) throws NoSuchFieldException, IllegalAccessException {
        return getField(sp, ShuttleProcessor.class, "priorityTaskQueue");
    }

    public static long getCurrentTaskCount(TaskDistributer td) throws NoSuchFieldException, IllegalAccessException {
        return getField(td, TaskDistributer.class, "currentTaskCount");
    }

    public static <R> List<R> getResults(ShuttleTask<?,?> task) throws NoSuchFieldException, IllegalAccessException {
        return getField(task, ShuttleTask.class, "computedResults");
    }


    private static <T> T getField(Object obj, Class clazz, String name) throws NoSuchFieldException, IllegalAccessException {
        var f = clazz.getDeclaredField(name);
        f.setAccessible(true);
        return (T) f.get(obj);
    }
}
