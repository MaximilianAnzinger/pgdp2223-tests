package pgdp.shuttle;

import org.junit.jupiter.api.Test;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.tasks.ShuttleTask;
import pgdp.shuttle.tasks.TaskGenerator;

import java.util.Random;
import java.util.function.Function;

public class TestTaskGenerator implements TaskGenerator {

    private static final Function<Integer, String> TEST_TASK = (i) -> "Test Task Nr. " + i + " completed successfully!";
    private static final Function<Integer, String> SLOW_TASK = (i) -> {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {}
        return "Slow Task, slept 50ms " + i;
    };

    private final Random rand;
    private int errorProb;
    private int index = 1; //indexing at 1 :p
    private int lower;
    private int upper;

    /**
     * Generates tasks and evaluates them a random number of times. Upper and lower are the bounds for how often the tasks should be evaluated.
     * @param rand
     * @param lower
     * @param upper
     */
    public TestTaskGenerator(int lower, int upper, int errorProb) {
        this.rand = new Random(69);
        this.lower = lower;
        this.upper = upper;
        this.errorProb = errorProb;
    }

    public TestTaskGenerator() {
        this(0, 0, 0);
    }

    @Override
    public ShuttleTask<?, ?> generateTask() {
        boolean errorTask = this.rand.nextInt(100) + 1 < errorProb;

        int evaluationNumber = (upper <= 1) ? 0 : this.rand.nextInt(upper - lower) + lower;
        final int idx = index++;

        var task = new ShuttleTask<>(idx, TEST_TASK) {
            @Override
            public String toString() {
                if(errorTask) {
                    return "Error Task";
                }
                return "Test Task Nr. " + idx;
            }
        };

        for(int i = 0; i < evaluationNumber; i++) task.evaluate();
        return task;
    }

    public ShuttleTask<?, ?> generateSlowTask() {
        final int idx = index++;
        var task = new ShuttleTask<>(idx, SLOW_TASK) {
            @Override
            public String toString() {
                return "Slow Task Nr. " + idx;
            }
        };

        return task;
    }
}