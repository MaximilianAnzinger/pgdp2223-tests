package pgdp.sim;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class MoveMethodTest {

    @BeforeEach
    void setDefaults() {
        SimConfig.plantReproductionCost = 0;
        // has to be grater than 0 so in case of a faulty implementation rng does not throw an exception
        SimConfig.plantMaxGrowth = 1;
        SimConfig.plantMinGrowth = 0;
        SimConfig.hamsterFoodConsumption = 0;
        SimConfig.hamsterConsumedFood = 0;
        SimConfig.hamsterReproductionCost = 0;
        SimConfig.hamsterInitialFood = 0;
        SimConfig.pinguFoodConsumption = 0;
        SimConfig.pinguConsumedFood = 0;
        SimConfig.pinguReproductionCost = 0;
        SimConfig.pinguInitialFood = 0;
        SimConfig.wolfFoodConsumption = 0;
        SimConfig.wolfConsumedFood = 0;
        SimConfig.wolfReproductionCost = 0;
        SimConfig.wolfInitialFood = 0;
    }

    @Test
    public void testMove() {
        var states = new String[]{"""
                . . . . .
                . . . . .
                . . p . .
                . . . . .
                . . . . .""","""
                . . . . .
                . . . . .
                . . . . .
                . p . . .
                . . . . .""", """
                . . . . .
                . . . . .
                . . p . .
                . . . . .
                . . . . .""", """
                . . . . .
                . . . . .
                . . . . .
                . p . . .
                . . . . .""", """
                . . . . .
                . . . . .
                p . . . .
                . . . . .
                . . . . ."""};
        SimConfig.pinguInitialFood = 5;
        SimConfig.pinguReproductionCost = 6;
        SimConfig.pinguConsumedFood = 1;
        SimulationTest.runTest("never", 5, 5, states);
    }


    @Test
    public void testMove2() {
        var states = new String[]{"""
                g . . . w
                g . . . w
                g . . . w
                g . . . w
                g . . . w""","""
                g . . . w
                g . . . w
                g . . . w
                g . . . w
                g . . . w""", """
                g . . . w
                g . . . .
                g . . w w
                g . . . w
                g . . . w""","""
                g . . . w
                g . . . .
                g . . . w
                g . . w w
                g . . . w""","""
                g . . . w
                g . . . .
                g . . . w
                g . . w w
                g . . . w""","""
                g . . . w
                g . . . .
                g . . . w
                g . . w .
                g . . w w""","""
                g . . . w
                g . . . .
                g . . . w
                g . . . w
                g . . w w"""};
        SimConfig.plantReproductionCost = 999;
        SimConfig.wolfReproductionCost = 999;
        SimulationTest.runTest("gonna", 5, 5, states);
    }

    @Test
    public void testMoveDifferentWidthHeight() {
        var states = new String[]{"""
                . . . . . . .
                . h . h . h .
                . . . . . . .""","""
                . . . . . . .
                . . h . . h .
                . . . . h . .""","""
                . h . . . . .
                . . . . h . .
                . . . . . h .""","""
                . h . . . . .
                . . . . h . .
                . . . . . h .""","""
                . h . . . . .
                . . . h . . .
                . . . . . h .""","""
                . h . . . . .
                . . . . . h .
                . . h . . . ."""};

        SimConfig.hamsterReproductionCost = 123;
        SimulationTest.runTest("give", 7, 3, states);
    }


    @Test
    public void testMoveWithWolvesAndPingus() {
        var states = new String[]{"""
                . . . . . . .
                . w . p . w .
                . p . w . p .
                . . . . . . .""", """
                . . . p . . .
                w . . . w . .
                . . . . . p .
                . p . . w . .""", """
                . . . p . . .
                . w . . w . .
                . . . . . p .
                p . . w . . .""", """
                . . . . p . .
                . w . w . . .
                . . . . . . p
                p . . w . . .""", """
                . . w . p . .
                . . . . . . .
                . . w . . . .
                p . w . . p .""", """
                . . w . . . .
                . . w . p . .
                . . . . . . .
                p . w . . p .""", """
                . . . . . . .
                . w . . . p .
                . . . w . . .
                p w . . . p .""", """
                . . . . . . .
                . . . . p . .
                . w . . w p .
                p w . . . . ."""
        };

        SimConfig.wolfReproductionCost = 123;
        SimConfig.pinguReproductionCost = 123;
        SimulationTest.runTest("you", 7, 4, states);
    }

    @Test
    public void testMove1DArray() {
        var states = new String[]{
                "w . p . w . w . p .",
                ". w p . w . . w . p",
                ". w . p w . . w . p",
                ". w . p . w . . w p",
                ". . w p . w . . w p",
                ". . w p w . . . w p",
                ". . w p w . . . w p",
                ". . w p w . . . w p",
                ". . w p w . . . w p",
                ". . w p w . . . w p",
                ". . w p w . . . w p"
        };

        SimConfig.pinguReproductionCost = 123;
        SimConfig.wolfReproductionCost = 123;
        SimulationTest.runTest("up", 10, 1, states);
    }
}
