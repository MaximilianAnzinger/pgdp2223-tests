package pgdp.infinite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class InfiniteTreeTest {
    @Test
    public void withRootTest() {
        var tree = Trees.binaryCounter.get();
        var node = tree.withRoot(0);
        assertEquals(0, node.getValue());
    }

    @Test
    public void withRootStringTest() {
        var tree = Trees.libraryOfBabel.get();
        var node = tree.withRoot("");
        assertEquals("", node.getValue());
    }

    @ParameterizedTest(name = "{5}")
    @MethodSource
    public void findTest(int from, int depth, int searchVal, int expected, String message, String name) {
        var tree = Trees.countUpDown.get();
        var optimizable = new OptimizableComparable<>(searchVal) {
            @Override
            public boolean process(Integer t) {
                // System.out.printf("%s, %s, %s\n", expectedBest.compareTo(t), expectedBest, t);
                if (expectedBest.equals(t)) {
                    best = t;
                    return true;
                } else if (best == null) {
                    best = 0;
                } else if (Math.abs(expectedBest - t) < Math.abs(expectedBest - best)) {
                    best = t;
                }
                return false;
            }
        };
        assertEquals(expected, tree.find(from, depth, optimizable), message);
    }

    private static Stream<Arguments> findTest() {
        return Stream.of(
                arguments(
                        0, 420, 420, 420,
                        "Your implementation does not search deep enough!",
                        "searches deep enough"
                ),
                arguments(
                        0, 419, 420, 419,
                        "Your implementation searches to deep!",
                        "does not exceed search depth"
                ),
                arguments(-1, 420, 420, 0,
                        "Your implementation should not have found anything but did not return the optimum value!",
                        "returns optimum if no element found"
                )
        );
    }

    @Test
    public void artemisExample() {
        // root am Anfang, vor jeder weiteren Berechnung:

        var tree = Trees.binaryCounter.get();
        var root = tree.withRoot(0);

        assertEquals(0, root.getValue());

        // Nach dem Aufruf root.calculateAllChildren():

        root.calculateAllChildren();

        assertEquals(0, root.getChildren().get(0).getValue());
        assertEquals(1, root.getChildren().get(1).getValue());

        assertTrue(root.isFullyCalculated());

        // Nach dem Aufruf root.getChildren().get(0).calculateNextChild():

        root.getChildren().get(0).calculateNextChild();

        assertEquals(0, root.getChildren().get(0).getChildren().get(0).getValue());
        assertEquals(1, root.getChildren().get(0).getChildren().size());

        assertFalse(root.getChildren().get(0).isFullyCalculated());

        // Nach dem Aufruf root.getChildren().get(0).calculateNextChild():

        root.getChildren().get(0).calculateNextChild();

        assertEquals(1, root.getChildren().get(0).getChildren().get(1).getValue());
        assertEquals(2, root.getChildren().get(0).getChildren().size());

        assertTrue(root.getChildren().get(0).isFullyCalculated());
        assertFalse(root.getChildren().get(1).isFullyCalculated());
        
        // Nach dem Aufruf root.getChildren().get(1).calculateAllChildren():
        
        root.getChildren().get(1).calculateAllChildren();

        assertEquals(2, root.getChildren().get(1).getChildren().get(0).getValue());
        assertEquals(3, root.getChildren().get(1).getChildren().get(1).getValue());

        assertTrue(root.getChildren().get(1).isFullyCalculated());
        
        // Nach dem Aufruf root.getChildren().get(1).resetChildren():
        
        root.getChildren().get(1).resetChildren();

        assertFalse(root.getChildren().get(1).isFullyCalculated());
    }
    
    @Test
    @DisplayName("Test to see if InfiniteTree with many children can be searched")
    /**
     * This Test creates an Iterator over a List of 100k Integers (every node has 100k children)
     * You should delete every child after checking it
     * This Test should run without Problems for 2048MB RAM
     * (add maxHeapSize = "2048m" to your build.gradle)
     * if you don't delete your nodes it will throw an "OutOfMemoryError" and it'll be shown as an "ignored Test"
     */
    public void manyChildrenTreeTest() {

        // A tree with heavy objects (Objects that take up a lot of memory)
        var manyChildrenTree = Trees.makeManyChildren.get();

        // Optimizable that finds the longest Array in the tree
        var anyOptimizable = new pgdp.infinite.OptimizableComparable<>(2);

        int result = manyChildrenTree.find(1, 1, anyOptimizable);
        int expected = 1;

        long l = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1_000_000;
        System.out.println("Used: " + l + "MB of RAM for tree with many children");

        assertEquals(expected, result);
    }
}
