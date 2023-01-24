package pgdp.pengusurvivors;

import java.lang.reflect.Field;
import java.util.List;

import static pgdp.pengusurvivors.FlowGraph.Vertex;

public class GraphUtils {

    public static String stringifyPath(List<Vertex> path) {
        var sb = new StringBuilder();
        for (var vertex : path) {
            sb.append(getName(vertex)).append(" -> ");
        }
        return sb.substring(0, sb.length() - 4);
    }

    public static String getName(Vertex vertex) {
        try {
            Field field = vertex.getClass().getDeclaredField("label");
            field.setAccessible(true);
            var label = (String) field.get(vertex);
            var splitLabel = label.split("-");
            return splitLabel[splitLabel.length - 1].trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printGraph(FlowGraph graph) {
        System.out.println("digraph {");
        for (Vertex vertex : graph.getVertices()) {
            var neighbors = vertex.getSuccessors();
            for (Vertex neighbor : neighbors) {
                var edge = vertex.getEdge(neighbor);
                System.out.println(getName(vertex) + " -> " + getName(neighbor) + " [label=\"" + edge.getFlow() + "/" + edge.getCapacity() + "\"];");
            }
        }
        System.out.println("}");
    }

    public static FlowGraph cloneGraph(FlowGraph graph) {
        FlowGraph clone = new FlowGraph();
        for (Vertex vertex : graph.getVertices()) {
            var clonedVertex = clone.addVertex(getName(vertex));
            var neighbors = vertex.getSuccessors();
            for (Vertex neighbor : neighbors) {
                var clonedNeighbor = clone.addVertex(getName(neighbor));
                var edge = vertex.getEdge(neighbor);
                var cloneEdge = clonedVertex.addEdge(clonedNeighbor, edge.getCapacity());
                GraphUtils.setFlow(cloneEdge, edge.getFlow());
            }
        }
        return clone;
    }

    public static void setFlow(FlowGraph.Edge edge, int flow) {
        try {
            Field field = edge.getClass().getDeclaredField("f");
            field.setAccessible(true);
            field.set(edge, flow);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Vertex getVertex(FlowGraph graph, String name) {
        for (Vertex vertex : graph.getVertices()) {
            if (getName(vertex).equals(name)) {
                return vertex;
            }
        }
        return null;
    }

    /*
     * Moves all normal edges to the residual network. There will be no edges in the normal network anymore.
     */
    public static FlowGraph mirrorToResidual(FlowGraph graph) {
        FlowGraph residual = new FlowGraph();
        for (Vertex vertex : graph.getVertices()) {
            var clonedVertex = residual.addVertex(getName(vertex));
            var neighbors = vertex.getSuccessors();
            for (Vertex neighbor : neighbors) {
                var clonedNeighbor = residual.addVertex(getName(neighbor));
                var edge = vertex.getEdge(neighbor);
                var cloneEdge = clonedVertex.addResEdge(clonedNeighbor, edge.getCapacity());
                GraphUtils.setFlow(cloneEdge, edge.getFlow());
            }
        }
        return residual;
    }


    /*
     * Compares the residual network of the source graph with the normal network of the compareTo Graph.
     */
    public static boolean residualEquals(FlowGraph source, FlowGraph compareTo) {
        if(source.getVertices().size() != compareTo.getVertices().size()) {
            throw new AssertionError("Graphs have different number of vertices");
        }
        for (Vertex vertex : source.getVertices()) {
            var neighbors = vertex.getResSuccessors();
            for (Vertex neighbor : neighbors) {
                var edge = vertex.getResEdge(neighbor);
                Vertex compareToVertex = GraphUtils.getVertex(compareTo, GraphUtils.getName(vertex));
                if(compareToVertex == null) {
                    throw new AssertionError("Vertex " + GraphUtils.getName(vertex) + " not found in compareTo Graph");
                }
                Vertex compareToNeighbor = GraphUtils.getVertex(compareTo, GraphUtils.getName(neighbor));
                var compareToEdge = compareToVertex.getEdge(compareToNeighbor);
                if (edge.getFlow() != compareToEdge.getFlow()) {
                    throw new AssertionError("Flow of edge " + GraphUtils.getName(vertex) + " -> " + GraphUtils.getName(neighbor) + " is not equal. Source: " + edge.getFlow() + " compareTo: " + compareToEdge.getFlow());
                }
                if (edge.getCapacity() != compareToEdge.getCapacity()) {
                    throw new AssertionError("Capacity of edge " + GraphUtils.getName(vertex) + " -> " + GraphUtils.getName(neighbor) + " is not equal. Source: " + edge.getCapacity() + " compareTo: " + compareToEdge.getCapacity());
                }
            }
        }
        return true;
    }

    public static boolean normalEquals(FlowGraph source, FlowGraph compareTo) {
        for (Vertex vertex : source.getVertices()) {
            var neighbors = vertex.getSuccessors();
            for (Vertex neighbor : neighbors) {
                var edge = vertex.getEdge(neighbor);
                var compareToEdge = GraphUtils.getVertex(compareTo, GraphUtils.getName(vertex)).getEdge(GraphUtils.getVertex(compareTo, GraphUtils.getName(neighbor)));
                if (edge.getFlow() != compareToEdge.getFlow()) {
                    return false;
                }
                if (edge.getCapacity() != compareToEdge.getCapacity()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Vertex> generatePath(FlowGraph sourceGraph, List<String> vertexPath) {
        var path = new java.util.ArrayList<Vertex>();
        for (String vertexName : vertexPath) {
            path.add(GraphUtils.getVertex(sourceGraph, vertexName));
        }
        return path;
    }
}
