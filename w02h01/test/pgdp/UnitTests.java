package pgdp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.*;
import pgdp.warmup.PenguWarmup;

public class UnitTests {

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream() {
        // returns platform independent captured output
        @Override
        public synchronized String toString() {
            return super.toString().replace("\r", "");
        }
    };

    private static final PrintStream stdOut = System.out;

    @AfterEach
    public void resetBuffer() {
        out.reset();
    }

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(stdOut);
    }

    @Test
    public void checkPenguInfoOutNegative() {
        // Abort for negative numbers
        PenguWarmup.penguInfoOut(-1);
        Assertions.assertEquals("Penguin -1 is not a known penguin!\n", out.toString());
    }

    @Test
    public void checkPenguInfoOutZero() {
        // Edge Case: 0, from https://zulip.in.tum.de/#narrow/stream/1350-PGdP-W02H01/topic/Null.20Penguin.3F/near/748272.
        PenguWarmup.penguInfoOut(0);
        Assertions.assertEquals("Penguin: 0\nThis penguin is a male.\n", out.toString());
    }

    @Test
    public void checkPenguInfoOutFemale() {
        // 1
        PenguWarmup.penguInfoOut(1);
        Assertions.assertEquals("Penguin: 1\nThis penguin is a female.\n", out.toString());
    }

    @Test
    public void checkPenguInfoOutMale() {
        // 2
        PenguWarmup.penguInfoOut(2);
        Assertions.assertEquals("Penguin: 2\nThis penguin is a male.\n", out.toString());
    }

    @Test
    public void checkPenguEvolutions() {
        // Example 1:
        // IN: penguin = 128, years = 2
        // OUT: 4
        // EVO: Year 0: 128
        //      Year 1: 1
        //      Year 2: 4
        Assertions.assertEquals(4, PenguWarmup.penguEvolution(128, 2));

        // Example 2:
        // IN: penguin = 9, years = 9
        // OUT: 7
        // EVO: Year 0: 9
        //      Year 1: 28
        //      Year 2: 14
        //      Year 3: 7
        //      Year 4: 7
        //      Year 5: 7
        //      Year 6: 7
        //      Year 7: 7
        //      Year 8: 7
        //      Year 9: 7
        Assertions.assertEquals(7, PenguWarmup.penguEvolution(9, 9));

        // Example 3:
        // IN: penguin = 9, years = 10
        // OUT: 22
        // EVO: Year  0: 9
        //      Year  1: 28
        //      Year  2: 14
        //      Year  3: 7
        //      Year  4: 7
        //      Year  5: 7
        //      Year  6: 7
        //      Year  7: 7
        //      Year  8: 7
        //      Year  9: 7
        //      Year 10: 22
        Assertions.assertEquals(22, PenguWarmup.penguEvolution(9, 10));

        // Test case with 1.
        // 
        // Example 4:
        // IN: penguin = 1, years = 0
        // OUT: 1
        // EVO: Year  0: 1
        Assertions.assertEquals(1, PenguWarmup.penguEvolution(1, 0));

        // Example 5:
        // IN: penguin = 70, years = 0
        // OUT: 1
        // EVO: Year  0: 70
        //      Year  1: 35
        //      Year  2: 35
        //      Year  3: 35
        //      Year  4: 35
        //      Year  5: 35
        //      Year  6: 35
        //      Year  7: 35
        //      Year  8: 106
        Assertions.assertEquals(106, PenguWarmup.penguEvolution(70, 8));

        // From https://zulip.in.tum.de/#narrow/stream/1350-PGdP-W02H01/topic/Evolution.20der.20Penguine.20.7C.20Startwert.20ein.20Vielfaches.20von.20sieben/near/748278
        //
        // Example 6:
        // IN: penguin = 7, years = 10
        // OUT: 17
        // EVO: Year  0: 7
        //      Year  1: 7
        //      Year  2: 7
        //      Year  3: 7
        //      Year  4: 7
        //      Year  5: 7
        //      Year  6: 7
        //      Year  7: 22
        //      Year  8: 11
        //      Year  9: 34
        //      Year 10: 17
        Assertions.assertEquals(17, PenguWarmup.penguEvolution(7, 10));
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
        Assertions.assertEquals(81, PenguWarmup.penguPowers(3, 4));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(3, 0));
        Assertions.assertEquals(9, PenguWarmup.penguPowers(9, 1));
        Assertions.assertEquals(0, PenguWarmup.penguPowers(0, 1));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(0, 0));
        Assertions.assertEquals(Math.pow(Integer.MAX_VALUE, 2), PenguWarmup.penguPowers(Integer.MAX_VALUE, 2));
    }

    @Test
    @DisplayName("checkPenguPowersNegative() (this is an optional test. your code does not have to pass it)")
    void checkPenguPowersNegative() {
        Assertions.assertEquals(1787569, PenguWarmup.penguPowers(-1337, 2));
        Assertions.assertEquals(-512, PenguWarmup.penguPowers(-8, 3));
        Assertions.assertEquals(81, PenguWarmup.penguPowers(-9, 2));
        Assertions.assertEquals(-27, PenguWarmup.penguPowers(-3, 3));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(1, -1));
        Assertions.assertEquals(1, PenguWarmup.penguPowers(1, -42));
    }

}
