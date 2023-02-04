package pgdp.minijvm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BonusByteToJavaTest {

    private final Fragment fragment;

    public BonusByteToJavaTest() {
        fragment = new Fragment();
    }

    @ParameterizedTest(name = "Input: [{0}, {1}]")
    @DisplayName("Compare to Byte code")
    @CsvSource({
            "12345, 54321, 22066",
            "12345, 54321, 22066",
            "1234, -4321, 14",
            "-6789, -4321, -60",
            "8, 37, 1",
            "38, 3, 1",
            "9, 7, 1",
            "-8, -16, 1",
            "-5, 10, 1",
    })
    public void compare(int a, int b, int o) {
        String input = a + "\n" + b + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        fragment.fragment();
        assertEquals(o, Integer.parseInt(out.toString().replace("\r", "").split("\n")[0]));
    }

}
