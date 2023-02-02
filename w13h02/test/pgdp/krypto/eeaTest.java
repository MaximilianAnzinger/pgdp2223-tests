package pgdp.krypto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.pvm.PVMParser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class eeaTest {
    private final pgdp.pvm.PVMParser parser;

    public eeaTest() {
        try {
            String[] swaps = new String[] {"sub", "div", "mod", "less", "leq"};
            Stream<String> lines = Files.lines(Path.of("src/pgdp/krypto/eea.jvm")).flatMap(
                    instruction -> {
                        // Adding swap in front of commands listed in swaps

                        String temp = instruction;
                        if (temp.contains("//")) temp = temp.split("//")[0];

                        for (String swap : swaps) {
                            if (temp.toLowerCase().contains(swap)) {
                                // Instruction found
                                return Stream.of("swap", instruction);
                            }
                        }
                        return Stream.of(instruction);
                    }
            );
            parser = new pgdp.pvm.PVMParser(lines);
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
                {23482, 20687}
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

    private List<Object> run(int[] input) {
        ArrayList<Object> output = new ArrayList<>();
        int rPrior, r, sPrior, s, tPrior, t, q, tmp;

        rPrior = input[0]; // this is the input e
        r = input[1]; // this is the input /\(N)
        sPrior = 1;
        s = 0;
        tPrior = 0;
        t = 1;

        while (r != 0) {
            q = rPrior / r;
            tmp = r;

            r = rPrior - q * r;
            rPrior = tmp;
            tmp = s;

            s = sPrior - q * s;
            sPrior = tmp;
            tmp = t;

            t = tPrior - q * t;
            tPrior = tmp;
        }

        output.add(sPrior); // this is the wanted d
        return output;
    }
}

