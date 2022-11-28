import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pgdp.ds.DenseGraph;
import pgdp.ds.SparseGraph;

import java.util.Arrays;


class SparseGraphCapabilityTest {
    /**
     * Laut Aufgabenstellung muss der Sparse Graph "mehrere Millionen" Knoten, aber "nur einige wenige Kanten pro
     * Knoten speichern können. Hier können Graphen generiert werden, mit nodes Knoten und edgesPerNode Kanten pro
     * Knoten.
     * Constraints: nodes > 1000, edgesPerNode < nodes
     **/

    private int nodes = 5_000_000;
    private int edgesPerNode = 3;

    @Test
    @DisplayName("Testing SparseGraph")
    public void testSparseGraph() {
        SparseGraph sparseGraph = new SparseGraph(nodes);
        System.out.println(sparseGraph.getNumberOfNodes());

        for(int i = 0; i< nodes; i++){
            for (int j = i; j < i+edgesPerNode; j++){
                if(i == 420 && j == 420){;
                }
                sparseGraph.addEdge(i, j);
            }
        }
        int[] res = new int[edgesPerNode];
        for(int i = 0; i < edgesPerNode; i++){
            int addRes = 420 + i;
            res[i] = addRes;
        }
        Assertions.assertArrayEquals(res, sparseGraph.getAdj(420));
    }

}
