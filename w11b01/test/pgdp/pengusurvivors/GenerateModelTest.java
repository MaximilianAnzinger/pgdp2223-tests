package pgdp.pengusurvivors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GenerateModelTest {
    // Only Tests for correct flow-rate. Not for correctness of the graph.
    @Test
    public void basicFlow() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {{0, 0}, {1, 1}, {2, 2}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(3, maxPairs);
    }

    @Test
    public void noFlow() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(0, maxPairs);
    }

    @Test
    public void oneSplit() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {{0, 0}, {0, 1}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(1, maxPairs);
    }

    @Test
    public void oneMerge() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {{0, 0}, {1, 0}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(1, maxPairs);
    }

    @Test
    public void merge3on2() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2};
        int[][] friendships = {{0, 1}, {1, 0}, {2, 1}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(2, maxPairs);
    }

    @Test
    public void split2in3() {
        int[] workaholics = {1, 2};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {{0, 0}, {0, 2}, {1, 1}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(2, maxPairs);
    }

    @Test
    public void fullyConnected3on3() {
        int[] workaholics = {1, 2, 3};
        int[] procrastinators = {1, 2, 3};
        int[][] friendships = {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}};
        FlowGraph graph = PenguSurvivors.generateModel(workaholics, procrastinators, friendships);
        int maxPairs = graph.computeMaxFlowValue();
        assertEquals(3, maxPairs);
    }



}
