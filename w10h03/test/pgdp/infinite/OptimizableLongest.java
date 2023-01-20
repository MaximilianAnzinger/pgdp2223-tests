package pgdp.infinite;

import pgdp.infinite.tree.Optimizable;

public class OptimizableLongest<T extends Object> implements Optimizable<T> {
    public T optimumLongest;

    public OptimizableLongest(T optimumStart) {
        optimumLongest = optimumStart;
    }

    @Override
    public boolean process(T value) {
        String[] valueAsArray = (String[]) value;
        String[] optimumAsArray = (String[]) optimumLongest;
        if (valueAsArray.length > optimumAsArray.length) {
            optimumLongest = value;
        }
        return false;
    }

    @Override
    public T getOptimum() {
        return optimumLongest;
    }
}
