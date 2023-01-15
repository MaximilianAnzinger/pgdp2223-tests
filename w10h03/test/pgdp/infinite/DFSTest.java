package pgdp.infinite;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.infinite.tree.InfiniteTree;
import pgdp.infinite.tree.Optimizable;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class DFSTest {

    @ParameterizedTest
    @MethodSource
    void test(int from, int maxDepth, int searchValue, int expectedResult, List<Integer> expectedSteps) {
        InfiniteTree<Integer> tree = Trees.countUpDown.get();

        ProbeOptimizable probe = new ProbeOptimizable(searchValue);

        Assertions.assertEquals(expectedResult, tree.find(from, maxDepth, probe));
        Assertions.assertEquals(expectedSteps, probe.getValues());
    }

    private static Stream<Arguments> test() {
        return Stream.of(
                Arguments.arguments(0, 10, -10, -10, List.of(0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10)),
                Arguments.arguments(0, 10, 10, 10, List.of(0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)),
                Arguments.arguments(0, 5, -10, -5, List.of(0, -1, -2, -3, -4, -5, 1, 2, 3, 4, 5)),
                Arguments.arguments(0, 5, 10, 5, List.of(0, -1, -2, -3, -4, -5, 1, 2, 3, 4, 5)),
                Arguments.arguments(5, 5, 10, 10, List.of(5, 6, 7, 8, 9, 10)),
                Arguments.arguments(-5, 5, -10, -10, List.of(-5, -6, -7, -8, -9, -10))
        );
    }

    @Test
    void test2() {
        InfiniteTree<Integer> tree = Trees.countUpDown.get();

        List<Integer> values = List.of(0, -1, -2, -3, -4, -5, -6, -7, -8, -9, -10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        ProbeOptimizable probe = new ProbeOptimizable(10);

        Assertions.assertEquals(10, tree.find(0, 10, probe));
        Assertions.assertEquals(values, probe.getValues());

    }

    static class ProbeOptimizable implements Optimizable<Integer> {

        private final int x;
        private Integer optimum;

        private final List<Integer> values = new LinkedList<>();

        public ProbeOptimizable(int x) {
            this.x = x;
        }

        @Override
        public boolean process(Integer x) {

            values.add(x);

            if (optimum == null || Math.abs(this.x - x) < Math.abs(this.x - optimum)) {
                optimum = x;
            }

            return this.x == x;
        }

        @Override
        public Integer getOptimum() {
            return optimum;
        }

        public List<Integer> getValues() {
            return values;
        }

    }

}
