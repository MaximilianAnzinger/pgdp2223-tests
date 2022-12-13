package pgdp.pools.structural;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pools.Task;
import pgdp.pools.TaskFactory;
import pgdp.pools.TaskFunction;
import pgdp.pools.TaskPool;

@SuppressWarnings("rawtypes")
public class TaskFactoryTest extends StructuralTest<TaskFactory> {

    public TaskFactoryTest() {
        super(TaskFactory.class);
    }

    @Test
    @DisplayName("Check attributes")
    void checkAttributes() {
        expectAttribute("pool", TaskPool.class, Modifier.PRIVATE | Modifier.FINAL);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(Modifier.PUBLIC);
    }

    @Test
    @DisplayName("Check methods")
    void checkMethods() {
        expectMethod(Modifier.PUBLIC, "create", Task.class, Object.class, TaskFunction.class);
        expectMethod(Modifier.PUBLIC, "intern", Task.class, Task.class);
    }

}
