package pgdp.infinite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class InfiniteNodeTest {
    @Test
    public void calculateNextChildTest() {
        var tree = Trees.libraryOfBabel.get();
        var node = tree.withRoot("");

        for (int i = 0; i < 26; i++) {
            var msg = "At call #" + i;

            assertFalse(node.isFullyCalculated(), msg);
            node.calculateNextChild();

            assertEquals(i + 1, node.getChildren().size(), msg);
            assertEquals("" + (char) (i + (int) 'a'), node.getChildren().get(i).getValue(), msg);
        }

        assertTrue(node.isFullyCalculated());
    }

    @Test
    public void calculateAllChildrenTest() {
        var tree = Trees.libraryOfBabel.get();
        var node = tree.withRoot("");

        node.calculateAllChildren();
        assertEquals(26, node.getChildren().size());
        assertTrue(node.isFullyCalculated());
    }

    @Test
    @DisplayName("should not break when calculateNextChild was used before")
    public void calculateAllChildrenWithCalculateNextChildTest() {
        var tree = Trees.libraryOfBabel.get();
        var node = tree.withRoot("");

        for (int i = 0; i < 10; i++) {
            node.calculateNextChild();
        }

        node.calculateAllChildren();
        assertEquals(26, node.getChildren().size());
        assertTrue(node.isFullyCalculated());
    }

    @Test
    @DisplayName("should reset children successfully")
    public void resetChildrenTest() {
        var tree = Trees.libraryOfBabel.get();
        var node = tree.withRoot("");

        node.calculateAllChildren();
        node.resetChildren();

        assertEquals(0, node.getChildren().size());
    }
    
    @Test
    @DisplayName("Test to see if InfiniteTree with \"heavy\" objects can be searched")
    /**
     * This Test creates an Iterator over a List of String Arrays with a length of 500k and growing
     * This Test should run without Problems for 2048MB RAM
     * (add maxHeapSize = "2048m" to your build.gradle)
     * if you don't delete your nodes it will throw an "OutOfMemoryError" and it'll be shown as an "ignored Test"
     */
    public void heavyTreeTest() {

        // A tree with heavy objects (Objects that take up a lot of memory)
        var heavyTree = Trees.makeHeavyChildren.get();

        // Optimizable that finds the longest Array in the tree
        var optimizableLongest = new OptimizableLongest(new String[0]);

        long treeLength = heavyTree.find(new String[100000], 5, optimizableLongest).length;
        long expectedLength = 24300000L;

        assertEquals(expectedLength, treeLength);
    }
}
