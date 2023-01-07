package pgdp.ds.behavior;

import pgdp.ds.SparseGraph;

public class SparseGraphTest extends GraphTest<SparseGraph> {

    @Override
    protected SparseGraph createGraph(int nodes) {
        return new SparseGraph(nodes);
    }


}
