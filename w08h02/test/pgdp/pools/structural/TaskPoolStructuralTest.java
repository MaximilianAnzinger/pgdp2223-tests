package pgdp.pools.structural;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pools.Task;
import pgdp.pools.TaskFunction;
import pgdp.pools.TaskPool;

@SuppressWarnings("rawtypes")
public class TaskPoolStructuralTest extends StructuralTest<TaskPool> {

    public TaskPoolStructuralTest() {
        super(TaskPool.class);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(Modifier.PROTECTED);
    }

    @Test
    @DisplayName("Check methods")
    void checkMethods() {
        expectMethod(Modifier.PUBLIC, "insert", Task.class, Task.class);
        expectMethod(Modifier.PUBLIC, "getByValue", Task.class, Object.class, TaskFunction.class);
    }

}
