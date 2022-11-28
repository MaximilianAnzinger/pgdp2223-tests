package pgdp.ds.behavior;

import pgdp.ds.DenseGraph;

public class DenseGraphTest extends GraphTest<DenseGraph> {

    public DenseGraphTest() {
        super(DenseGraph.class);
    }

    @Override
    protected DenseGraph createGraph(int nodes) {
        return new DenseGraph(nodes);
    }

}
