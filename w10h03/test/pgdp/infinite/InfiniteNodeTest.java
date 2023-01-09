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

        var testIterator = Lambdas.libraryOfBabel.apply("");

        for (int childrenSize = 1; testIterator.hasNext(); childrenSize++) {
            var msg = "At call #" + childrenSize;

            assertFalse(node.isFullyCalculated(), msg);
            node.calculateNextChild();

            assertEquals(childrenSize, node.getChildren().size(), msg);
            assertEquals(testIterator.next(), node.getChildren().get(childrenSize - 1).getValue(), msg);
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
}
