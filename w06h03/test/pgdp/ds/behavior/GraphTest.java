package pgdp.ds.behavior;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import pgdp.ds.Graph;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTest<GraphType extends Graph> {

    final Class<GraphType> graphType;

    public GraphTest(Class<GraphType> graphType) {
        this.graphType = graphType;
    }

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
        int[] actual = graph.getAdj(1);
        Arrays.sort(actual);
        assertArrayEquals(expectedNeighbors, actual);
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

    private int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    private <T> T switchGraph(T sparse, T dense) {
        return switch (graphType.getSimpleName()) {
            case "SparseGraph" -> sparse;
            case "DenseGraph" -> dense;
            default -> throw new IllegalArgumentException("Unknown graph type");
        };
    }

    @Test
    @Timeout(2)
    final void getAdjShouldBeFast() {
        final var numberOfNodes = switchGraph(1_000_000, 10_000);
        final var numberOfEdges = numberOfNodes * switchGraph(1.5, 8.0);

        final var graph = createGraph(numberOfNodes);
        for (int i = 0; i < numberOfEdges; i++) {
            graph.addEdge(random(0, numberOfNodes - 1), random(0, numberOfNodes - 1));
        }
        for (int i = 0; i < 1000; i++) {
            graph.getAdj(i);
        }
    }


}
