package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EatBehaviourTest {
    static Pingu pingu = new Pingu();
    static Plant plant1 = new Plant();
    static Plant plant2 = new Plant();
    static Hamster ham = new Hamster();
    static Wolf wolf = new Wolf();

    static Cell[] theWorld = new Cell[]{
            null, pingu, plant1,
            ham, null, wolf,
            null, plant2, null,
    };

    static int width = 3, height = 3;

    @Test
    void herbivoreBehaviour() {
        for(var consumer : new MovingCell[]{ham, pingu}) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Cell[] newWorld = theWorld.clone();
                    consumer.eat(theWorld.clone(), newWorld, width, height, x,y);
                    String errormsg = "\n------Failure------\n"
                            + "Eating on coordinates: \n"
                            + "  x: " + x + "\n"
                            + "  y: " + y + "\n"
                            + "Eating party was a:\n"
                            + consumer.getSymbol() + "\n"
                            + "Expected world was: " + Arrays.toString(herbivoreWorlds[x][y]) + "\n\n"
                            + "Got world: " + Arrays.toString(newWorld) + "\n\n";
                    Assertions.assertArrayEquals(herbivoreWorlds[x][y], newWorld, errormsg);
                }
            }
        }

    }


    /**
     * Depending on which coordinate (first two array indecies) you place the herbivore, this world results
     */
    static Cell[][][] herbivoreWorlds = new Cell[][][] {
            // [0]
            {
                    // [0][0]
                    {
                            null, pingu, plant1,
                            ham, null, wolf,
                            null, plant2, null,
                    },
                    // [0][1]
                    {
                            null, pingu, plant1,
                            ham, null, wolf,
                            null, null, null,
                    },
                    // [0][2]
                    {
                            null, pingu, plant1,
                            ham, null, wolf,
                            null, null, null,
                    },
            },
            // [1]
            {
                    // [1][0]
                    {
                            null, pingu, null,
                            ham, null, wolf,
                            null, plant2, null,
                    },
                    // [1][1]
                    {
                            null, pingu, null,
                            ham, null, wolf,
                            null, null, null,
                    },
                    // [1][2]
                    {
                            null, pingu, plant1,
                            ham, null, wolf,
                            null, null, null,
                    },
            },
            // [2]
            {
                    // [2][0]
                    {
                            null, pingu, null,
                            ham, null, wolf,
                            null, plant2, null,
                    },
                    // [2][1]
                    {
                            null, pingu, null,
                            ham, null, wolf,
                            null, null, null,
                    },
                    // [2][2]
                    {
                            null, pingu, plant1,
                            ham, null, wolf,
                            null, null, null,
                    },
            },
    };
}
