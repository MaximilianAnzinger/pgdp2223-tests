package pgdp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pgdp.math.PinguSqrt;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class UnitTests {

    private static final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private static final PrintStream stdout = System.out;

    @BeforeAll
    public static void prep()
    {
        System.setOut(new PrintStream(out));
    }

    @AfterAll
    public static void clean()
    {
        System.setOut(stdout);
    }

    @Test
    public void checkSqrt() {
        Random rnd = new Random();

        for(int i = 0; i < 10000; i++) {
            double testNumber = rnd.nextDouble() * Math.pow(10, rnd.nextInt(9));

            PinguSqrt.sqrt(testNumber);
            int resultBegin = out.toString().lastIndexOf(" ");
            String actual = out.toString().substring(resultBegin+1, out.toString().length()-1);

            Assertions.assertEquals(Math.sqrt(testNumber), Double.parseDouble(actual), 0.01);
        }
    }
}
