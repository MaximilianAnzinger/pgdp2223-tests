package pgdp.infinite;

import pgdp.infinite.tree.Optimizable;

public class OptimizableComparable<T extends Comparable<T>> implements Optimizable<T> {
    public T best;
    public final T expectedBest;

    public OptimizableComparable(T expect) {
        expectedBest = expect;
    }

    @Override
    public boolean process(T t) {
        // System.out.printf("%s, %s, %s\n", expectedBest.compareTo(t), expectedBest, t);
        if (expectedBest.equals(t)) {
            best = t;
            return true;
        } else if (best == null) {
            best = t;
        } else if (Math.abs(expectedBest.compareTo(t)) < Math.abs(expectedBest.compareTo(best))) {
            best = t;
        }
        return false;
    }

    @Override
    public T getOptimum() {
        return best;
    }

}
