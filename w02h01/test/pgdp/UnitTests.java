package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgdp.warmup.PenguWarmup;
public class UnitTests {

    @Test
    public void checkPenguEvolutions()
    {
        Assertions.assertEquals(PenguWarmup.penguEvolution(128, 2), 4);
        Assertions.assertEquals(PenguWarmup.penguEvolution(9, 9), 7);
        Assertions.assertEquals(PenguWarmup.penguEvolution(9, 10), 22);
    }

    @Test
    public void checkPenguSum()
    {
        Assertions.assertEquals(PenguWarmup.penguSum(128), 11);
        Assertions.assertEquals(PenguWarmup.penguSum(1337), 14);
        Assertions.assertEquals(PenguWarmup.penguSum(54354), 21);
        Assertions.assertEquals(PenguWarmup.penguSum(1000), 1);
        Assertions.assertEquals(PenguWarmup.penguSum(0), 0);
        Assertions.assertEquals(PenguWarmup.penguSum(9), 9);
        Assertions.assertEquals(PenguWarmup.penguSum(4004), 8);
        Assertions.assertEquals(PenguWarmup.penguSum(1234567), 28);
    }

    @Test
    public void checkPenguPermutation()
    {
        Assertions.assertEquals(PenguWarmup.penguPermutation(6, 3), 120);
        Assertions.assertEquals(PenguWarmup.penguPermutation(21, 19), 420);
        Assertions.assertEquals(PenguWarmup.penguPermutation(10, 7), 720);
        Assertions.assertEquals(PenguWarmup.penguPermutation(42, 42), 1);
    }

    @Test
    public void checkPenguPowers()
    {
        Assertions.assertEquals(PenguWarmup.penguPowers(1337, 2), 1787569);
        Assertions.assertEquals(PenguWarmup.penguPowers(-1337, 2), 1787569);
        Assertions.assertEquals(PenguWarmup.penguPowers(-8, 3), -512);
        Assertions.assertEquals(PenguWarmup.penguPowers(3, 4), 81);
        Assertions.assertEquals(PenguWarmup.penguPowers(3, 0), 1);
        Assertions.assertEquals(PenguWarmup.penguPowers(9, 1), 9);
        Assertions.assertEquals(PenguWarmup.penguPowers(-9, 2), 81);
        Assertions.assertEquals(PenguWarmup.penguPowers(-3, 3), -27);
        Assertions.assertEquals(PenguWarmup.penguPowers(1, -1),1);
    }

}
