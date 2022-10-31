package pgdp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgdp.warmup.PenguWarmup;

public class UnitTests {
    private final PrintStream originalOut = System.out;

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void checkPenguInfoOut() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();

        // Abort for negative numbers
        System.setOut(new PrintStream(outContent));
        PenguWarmup.penguInfoOut(-1);
        Assertions.assertEquals("Penguin -1 is not a known penguin!\n", outContent.toString());

        // Edge Case: 0, from https://zulip.in.tum.de/#narrow/stream/1350-PGdP-W02H01/topic/Null.20Penguin.3F/near/748272.
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PenguWarmup.penguInfoOut(0);
        Assertions.assertEquals("Penguin: 0\nThis penguin is a male.\n", outContent.toString());

        // 1
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PenguWarmup.penguInfoOut(1);
        Assertions.assertEquals("Penguin: 1\nThis penguin is a female.\n", outContent.toString());

        // 2
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        PenguWarmup.penguInfoOut(2);
        Assertions.assertEquals("Penguin: 2\nThis penguin is a male.\n", outContent.toString());
    }

    @Test
    public void checkPenguEvolutions() {
        Assertions.assertEquals(4, PenguWarmup.penguEvolution(128, 2));
        Assertions.assertEquals(7, PenguWarmup.penguEvolution(9, 9));
        Assertions.assertEquals(22, PenguWarmup.penguEvolution(9, 10));
    }

    @Test
    public void checkPenguSum() {
        Assertions.assertEquals(11, PenguWarmup.penguSum(128));
        Assertions.assertEquals(14, PenguWarmup.penguSum(1337));
        Assertions.assertEquals(21, PenguWarmup.penguSum(54354));
        Assertions.assertEquals(1, PenguWarmup.penguSum(1000));
        Assertions.assertEquals(0, PenguWarmup.penguSum(0));
        Assertions.assertEquals(9, PenguWarmup.penguSum(9));
        Assertions.assertEquals(8, PenguWarmup.penguSum(4004));
        Assertions.assertEquals(28, PenguWarmup.penguSum(1234567));
    }

    @Test
    public void checkPenguPermutation() {
        Assertions.assertEquals(120, PenguWarmup.penguPermutation(6, 3));
        Assertions.assertEquals(420, PenguWarmup.penguPermutation(21, 19));
        Assertions.assertEquals(720, PenguWarmup.penguPermutation(10, 7));
        Assertions.assertEquals(1, PenguWarmup.penguPermutation(42, 42));
        Assertions.assertEquals(500000000000001L, PenguWarmup.penguPermutation(500000000000001L, 500000000000000L));
    }

    @Test
    public void checkPenguPowers() {
        Assertions.assertEquals(1787569, PenguWarmup.penguPowers(1337, 2));
        Assertions.assertEquals(1787569, PenguWarmup.penguPowers(-1337, 2));
        Assertions.assertEquals(-512, PenguWarmup.penguPowers(-8, 3));
        Assertions.assertEquals(81, PenguWarmup.penguPowers(3, 4));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(3, 0));
        Assertions.assertEquals(9, PenguWarmup.penguPowers(9, 1));
        Assertions.assertEquals(81, PenguWarmup.penguPowers(-9, 2));
        Assertions.assertEquals(-27, PenguWarmup.penguPowers(-3, 3));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(1, -1));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(1, -42));
        Assertions.assertEquals(0, PenguWarmup.penguPowers(0, 1));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(0, 0));
        Assertions.assertEquals(Math.pow(Integer.MAX_VALUE, 2), PenguWarmup.penguPowers(Integer.MAX_VALUE, 2));
    }

}
