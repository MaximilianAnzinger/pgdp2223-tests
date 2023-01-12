package pgdp.shuttle.dummies;

import pgdp.shuttle.computer.ShuttleOutput;
import pgdp.shuttle.computer.ShuttleProcessor;
import pgdp.shuttle.computer.TaskChecker;
import pgdp.shuttle.tasks.ShuttleTask;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskCheckerDummy extends TaskChecker {
    public TaskCheckerDummy(List<ShuttleProcessor> processors, ShuttleOutput output) {
        super(processors, output);
    }

    public LinkedBlockingQueue<ShuttleTask<?, ?>> getTaskQueue() throws NoSuchFieldException, IllegalAccessException {
        var f = TaskChecker.class.getDeclaredField("taskQueue");
        f.setAccessible(true);
        return (LinkedBlockingQueue<ShuttleTask<?, ?>>) f.get(this);
    }

    @Override
    public void run() {

    }

    @Override
    public void shutDown() {

    }
}
