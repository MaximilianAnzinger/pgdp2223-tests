package pgdp.infinite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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

    @Test
    public void depthTest() {
        var tree = Trees.binaryCounter.get();
        var optimizable = new OptimizableComparable<Integer>(8);
        tree.withRoot(1);
        assertEquals(8, tree.find(0, 5, optimizable));
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
}
