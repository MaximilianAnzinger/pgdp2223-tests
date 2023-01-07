package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class SimulationTest {

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

    static void runTest(String seed, int width, int height, String[] states) {
        try {
            runTest(seed.getBytes(StandardCharsets.UTF_8), width, height, states);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    static void runTest(byte[] seed, int width, int height, String[] states) throws IllegalAccessException, NoSuchFieldException {
        RandomGenerator.reseed(seed);

        Field cellsField = Simulation.class.getDeclaredField("cells");
        cellsField.setAccessible(true);

        var cells = FieldVisualizeHelper.fieldDescriptionToCellArray(states[0], width, height);
        var sim = new Simulation(cells, width, height);
        for (int i = 1; i < states.length; i++) {
            sim.tick();
            Cell[] currentCells = (Cell[]) cellsField.get(sim);
            Assertions.assertEquals(states[i] + "\n", FieldVisualizeHelper.cellArrayToFieldDescription(currentCells, width, height, false), "State " + i + ", " + (i + 1) + ".ter String");
        }
    }

    @Test
    @DisplayName("Single Plant should reproduce and create Plant colony")
    void singlePlant() {
        var states = new String[]{"""
                g . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                g g . . .
                g . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                g g g . .
                g g . . .
                g . . . .
                . . . . .
                . . . . .""", """
                g g g . .
                g g g . .
                g g . . .
                . . . . .
                . . . . .""", """
                g g g g .
                g g g g .
                g g g g .
                g g . . .
                . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 0;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("Single pingu should reproduce and create Pingu Colony")
    void singlePingu() {
        var states = new String[]{"""
                p . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p . . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p p . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p p p . .
                p p . . .
                p . . . .
                . . . . .
                . . . . .""", """
                p p p p .
                p p p . .
                p p . . .
                . p . . .
                . . . . ."""};
        SimConfig.pinguInitialFood = 3;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 1;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("Pingu eats plant and reproduces")
    void pinguEatsPlant() {
        var states = new String[]{"""
                p g . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p . . . .
                p . . . .
                . . . . .
                . . . . .
                . . . . ."""};
        SimConfig.pinguInitialFood = 1;
        SimConfig.pinguConsumedFood = 2;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 2;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("Hamster eats plant, reproduces, wolf eats hamster child, reproduces")
    void hamsterEatsPlantWolfEatsHamster() {
        var states = new String[]{"""
                h g . . .
                . . . . .
                w . . . .
                . . . . .
                . . . . .""", """
                h . . . .
                h . . . .
                w . . . .
                . . . . .
                . . . . .""", """
                . . . . .
                . w . . .
                . . . . .
                w . . . .
                . . . . .""", """
                . . . . .
                . . . . .
                w . . . .
                w . . . .
                . . . . .""", """
                . . . . .
                . . . . .
                . . . . .
                w . . . .
                . . . . .""", """
                . . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        runTest("plant", 5, 5, states);
    }

    @Test
    @DisplayName("10x10 2 ticks sparse")
    void tenByTen2TicksSparse() {
        var states = new String[]{"""
                h g . . . . . . . .
                . . . . . . . . . .
                w . . p . . . . . .
                . . . . . . . h . .
                h g . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . p . . .
                . . . . . . . . . .""", """
                h . . . . . . . . .
                h . . . . . . . . .
                w . . . . . . . h .
                . . . p . . . . . .
                h h . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . p . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                h . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        SimConfig.pinguInitialFood = 1;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 2;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 5 ticks sparse")
    void tenByTen5TicksSparse() {
        var states = new String[]{"""
                h g . . . . . . . .
                . . . . . . . . . .
                w . . p . . . . . .
                . . . . . . . h . .
                h g . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . p . . .
                . . . . . . . . . .""", """
                h . . . . . . . . .
                h . . . . . . . . .
                w . . . . . . . h .
                . . . p . . . . . .
                h h . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . p . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                h . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                w w . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                . . . . g g . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . w . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                . . . . g g . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . g g . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        SimConfig.pinguInitialFood = 1;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 2;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 10 ticks sparse")
    void tenByTen10TicksSparse() {
        var states = new String[]{"""
                h g . . . . . . . .
                . . . . . . . . . .
                w . . p . . . . . .
                . . . . . . . h . .
                h g . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . p . . .
                . . . . . . . . . .""", """
                h . . . . . . . . .
                h . . . . . . . . .
                w . . . . . . . h .
                . . . p . . . . . .
                h h . . . . . . . .
                . . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . p . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                h . . . . . . . . .
                w . . . . g . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                w w . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                . . . . g g . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . w . . . . . . .
                . . . . . . . . . .
                w . . . . . . . . .
                . . . . g g . . . .
                w . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                w . . . g g . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . g . . .
                . . . . g g . . . .
                . . . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . g . . .
                . . . . g g . . . .
                . . . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . g . . .
                . . . . g g . . . .
                . . . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . g . . . . . . .
                . . . . . g g . . .
                . . . . g g . g . .
                . g . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . g . . . . . . .
                . . . . . g g . . .
                . . . . g g . g . .
                . g . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . g . . . . . . .
                . . . . . g g . . .
                . . . . g g . g . .
                . g . g . . . . . .
                . . . . . . . . . .
                . . . . . . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        SimConfig.pinguInitialFood = 1;
        SimConfig.pinguFoodConsumption = 1;
        SimConfig.pinguReproductionCost = 2;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 2 ticks dense")
    void tenByTen2TicksDense() {
        var states = new String[]{"""
                h g . . . . g . . .
                . p . g . p . h . .
                w . w w . p . . p .
                . w . p . g . h . .
                h g . . w . p . h .
                . . . g . g . p . .
                w p . . . g . . h .
                . . w . g . . h . .
                . w . p p h . g . h
                . . . . . . . . . .""", """
                h . . p . h p . . .
                h p p g . . . . . .
                w p . w p . h p . .
                . . w p p . . h . .
                w . . . . p p . h .
                w p . g p w . p . .
                . p . p p g . . h .
                . . w . . . . h . .
                . . . . p . h h . h
                . w p . . h . . . .""", """
                . . p . . . p . . .
                . p p p h p . . . .
                w p p w p p . p . .
                p w . p p p . . . .
                w w p . . p p . . .
                . p p . p w . . . .
                w . w p p . p p . .
                p . . p p . . . h .
                . . . p p h . . . .
                . w . p . . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 5 ticks dense")
    void tenByTen5TicksDense() {
        var states = new String[]{"""
                h g . . . . g . . .
                . p . g . p . h . .
                w . w w . p . . p .
                . w . p . g . h . .
                h g . . w . p . h .
                . . . g . g . p . .
                w p . . . g . . h .
                . . w . g . . h . .
                . w . p p h . g . h
                . . . . . . . . . .""", """
                h . . p . h p . . .
                h p p g . . . . . .
                w p . w p . h p . .
                . . w p p . . h . .
                w . . . . p p . h .
                w p . g p w . p . .
                . p . p p g . . h .
                . . w . . . . h . .
                . . . . p . h h . h
                . w p . . h . . . .""", """
                . . p . . . p . . .
                . p p p h p . . . .
                w p p w p p . p . .
                p w . p p p . . . .
                w w p . . p p . . .
                . p p . p w . . . .
                w . w p p . p p . .
                p . . p p . . . h .
                . . . p p h . . . .
                . w . p . . . . . .""", """
                . p p p p p p . . .
                p p p p p p . . . .
                w p p w p p . . p .
                p w w p p p p p . .
                w . p p p p p p . .
                w p p p p . . . . .
                . p . p p . p p . .
                . . p p p p . p . .
                p p . p p . . . . .
                . . . p p p . . . .""", """
                p p p p p p p . . .
                p p p p p p p p . .
                . p p w p p p p p .
                p w w p p p p p p .
                w p p p p p p . p .
                w p p p p p p p . .
                p p p p p p p . . .
                . p p p p p . . p .
                p p p p p p . . p .
                . . p p p p . . . .""", """
                p p p p p p p p . .
                p p p p p p p p p p
                p p p . p p p p p .
                p . w p p p p p p p
                . p p p p p p p . .
                w p p p p p p p p .
                p p p p p p p p . .
                p p p p p p p . . p
                p p p p p p p . . p
                p p p p p p . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 10 ticks dense")
    void tenByTen10TicksDense() {
        var states = new String[]{"""
                h g . . . . g . . .
                . p . g . p . h . .
                w . w w . p . . p .
                . w . p . g . h . .
                h g . . w . p . h .
                . . . g . g . p . .
                w p . . . g . . h .
                . . w . g . . h . .
                . w . p p h . g . h
                . . . . . . . . . .""", """
                h . . p . h p . . .
                h p p g . . . . . .
                w p . w p . h p . .
                . . w p p . . h . .
                w . . . . p p . h .
                w p . g p w . p . .
                . p . p p g . . h .
                . . w . . . . h . .
                . . . . p . h h . h
                . w p . . h . . . .""", """
                . . p . . . p . . .
                . p p p h p . . . .
                w p p w p p . p . .
                p w . p p p . . . .
                w w p . . p p . . .
                . p p . p w . . . .
                w . w p p . p p . .
                p . . p p . . . h .
                . . . p p h . . . .
                . w . p . . . . . .""", """
                . p p p p p p . . .
                p p p p p p . . . .
                w p p w p p . . p .
                p w w p p p p p . .
                w . p p p p p p . .
                w p p p p . . . . .
                . p . p p . p p . .
                . . p p p p . p . .
                p p . p p . . . . .
                . . . p p p . . . .""", """
                p p p p p p p . . .
                p p p p p p p p . .
                . p p w p p p p p .
                p w w p p p p p p .
                w p p p p p p . p .
                w p p p p p p p . .
                p p p p p p p . . .
                . p p p p p . . p .
                p p p p p p . . p .
                . . p p p p . . . .""", """
                p p p p p p p p . .
                p p p p p p p p p p
                p p p . p p p p p .
                p . w p p p p p p p
                . p p p p p p p . .
                w p p p p p p p p .
                p p p p p p p p . .
                p p p p p p p . . p
                p p p p p p p . . p
                p p p p p p . . . .""", """
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p . p p p p p p p
                p p p p p p p p p p
                . p p p p p p p . p
                p p p p p p p p . .
                p p p p p p p p . p
                p p p p p p p p p .
                p p p p p p p . . .""", """
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p . .
                p p p p p p p p . p""", """
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p""", """
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p""", """
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p
                p p p p p p p p p p"""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 10 ticks Die Off")
    void tenByTen10TicksDieOff() {
        var states = new String[]{"""
                h g . . . . g . . .
                . p . g . p . h . .
                w . w w . p . . p .
                . w . p . g . h . .
                h g . . w . p . h .
                . . . g . g . p . .
                w p . . . g . . h .
                . . w . g . . h . .
                . w . p p h . g . h
                . . . . . . . . . .""", """
                h . . p . h p . . .
                h p p g . . . . . .
                w p . w p . h p . .
                . . w p p . . h . .
                w . . . . p p . h .
                w p . g p w . p . .
                . p . p p g . . h .
                . . w . . . . h . .
                . . . . p . h h . h
                . w p . . h . . . .""", """
                . . p . . . p . . .
                . p p p h p . . . .
                w p p w p p . p . .
                p w . p p p . . . .
                w w p . . p p . . .
                . p p . p w . . . .
                w . w p p . p p . .
                p . . p p . . . h .
                . . . p p h . . . .
                . w . p . . . . . .""", """
                . p p p . p p . p .
                p p p p p p . . . .
                w p p w p . p . . .
                p w w p p . p . . .
                w . p p . p p p . .
                w p p . p . p . . .
                . p . p p p p p . .
                . . p p p p . . . .
                . p p p p . . . . .
                . . p p . . . . . .""", """
                p p p p p p p p p .
                p p p p p p p p . .
                . . p w . p . . . .
                p w w . p p p p . .
                w p p p p . p p p .
                . . p p p p p p p .
                w p p p p p p . . .
                p p p p p p p . p .
                p p p . p p . . . .
                . p p p p p . . . .""", """
                p p . p p p . p p .
                p . . . p . p p p .
                p p . . p p p p . .
                p . w p . p p p p p
                . p p p p p p p p .
                p p . p p p p p p p
                w p p . . p p p p .
                p p p . p p p . . p
                p p p p p p p p . .
                p p p p p p p . . .""", """
                p . . . . . p p . .
                p p p p p p p p p p
                p . p p p p p p p .
                p p . p p p p . p p
                p p p p p p . p p p
                p p p p . p p p p p
                . . p p p . . p p p
                p p . p p . p p p p
                p . . p . p p p . .
                p p . . . p p p . .""", """
                p p p p p p p . p .
                . p p p . p p . p p
                p p p p p . p p p p
                p p . p p . . p p p
                p p . p . p p . p p
                p . p p p p . . p p
                p p p p p p p p p .
                . . p p . p p p p .
                . p . p p . p p p p
                p . . . p p p p p .""", """
                p p p . p p p p p p
                p p p p p p . p p p
                . p p p . p . p p p
                . p p . . p . p . .
                p . p p p p p p . p
                p p p p p . p p . .
                p p . p p p p p . p
                . p p p p . p p p p
                p . . p p p p p p p
                . . p p p p p p p p""", """
                p p p p p p p p p p
                p . p . p . p p . .
                p p . . p p p p . p
                p . p p p p p p p .
                p p p . p . p p . .
                . p . p p p p . . p
                p p p p . p p . p .
                p p p . p p p . p p
                p p p . p p . p p p
                . . p p p . . . p p""", """
                . . p p . . p p . p
                p p . p p p p p p p
                p . p p p . p p p .
                p p p p p p . . p p
                . p p p p p p p p .
                p p p p p p . p p p
                p . p . p . . p p p
                p p . p p . . p . p
                p p p . p . . . . .
                p p . p . p . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        SimConfig.pinguInitialFood = 6;
        SimConfig.pinguConsumedFood = 2;
        SimConfig.pinguReproductionCost = 2;
        SimConfig.pinguFoodConsumption = 3;
        runTest("plant", 10, 10, states);
    }

    @Test
    @DisplayName("10x10 10 ticks Aggressive Die Off")
    void tenByTen10TicksAggressiveDieOff() {
        var states = new String[]{"""
                h g . . . . g . . .
                . p . g . p . h . .
                w . w w . p . . p .
                . w . p . g . h . .
                h g . . w . p . h .
                . . . g . g . p . .
                w p . . . g . . h .
                . . w . g . . h . .
                . w . p p h . g . h
                . . . . . . . . . .""", """
                h . . p . h p . . .
                h p p g . . . . . .
                w p . w p . h p . .
                . . w p p . . h . .
                w . . . . p p . h .
                w p . g p w . p . .
                . p . p p g . . h .
                . . w . . . . h . .
                . . . . p . h h . h
                . w p . . h . . . .""", """
                . . p . . . p . . .
                p . p p h . . . . .
                w p p w p p . . . .
                p w w . . p . . . .
                w . . p p . p . . .
                w . p . p w . . . .
                . . . p . . . . . .
                . . . p p h h . . .
                . . w . . . . . . .
                . w . . . . . . . .""", """
                p . . . . . . . . .
                . p . p p p p . . .
                w . p w . p . . . .
                p w . . w p . . . .
                w p . p p . . . . .
                . . . p . . . . . .
                . . . . . p . . . .
                . . . . . . . . . .
                . . . . p . . . . .
                . . . . . . . . . .""", """
                . . . . p . p . . .
                . . p . p . . p . .
                . p . w p . . . . .
                . . w . . . . . . .
                w p p . . w . . . .
                . . . p . . p . . .
                . . . . p . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . p . . . p . . . .
                . . . p . . . . . .
                . p . . p p . . . .
                . . . p . . . . . .
                . . p . . . . . . .
                . w . . p w . . . .
                . . . . . . . . . .
                . . p . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . p . . p . . . .
                p . . . p p . . . .
                . . . . . . . . . .
                . . . p . . p . . .
                . . . p . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                p . . . . . . . . .
                . . p . . . . . . .
                . . . . . . . . . .""", """
                . p . . . . . . . .
                . . . . . . . . . .
                p . . p . p p . . .
                . . p . p . p . . .
                . . . . . . . . . .
                p . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                p . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . p . . . .
                . p p p . . . . . .
                . . . p . p . . . .
                . . . . . . . . . .
                . . p . p . . . . .
                p . . . . . . . . .
                . p . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . p . . . .
                . . p p . . p . . .
                . . p . p . . . . .
                . . . . . . . . . .
                p p . p . . . . . .
                . . . . . . . . . .
                . p . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .""", """
                . . . . . . . . . .
                . p . . . . . . . .
                . . . p p . p p . .
                p . . . p . . . . .
                p . . . . . . . . .
                . . . . p . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . .
                . . . . . . . . . ."""};
        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 2;
        SimConfig.plantReproductionCost = 3;
        SimConfig.hamsterInitialFood = 1;
        SimConfig.hamsterConsumedFood = 2;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        SimConfig.wolfInitialFood = 2;
        SimConfig.wolfConsumedFood = 3;
        SimConfig.wolfFoodConsumption = 1;
        SimConfig.wolfReproductionCost = 3;
        SimConfig.pinguInitialFood = 6;
        SimConfig.pinguConsumedFood = 2;
        SimConfig.pinguReproductionCost = 2;
        SimConfig.pinguFoodConsumption = 5;
        runTest("plant", 10, 10, states);
    }
}
