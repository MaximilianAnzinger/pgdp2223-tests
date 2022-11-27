import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pgdp.ds.DenseGraph;


class DenseGraphCapabilityTest {
      /**
       * Laut Aufgabenstellung muss der Dense Graph "vllt. einige Zehntausend" Knoten, aber "von jedem Knoten aus Kanten
       * zu einem großen Teil der anderen Knoten haben. Hier wird ein Graph generiert, der n Knoten hat, und eine Kante
       * zwischen allen Knoten (inkl. Schleifen), damit also n² Kanten.
       * Durch Anpassung des Parameters n kann jede beliebige Anzahl an Kanten getestet werden.
       * Nach jedem Test wird stichprobenartig getestet, ob alle Nachbarn des Knotens mit der ID 0 korrekt gespeichert
       * wurden.
       **/
      private int n = 60000;


    @Test
    @DisplayName("Testing DenseGraph for specified amount of nodes")
    public void testDenseGraph() {
        System.out.println("Nodes: " + n);
        System.out.println("Edges: " + ((long)n*(long)n));
        DenseGraph denseGraph = new DenseGraph(n);
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                denseGraph.addEdge(i, j);
            }
        }

        int[] res = new int[n];
        for (int i = 0; i < res.length; i++){
            res[i] = i;
        }
        Assertions.assertArrayEquals(res, denseGraph.getAdj(1));

    }

    @Test
    @DisplayName("Testing DenseGraph for storing 10,000 Nodes and 100,000,000 Edges")
    public void testDenseGraph10k() {
        int x = 10000;
        DenseGraph denseGraph = new DenseGraph(x);
        for(int i = 0; i < x; i++){
            for (int j = 0; j < x; j++){
                denseGraph.addEdge(i, j);
            }
        }
        int[] res = new int[x];
        for (int i = 0; i < res.length; i++){
            res[i] = i;
        }
        Assertions.assertArrayEquals(res, denseGraph.getAdj(420));
    }

    @Test
    @DisplayName("Testing DenseGraph for storing 20,000 Nodes and 400,000,000 Edges")
    public void testDenseGraph20k() {
        int x = 20000;
        DenseGraph denseGraph = new DenseGraph(x);
        for(int i = 0; i < x; i++){
            for (int j = 0; j < x; j++){
                denseGraph.addEdge(i, j);
            }
        }
        int[] res = new int[x];
        for (int i = 0; i < res.length; i++){
            res[i] = i;
        }
        Assertions.assertArrayEquals(res, denseGraph.getAdj(420));
    }

    @Test
    @DisplayName("Testing DenseGraph for storing 30,000 Nodes and 900,000,000 Edges")
    public void testDenseGraph30k() {
        int x = 30000;
        DenseGraph denseGraph = new DenseGraph(x);
        for(int i = 0; i < x; i++){
            for (int j = 0; j < x; j++){
                denseGraph.addEdge(i, j);
            }
        }
        int[] res = new int[x];
        for (int i = 0; i < res.length; i++){
            res[i] = i;
        }
        Assertions.assertArrayEquals(res, denseGraph.getAdj(420));
    }


}