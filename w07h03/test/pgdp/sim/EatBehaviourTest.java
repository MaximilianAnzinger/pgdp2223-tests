package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EatBehaviourTest {
    static Pingu staticPingu = new Pingu();
    static Plant plant1 = new Plant();
    static Plant plant2 = new Plant();
    static Hamster staticHamster = new Hamster();
    static Wolf wolf = new Wolf();

    static Cell[] theWorld = new Cell[]{
            null, staticPingu, plant1,
            staticHamster, null, wolf,
            null, plant2, null,
    };

    static int width = 3, height = 3;

    @Test
    void herbivoreBehaviour() {
        for (var consumer : new MovingCell[]{new Hamster(), new Pingu()}) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell[] worldclone = theWorld.clone();
                    Cell[] newWorld = theWorld.clone();
                    var expected = herbivoreWorlds[x][y].clone();

                    newWorld[x + width * y] = consumer;
                    worldclone[x + width * y] = consumer;
                    expected[x + width * y] = consumer;


                    consumer.eat(worldclone, newWorld, width, height, x, y);
                    String errorMsg = "\n-------Failure-------\n"
                            + "Eating on coordinates: \n"
                            + "  x: " + x + "\n"
                            + "  y: " + y + "\n\n"
                            + "Eating party was a:\n"
                            + consumer.getSymbol() + "\n\n"
                            + "Expected world was:\n"
                            + FieldVisualizeHelper.cellArrayToFieldDescription(expected, width, height, false) + "\n"
                            + "Got world:\n"
                            + FieldVisualizeHelper.cellArrayToFieldDescription(newWorld, width, height, false) + "\n\n";

                    Assertions.assertArrayEquals(expected, newWorld, errorMsg);
                }
            }
        }

    }


    /**
     * Depending on which coordinate (first two array indecies) you place the herbivore, this world results
     */
    static Cell[][][] herbivoreWorlds = new Cell[][][]{
            // [0]
            {
                    // [0][0]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, plant2, null,
                    },
                    // [0][1]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, null, null,
                    },
                    // [0][2]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, null, null,
                    },
            },
            // [1]
            {
                    // [1][0]
                    {
                            null, staticPingu, null,
                            staticHamster, null, wolf,
                            null, plant2, null,
                    },
                    // [1][1]
                    {
                            null, staticPingu, null,
                            staticHamster, null, wolf,
                            null, null, null,
                    },
                    // [1][2]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, plant2, null,
                    },
            },
            // [2]
            {
                    // [2][0]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, plant2, null,
                    },
                    // [2][1]
                    {
                            null, staticPingu, null,
                            staticHamster, null, wolf,
                            null, null, null,
                    },
                    // [2][2]
                    {
                            null, staticPingu, plant1,
                            staticHamster, null, wolf,
                            null, null, null,
                    },
            },
    };

    @Test
    public void canEatShouldUseInstanceOf() {
        assertTrue(new Pingu().canEat(new FakePlant()));
        assertTrue(new Hamster().canEat(new FakePlant()));
        assertTrue(new Wolf().canEat(new FakeHamster()));
    }

    private static class FakePlant extends Plant {
        @Override
        public CellSymbol getSymbol() {
            return CellSymbol.WOLF;
        }
    }

    private static class FakeHamster extends Hamster {
        @Override
        public CellSymbol getSymbol() {
            return CellSymbol.WOLF;
        }
    }
}
