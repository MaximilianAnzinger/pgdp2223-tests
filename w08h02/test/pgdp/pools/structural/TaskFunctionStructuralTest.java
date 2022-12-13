package pgdp.pools.structural;

import java.lang.reflect.Modifier;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pools.TaskFunction;

@SuppressWarnings("rawtypes")
public class TaskFunctionStructuralTest extends StructuralTest<TaskFunction> {

    public TaskFunctionStructuralTest() {
        super(TaskFunction.class);
    }

    @Test
    @DisplayName("Check attributes")
    void checkAttributes() {
        expectAttribute("ID", int.class, Modifier.PRIVATE | Modifier.FINAL);
        expectAttribute("function", Function.class, Modifier.PRIVATE | Modifier.FINAL);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(Modifier.PUBLIC, Function.class);
    }

    @Test
    @DisplayName("Check methods")
    void checkMethods() {
        expectMethod(Modifier.PUBLIC, "apply", Object.class, Object.class);
        expectMethod(Modifier.PUBLIC, "equals", boolean.class, Object.class);
        expectMethod(Modifier.PUBLIC, "hashCode", int.class);
    }

}
