package pgdp.trials;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class TrialOfTheGrassesTest {
    @Test
    void testExample() {
        // Example from main() of TrialOfTheGrasses.java
        TrialOfTheGrasses.TreeNode<Integer> nodes = new TrialOfTheGrasses.TreeNode<>(1,
                new TrialOfTheGrasses.TreeNode<>(2, new TrialOfTheGrasses.TreeNode<>(3, new TrialOfTheGrasses.TreeNode<>(4), new TrialOfTheGrasses.TreeNode<>(5))),
                new TrialOfTheGrasses.TreeNode<>(5),
                new TrialOfTheGrasses.TreeNode<>(6, new TrialOfTheGrasses.TreeNode<>(7), new TrialOfTheGrasses.TreeNode<>(8)));

        Stream<TrialOfTheGrasses.TreeNode<Integer>> stream = nodes.flatten();
        int[] actual = stream.mapToInt(TrialOfTheGrasses.TreeNode::getLabel).toArray();
        int[] expected = { 1, 2, 3, 4, 5, 5, 6, 7, 8 };

        assertArrayEquals(expected, actual);
    }

    @Test
    void testSingle() {
        TrialOfTheGrasses.TreeNode<Integer> nodes = new TrialOfTheGrasses.TreeNode<>(1);

        Stream<TrialOfTheGrasses.TreeNode<Integer>> stream = nodes.flatten();
        int[] actual = stream.mapToInt(TrialOfTheGrasses.TreeNode::getLabel).toArray();
        int[] expected = { 1 };

        assertArrayEquals(expected, actual);
    }
}
