package pgdp.pengusurvivors;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResidualGraphTest {


    @ParameterizedTest
    @MethodSource("getResidualGraphArguments")
    void shouldGenerateCorrectResidualGraph(String source, String expectedResidual) {
        var sourceGraph = GraphGenerator.generateGraph(source).graph();
        var residualGraph = GraphGenerator.generateGraph(expectedResidual).graph();
        sourceGraph.generateResidualGraph();
        assertTrue(GraphUtils.residualEquals(sourceGraph, residualGraph));
    }

    static Stream<Arguments> getResidualGraphArguments() {
        return Stream.of(
                Arguments.of(
                        """
                                a -> b (4/10);
                                b -> c (2/6);
                                """,
                        """
                                a -> b (0/6);
                                b -> a (0/4);
                                b -> c (0/4);
                                c -> b (0/2);
                                """
                )
        );
    }

    @ParameterizedTest
    @MethodSource("findPathArguments")
    void findPath(String verticeString, String source, String sink, List<List<String>> expectedPaths) {
        var graphResult = GraphGenerator.generateGraph(
                verticeString,
                source,
                sink
        );
        var graph = graphResult.graph();
        GraphUtils.printGraph(graph);
        var vertices = graphResult.vertices();
        graph.generateResidualGraph();
        var path = graph.findPathInResidual();

        System.out.println("Your path was: " + (path == null ? "None found" : GraphUtils.stringifyPath(path)));

        var matchingPaths = 0;
        if (expectedPaths.isEmpty()) {
            if (path != null)
                throw new AssertionError("Expected no path, but found " + GraphUtils.stringifyPath(path));
            return;
        }
        pathloop:
        for (var expectedPath : expectedPaths) {
            if (path == null || expectedPath.size() != path.size()) continue;
            for (int i = 0; i < path.size(); i++) {
                var expectedVertex = vertices.get(expectedPath.get(i));
                var actualVertex = path.get(i);
                if (expectedVertex != actualVertex) continue pathloop;
            }
            matchingPaths++;
        }
        assertEquals(1, matchingPaths, "No matching path was found.");

    }

    static Stream<Arguments> findPathArguments() {
        return Stream.of(
                Arguments.of("""
                        s -> a;
                        s -> b;
                        a -> c;
                        b -> c;
                        """, "s", "c", List.of(List.of("s", "b", "c"), List.of("s", "a", "c"))),
                Arguments.of(
                        """
                                s -> a;
                                a -> b;
                                a -> c;
                                b -> c;
                                b -> t;
                                c -> t;
                                """,
                        "s", "b", List.of(List.of("s", "a", "b"))
                ),
                Arguments.of(
                        """
                                s -> a;
                                a -> b;
                                a -> c;
                                b -> c;
                                b -> t;
                                c -> t;
                                """,
                        "s", "t", List.of(List.of("s", "a", "b", "t"), List.of("s", "a", "c", "t"), List.of("s", "a", "b", "c", "t"))
                ),
                Arguments.of(
                        """
                                a -> b (0/1);
                                b -> (0/0);
                                """,
                        "a", "c", List.of()
                ),
                Arguments.of( // tests with potential loops
                        """
                                s -> a;
                                a -> b;
                                a -> t;
                                b -> c;
                                c -> a;
                                b -> t;
                                c -> t;
                                """,
                        "s", "t", List.of(List.of("s", "a", "b", "t"), List.of("s", "a", "t"), List.of("s", "a", "b", "c", "t"))
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getUpdateNetworkArguments")
    void updateNetwork(List<String> vertexPath, int augFlow, String source, String expected, String expectedResidual) {
        var sourceGraph = GraphGenerator.generateGraph(source).graph();
        var expectedGraph = GraphGenerator.generateGraph(expected).graph();
        var expectedResidualGraph = GraphGenerator.generateGraph(expectedResidual).graph();
        sourceGraph.generateResidualGraph();
        var path = GraphUtils.generatePath(sourceGraph, vertexPath);
        sourceGraph.updateNetwork(path, augFlow);
        assertTrue(GraphUtils.residualEquals(sourceGraph, expectedResidualGraph));
        assertTrue(GraphUtils.normalEquals(sourceGraph, expectedGraph));
    }

    static Stream<Arguments> getUpdateNetworkArguments() {
        return Stream.of(
                Arguments.of(
                        List.of("a", "b", "c"),
                        2,
                        """
                                a -> b (4/10);
                                b -> c (2/6);
                                """,
                        """
                                a -> b (6/10);
                                b -> c (4/6);
                                """,
                        """
                                a -> b (0/4);
                                b -> a (0/6);
                                b -> c (0/2);
                                c -> b (0/4);
                                """
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getMaxFlowArguments")
    void getMaxFlow(String graph, String source, String sink, int expected) {
        var sourceGraph = GraphGenerator.generateGraph(graph, source, sink).graph();
        assertEquals(expected, sourceGraph.computeMaxFlowValue());
    }

    static Stream<Arguments> getMaxFlowArguments() {
        return Stream.of(
                Arguments.of(
                        """
                                a -> b (0/10);
                                b -> c (0/6);
                                """,
                        "a", "c",
                        6
                )
        );
    }


}
