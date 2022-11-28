package pgdp.ds;

public class DeGraphViz {
    /**
     * Allows for easy conversion of graphViz instructions to actual instructions executed on the given graph.
     * Does not do a lot of error detection, meaning the graphViz instructions should first be checked for correctness
     * on the graphViz website.
     * <p>
     * How to use this method:
     * <ol>
     * <li>Initialize a new graph (sparse or dense) with the correct number of nodes. </li>
     * <li>Create a new String object by first typing the double quotes (""), then pasting the instructions from the website between them (line breaks should be converted to \n).</li>
     * <ul><li>Alternatively: Create a new String object using 6 double quotes ("""""") (called a text block), then pasting the instructions in between the middle two (no \n will show up, but that's fine).</li></ul>
     * <li>3. Call this method and hand it the String and graph.</li>
     * <li>4. Your graph will now have the respective edges.</li>
     * </ol>
     * <p>
     * Note that if you call toGraphViz() on your graph, the output order of certain "x -> y" instructions will differ, yet an identical graph to the one the instructions were copied from will be created.
     *
     * @param graphviz  a String/text block containing the <b>entire</b>> instruction set from the left window of the graphViz website
     * @param g         the graph to create the edges in (graph must first be initialized with correct node count prior to calling this method)
     */
    public static void graphVizToGraph(String graphviz, Graph g){
        String[] instructions = graphviz.split("\n");
        String[] instructionI;
        int from, to;
        for (int i = 1; i < instructions.length-1; i++){
            if (instructions[i].contains("->")){
                instructionI = instructions[i].split(" -> ");
                from = Integer.parseInt(instructionI[0]);
                to = Integer.parseInt(instructionI[1]);
                g.addEdge(from, to);
            }
        }
    }
}
