package pgdp.ds.behavior;

import org.junit.jupiter.api.Test;
import pgdp.ds.Graph;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTest<GraphType extends Graph> {


    protected abstract GraphType createGraph(int nodes);

    @Test
    final void edgeShouldBeInMatrixWhenItWasAdded() {
        var graph = createGraph(2);
        graph.addEdge(0, 1);
        assertTrue(graph.isAdj(0, 1));
    }

    @Test
    final void nodeShouldBeNeighborIfConnectionWithEdge() {
        final var graph = createGraph(3);
        final var expectedNeighbors = new int[]{
                0, 2
        };
        graph.addEdge(1, expectedNeighbors[0]);
        graph.addEdge(1, expectedNeighbors[1]);
        assertArrayEquals(expectedNeighbors, graph.getAdj(1));
    }

    @Test
    final void nodeShouldNotHaveNeighborWhenNoEdge() {
        final var graph = createGraph(3);
        final var neighbors = graph.getAdj(2);
        assertArrayEquals(new int[0], neighbors);
    }

    @Test
    final void numberOfNodesShouldBeInitCapacity() {
        final var capacity = 3;
        final var graph = createGraph(capacity);
        assertEquals(capacity, graph.getNumberOfNodes());
    }

    @Test
    final void directionOfEdgeShouldBeRespected() {
        final var graph = createGraph(3);
        graph.addEdge(1, 2);
        final var neighbors = graph.getAdj(2);
        assertArrayEquals(new int[0], neighbors);
    }

    @Test
    final void shouldNotAddEdgeWhenFromIsOutOfRange() {
        final var graph = createGraph(3);
        graph.addEdge(3, 1);
        assertFalse(graph.isAdj(3, 1));
    }
    @Test
    final void shouldNotAddEdgeWhenToIsOutOfRange() {
        final var graph = createGraph(3);
        graph.addEdge(1, 3);
        assertFalse(graph.isAdj(1, 3));
    }

    @Test
    final void shouldNotAddEdgeWhenToIsNegative() {
        final var graph = createGraph(3);
        graph.addEdge(1, -1);
        assertFalse(graph.isAdj(1, -1));
    }

    @Test
    final void shouldNotAddEdgeWhenFromIsNegative() {
        final var graph = createGraph(3);
        graph.addEdge(-1, 1);
        assertFalse(graph.isAdj(-1, 1));
    }

    @Test
    final void shouldSetNumberOfNodesToZeroWhenNegative() {
        final var graph = createGraph(-1);
        assertEquals(0, graph.getNumberOfNodes());
    }
    
    @Test
    final void shouldBeNullWhenNodeDoesNotExist() {
        final var graph = createGraph(5);
        assertArrayEquals(null, graph.getAdj(5));
    }


}
