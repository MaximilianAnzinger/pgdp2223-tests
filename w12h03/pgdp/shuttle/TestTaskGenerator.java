package pgdp.shuttle;

import org.junit.jupiter.api.Test;
import pgdp.shuttle.computer.TaskDistributer;
import pgdp.shuttle.tasks.ShuttleTask;
import pgdp.shuttle.tasks.TaskGenerator;

import java.util.Random;
import java.util.function.Function;

public class TestTaskGenerator implements TaskGenerator {

    private static final Function<Integer, String> TEST_TASK = (i) -> "Test task " + i;
    private static final Function<Integer, String> SLOW_TASK = (i) -> {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {}
        return "Slow Task, slept 50ms " + i;
    };

    private final Random rand;
    private int index = 1; //indexing at 1 :p
    private int lower;
    private int upper;

    public TestTaskGenerator(Random rand, int lower, int upper) {
        this.rand = rand;
        this.lower = lower;
        this.upper = upper;
    }
    @Override
    public ShuttleTask<?, ?> generateTask() {
        int evaluationNumber = this.rand.nextInt(upper - lower) + lower;
        final int idx = index++;
        var task = new ShuttleTask<>(idx, TEST_TASK) {
            @Override
            public String toString() {
                return "Test Task Nr. " + idx;
            }
        };

        for(int i = 0; i < evaluationNumber; i++) task.evaluate();
        task.computationSuccessfull();
        return task;
    }


    public ShuttleTask<?, ?> generateSlowTask() {
        final int idx = index++;
        var task = new ShuttleTask<>(idx, SLOW_TASK) {
            @Override
            public String toString() {
                return "Test Task Nr. " + idx;
            }
        };

        return task;
    }

}
