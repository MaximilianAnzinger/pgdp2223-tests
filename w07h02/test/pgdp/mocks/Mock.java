package pgdp.mocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface Mock {

    HashMap<Object, List<MethodCall>> calls = new HashMap<>();

    default void trackMethodCall(String name, Object... args) {
        MethodCall call = new MethodCall(name, args);
        calls.computeIfAbsent(this, k -> new ArrayList<>()).add(call);
    }

    default List<MethodCall> getMethodCalls(String method) {
        return calls.computeIfAbsent(this, k -> new ArrayList<>()).stream().filter(c -> c.name().equals(method)).toList();
    }

    default List<MethodCall> getMethodCalls() {
        return calls.get(this);
    }
}
