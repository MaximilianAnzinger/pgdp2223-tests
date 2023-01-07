package pgdp.ds.behavior;

import pgdp.ds.DenseGraph;

public class DenseGraphTest extends GraphTest<DenseGraph> {

    @Override
    protected DenseGraph createGraph(int nodes) {
        return new DenseGraph(nodes);
    }

}
