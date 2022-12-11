package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RandomGeneratorTest {

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

    void testRandomGenerator(byte[] seed, int times) {
        int len = 20;
        int[] a = new int[len];
        for (int i = 0; i < len; i++)
            a[i] = RandomGenerator.nextInt();
        RandomGenerator.reseed(seed);
        int tmp = times;
        while (tmp-- != 0)
            RandomGenerator.nextInt();
        for (int i = 0; i < len; i++)
            Assertions.assertEquals(a[i], RandomGenerator.nextInt());
        RandomGenerator.reseed(seed);
        tmp = times;
        while (tmp-- != 0)
            RandomGenerator.nextInt();
    }

    @Test
    @DisplayName("whether the time of using RandomGenerator is correct")
    void correctnessUsingRandomGenerator() {
        byte[] seed = {1};
        RandomGenerator.reseed(seed);
        seed = new byte[10];
        for (int i = 0; i < 10; i++)
            seed[i] = (byte) RandomGenerator.nextInt(-128, 127);
        RandomGenerator.reseed(seed);

        Cell[] cells = new Cell[1], nCells = new Cell[1];
        int width = 1, height = 1, times = 0;

        SimConfig.plantMinGrowth = 1;
        SimConfig.plantMaxGrowth = 10;
        SimConfig.plantReproductionCost = 1;
        cells[0] = new Plant();

        cells[0].place(cells, nCells, width, height, 0, 0);
        testRandomGenerator(seed, times);

        cells[0].tick(cells, nCells, width, height, 0, 0);
        times += 1;
        testRandomGenerator(seed, times);

        SimConfig.hamsterInitialFood = 5;
        SimConfig.hamsterConsumedFood = 0;
        SimConfig.hamsterFoodConsumption = 1;
        SimConfig.hamsterReproductionCost = 3;
        cells[0] = new Hamster();

        ((MovingCell)cells[0]).eat(cells, nCells, width, height, 0, 0);
        testRandomGenerator(seed, times);

        ((MovingCell)cells[0]).move(cells, nCells, width, height, 0, 0);
        times += 1;
        testRandomGenerator(seed, times);

        ((MovingCell)cells[0]).tick(cells, nCells, width, height, 0, 0);
        times += 1;
        testRandomGenerator(seed, times);
    }

}
