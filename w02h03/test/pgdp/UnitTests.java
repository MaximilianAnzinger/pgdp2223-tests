package pgdp;

import org.junit.jupiter.api.*;
import pgdp.math.PinguSqrt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class UnitTests {

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static final PrintStream stdout = System.out;

    @BeforeAll
    public static void prep() {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void clean() {
        System.setOut(stdout);
    }

    @AfterEach
    public void resetBuffer() {
        out.reset();
    }

    @Test
    void checkSqrt() {
        Random rnd = new Random();

        for (int i = 0; i < 10000; i++) {
            double testNumber = rnd.nextDouble() * Math.pow(10, rnd.nextInt(9));

            PinguSqrt.sqrt(testNumber);
            int resultBegin = out.toString().lastIndexOf(" ");
            String actual = out.toString().substring(resultBegin + 1, out.toString().length() - 1);

            Assertions.assertEquals(Math.sqrt(testNumber), Double.parseDouble(actual), 0.01);
        }
    }

    @Test
    void checkOutput() {
        double testNumber = 1049.76;

        PinguSqrt.sqrt(testNumber);

        String actual = out.toString();
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
        
        String[] actualParts = actual.replace("\r", "").split("\n\n");
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
