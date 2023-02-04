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

public class cryptionTest {
    private final pgdp.pvm.PVMParser parser;

    public cryptionTest() {
        try {
            String[] swaps = new String[] {"sub", "div", "mod", "less", "leq"};
            Stream<String> lines = Files.lines(Path.of("src/pgdp/krypto/cryption.jvm")).flatMap(
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
                {1, 1, 0},
                {5, 14, 7, 0},
                {5, 28, 7, 32, 0},
                {19, 28, 2, 0},
        };

        Integer[][] outputs = new Integer[][] {
                {},
                {7},
                {7, 16},
                {16}
        };
        Arguments[] args = new Arguments[inputs.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = Arguments.of(inputs[i], outputs[i]);
        }
        return Arrays.stream(args);
    }

    @ParameterizedTest(name = "Input: {0}")
    @DisplayName("Compare to evaluated values")
    @MethodSource("inputs")
    public void compare(int[] input, Integer[] output) {
        Iterator<Integer> iterator = Arrays.stream(input).iterator();

        List<Integer> outputs = new ArrayList<>();

        parser.run(pgdp.pvm.IO.of(iterator::next, outputs::add));
        assertEquals(Arrays.asList(output), outputs);
    }
}

