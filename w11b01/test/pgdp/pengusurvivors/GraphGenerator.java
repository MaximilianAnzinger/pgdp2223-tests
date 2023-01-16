package pgdp.pengusurvivors;

import java.util.HashMap;

public class GraphGenerator {

    /*
     * Returns a flow graph parsed from the given string.
     * The string must be formatted as follows:
     * a -> b (1/1); a -> c (2/0); b -> c; c -> d (3/0);
     * This would imply the following graph:
     *                a
     *               / \
     *              b - c
     *                  |
     *                  d
     * A edge is defined as following: <identifier> -> <identifier> [(<number>/<number>)].
     * The first number is the flow, the second number is the capacity.
     * The flow is optional and defaults to 0.
     * The capacity is optional and defaults to 1.
     */
    public static GeneratedGraph generateGraph(String verticeString, String source, String sink) {

        var vertices = new HashMap<String, FlowGraph.Vertex>();
        var graph = new FlowGraph();

        var lines = verticeString.split(";");

        for (var line : lines) {
            if (line.isBlank()) continue;
            var parts = line.split("->");
            var sourceName = parts[0].trim();
            var targetName = parts[1].split("\\(")[0].trim();
            var capacity = 1;
            var flow = 0;
            if (parts[1].contains("(")) {
                var capacityAndFlow = parts[1].split("\\(")[1].split("\\)")[0].split("/");
                flow = Integer.parseInt(capacityAndFlow[0]);
                capacity = Integer.parseInt(capacityAndFlow[1]);
            }

            var sourceVertex = vertices.get(sourceName);
            if (sourceVertex == null) {
                sourceVertex = graph.addVertex(sourceName);
                vertices.put(sourceName, sourceVertex);
            }

            var targetVertex = vertices.get(targetName);
            if (targetVertex == null) {
                targetVertex = graph.addVertex(targetName);
                vertices.put(targetName, targetVertex);
            }

            var edge = sourceVertex.addEdge(targetVertex, capacity);
            GraphUtils.setFlow(edge, flow);
            if (sourceName.equals(source)) {
                graph.setSource(sourceVertex);
            }

            if (targetName.equals(sink)) {
                graph.setSink(targetVertex);
            }
        }

        return new GeneratedGraph(graph, vertices);
    }

    public static GeneratedGraph generateGraph(String verticeString) {
        return generateGraph(verticeString, null, null);
    }


}
