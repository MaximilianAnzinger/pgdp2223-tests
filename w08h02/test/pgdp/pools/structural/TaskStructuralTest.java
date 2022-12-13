package pgdp.pools.structural;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pools.Task;
import pgdp.pools.TaskFunction;

@SuppressWarnings("rawtypes")
public class TaskStructuralTest extends StructuralTest<Task> {

    public TaskStructuralTest() {
        super(Task.class);
    }

    @Test
    @DisplayName("Check attributes")
    void checkAttributes() {
        expectAttribute("input", Object.class, Modifier.PRIVATE | Modifier.FINAL);
        expectAttribute("taskFunction", TaskFunction.class, Modifier.PRIVATE | Modifier.FINAL);
        expectAttribute("result", Object.class, Modifier.PRIVATE);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(Modifier.PROTECTED, Object.class, TaskFunction.class);
    }

    @Test
    @DisplayName("Check methods")
    void checkMethods() {
        expectMethod(Modifier.PUBLIC, "equals", boolean.class, Object.class);
        expectMethod(Modifier.PUBLIC, "hashCode", int.class);
    }

    @Test
    @DisplayName("Check getters")
    void checkGetters() {
        expectMethod(Modifier.PUBLIC, "getInput", Object.class);
        expectMethod(Modifier.PUBLIC, "getResult", Object.class);
        expectMethod(Modifier.PUBLIC, "getTaskFunction", TaskFunction.class);
    }

}
