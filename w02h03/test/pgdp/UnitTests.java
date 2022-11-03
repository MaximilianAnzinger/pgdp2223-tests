package pgdp;

import org.junit.jupiter.api.*;
import pgdp.math.PinguSqrt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.util.Random;

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

    public void assertOutput(double d) {
        PinguSqrt.sqrt(d);

        int resultBegin = out.toString().lastIndexOf(" ");
        String actual = out.toString().substring(resultBegin + 1, out.toString().length() - 1);

        double expected = Math.sqrt(d);

        Assertions.assertEquals(expected, Double.parseDouble(actual), 0.01, "sqrt(" + d + ") = " + expected + " Your Output: " + actual);
    }

    public void assertOutput(double d, String expected) {
        PinguSqrt.sqrt(d);

        int resultBegin = out.toString().lastIndexOf(" ");
        String actual = out.toString().substring(resultBegin + 1, out.toString().length() - 1);

        System.out.println(actual);

        Assertions.assertEquals(expected, actual, "sqrt(" + d + ") = " + expected + " Your Output: " + actual);
    }

    @Test
    void checkSqrt() {
        Random rnd = new Random();

        for (int i = 0; i < 500; i++) {
            double testNumber = rnd.nextDouble() * Math.pow(10, rnd.nextInt(9));
            assertOutput(testNumber);
        }

        assertOutput(5.29, "2.3");
        assertOutput(5341493.3160, "2311.16");
    }

    @Test
    @DisplayName("It should calculate the square roots of example cases")
    void example_sqrt() {
        assertOutput(1049.76, "32.4");
        assertOutput(4.0, "2.0");
    }

    @Test
    @DisplayName("Should calculate natural square roots")
    void natural_sqrt() {

        assertOutput(0.0, "0.0");
        assertOutput(1.0, "1.0");
        assertOutput(4.0, "2.0");
        assertOutput(9.0, "3.0");
        assertOutput(16.0, "4.0");
        assertOutput(25.0, "5.0");
        assertOutput(36.0, "6.0");
        assertOutput(49.0, "7.0");
        assertOutput(64.0, "8.0");
        assertOutput(81.0, "9.0");
        assertOutput(100.0, "10.0");
        assertOutput(2500.0, "50.0");
        assertOutput(360000.0, "600.0");
        assertOutput(49000000.0, "7000.0");
        assertOutput(1000000.0, "1000.0");
    }

    @Test
    @DisplayName("It should calculate the square roots of numbers near zero")
    void comma_sqrt() {
        assertOutput(0.01, "0.1");
        assertOutput(0.04, "0.2");
        assertOutput(0.09, "0.3");
        assertOutput(0.16, "0.4");
        assertOutput(0.25, "0.5");
        assertOutput(0.36, "0.6");
        assertOutput(0.49, "0.7");
        assertOutput(0.64, "0.8");
        assertOutput(0.81, "0.9");
        assertOutput(0.0001, "0.01");
        assertOutput(0.0004, "0.02");
        assertOutput(0.0009, "0.03");
        assertOutput(0.0016, "0.04");
        assertOutput(0.0025, "0.05");
        assertOutput(0.0036, "0.06");
        assertOutput(0.0049, "0.07");
        assertOutput(0.0064, "0.08");
        assertOutput(0.0081, "0.09");
    }

    @Test
    @DisplayName("It should calculate big numbers")
    void big_number() {
        assertOutput(2147483647, "46340.95");
        assertOutput(2147483646.9999, "46340.95");
        assertOutput(2111111111.0001, "45946.82");

    }

    @Test
    void checkOutput() {
        double testNumber = 1049.76;

        PinguSqrt.sqrt(testNumber);

        String actual = out.toString();
        // java always terminates lines with \n (platform/source file independent)
        String expected = """
                Wurzel aus 1049.76

                10
                --------
                -1
                -3
                -5
                --------
                Rest: 1
                neue Ergebnis Ziffer: 3

                149
                --------
                -61
                -63
                --------
                Rest: 25
                neue Ergebnis Ziffer: 2

                2576
                --------
                -641
                -643
                -645
                -647
                --------
                Rest: 0
                neue Ergebnis Ziffer: 4

                Ergebnis: 32.4
                """;
        String expectedZero = """
                0
                --------
                --------
                Rest: 0
                neue Ergebnis Ziffer: 0
                """;

        String[] actualParts = actual.split("\n\n");
        String[] expectedParts = expected.split("\n\n");

        // check header and calculation steps
        for (int i = 0; i < expectedParts.length - 1; i++) {
            Assertions.assertEquals(expectedParts[i], actualParts[i]);
        }

        // check trailing zero calculations
        if (actualParts.length > expectedParts.length) {
            for (int i = actualParts.length - (actualParts.length - expectedParts.length); i < actualParts.length - 1; i++) {
                Assertions.assertEquals(expectedZero, actualParts[i]);
            }
        }

        // check result
        Assertions.assertEquals(expectedParts[expectedParts.length - 1], actualParts[actualParts.length - 1]);
    }

}
