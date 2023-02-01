package pgdp.minijvm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.pvm.PVMParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class JavaToByteTest {
    private pgdp.pvm.PVMParser parser;

    public JavaToByteTest() {
        try {
            parser = new pgdp.pvm.PVMParser(Path.of("src/pgdp/minijvm/lcm.jvm"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static Stream<Arguments> inputs() {
        int[][] inputs = new int[][] {
                {5, 9},
                {8, 37},
                {38, 3},
                {9, 7},
                {-8, -16},
                {-5, 10},
        };
        return Arrays.stream(inputs).map(Arguments::of);
    }

    @ParameterizedTest(name = "Input: {0}")
    @DisplayName("Compare to Java code")
    @MethodSource("inputs")
    public void compare(int[] input) {
        Iterator<Integer> iterator = Arrays.stream(input).iterator();

        List<Integer> outputs = new ArrayList<>();

        parser.run(pgdp.pvm.IO.of(iterator::next, outputs::add));
        assertEquals(run(input), outputs);
    }

    private List<Integer> run(int[] input) {
        ArrayList<Integer> output = new ArrayList<>();
        int a, b, r;
        a = input[0];
        b = input[1];
        if (a <= 0) {
            a = -a;
        }
        if (b <= 0) {
            b = -b;
        }
        r = a * b;
        while (a != b) {
            if (b < a) {
                a = a - b;
            } else {
                b = b - a;
            }
        }

        r = r / a;
        output.add(r);
        return output;
    }


}

