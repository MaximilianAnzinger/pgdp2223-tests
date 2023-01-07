package pgdp.infinite;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Lambdas {
    private Lambdas() {}

    /**
     * @name Artemis Example
     * @description Generates Tree that follows Artemis Example
     */
    public static final Function<Integer, Iterator<Integer>> binaryCounter = i -> {
        return List.of(i * 2, i * 2 + 1).iterator();
    };

    /**
     * @name Reverse Collatz Calculator
     * @description Generates Tree with different integer algorithm
     */
    public static final Function<Integer, Iterator<Integer>> reverseCollatz = i -> {
        return List.of(i * 2, (i - 1) / 3).iterator();
    };

    /**
     * @name Inconsistent Child Count
     * @description In this Generated Tree, Child Elements have different further
     *              child elements and the values in the tree can repeat.
     */
    public static final Function<Integer, Iterator<Integer>> childAnomaly = i -> {
        if (i <= 0)
            return Collections.emptyIterator();
        return IntStream.range(0, i).iterator();
    };

    /**
     * @name Library of Babel
     * @description Generates Non-Integer Example
     */
    public static final Function<String, Iterator<String>> libraryOfBabel = i -> {
        return "abcdefghijklmnopqrstuvwxyz".chars().mapToObj(j -> i + (char) j).iterator();
    };
}
