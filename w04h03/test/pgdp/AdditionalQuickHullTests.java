package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static pgdp.convexhull.QuickHull.quickHull;


public class AdditionalQuickHullTests {

    public static final Random rand = new Random(69420);

    @Test
    @DisplayName("Quick hull two points")
    public void quickHullTwoPoints() {
        int[][] in = new int[][] {{Integer.MAX_VALUE, Integer.MIN_VALUE}, {Integer.MIN_VALUE, Integer.MAX_VALUE}};
        int[][] expected = new int[][] {{Integer.MIN_VALUE, Integer.MAX_VALUE}, {Integer.MAX_VALUE, Integer.MIN_VALUE}, {Integer.MIN_VALUE, Integer.MAX_VALUE}};
        int[][] actual = quickHull(in);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("QH Big input Artemis test")
    public void quickHullBigInputArtemis() {
        int[][] in = new int[10000][];
        for(int i = 0; i < 100; i++) {
            for(int j = 0; j < 100; j++) {
                in[i * 100 + j] = new int[] {i, j};
            }
        }

        int[][] actual = quickHull(in);
        int[][] expected = new int[][]{{0, 0}, {99, 0}, {99, 99}, {0, 99}, {0, 0}};
        for (int i = 0; i < expected.length; i++)
            assertArrayEquals(expected[i], actual[i], "Incorrect result for big input artemis example (in 100x100 grid shape)");
    }

    @Test
    @DisplayName("Quick hull same Y coordinate")
    public void quickHullSameYCoordinate() {
        int[][] points = new int[][] {
                {-999, 1}, {-1, 1}, {69, 1}, {Integer.MAX_VALUE, 1}
        };

        int[][] expected = new int[][] {{-999, 1}, {Integer.MAX_VALUE, 1}, {-999, 1}};
        int[][] actual = quickHull(points);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Quick hull diagonal")
    public void quickHullDiagonal() {
        int[][] in = new int[100][];
        for(int i = -30; i < 70; i++) {
            in[i + 30] = new int[] {i, i};
        }

        int[][] expected = new int[][] {{-30, -30}, {69, 69}, {-30, -30}};
        int[][] actual = quickHull(in);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }

    @Test
    @DisplayName("Quick hull Square Function")
    public void quickHullSquareFunction() {
        int[][] in = new int[300][];
        int[][] expected = new int[102][];

        for(int i = 0; i < 100; i++) {
            in[i] = new int[] {i, i * i};
            expected[i] = in[i];
        }

        for(int i = 100; i < 299; i++) {
            int x = (int) ((i - 100.0) / 198.0 * 95) + 1;
            System.out.println(x);
            in[i] = new int[]{x, rand.nextInt(x * x, 9801)};
        }

        in[299] = new int[] {0, 9801};
        expected[100] = new int[] {0, 9801};
        expected[101] = new int[] {0, 0};

        List<int[]> l = Arrays.asList(in);
        Collections.shuffle(l, rand);
        in = l.stream().toArray(int[][]::new);

        int[][] actual = quickHull(in);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i], "Points in hull are not correct!");
        }
    }
}
