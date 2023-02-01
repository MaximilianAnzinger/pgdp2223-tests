package pgdp.pvm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UnitTest {
    @ParameterizedTest
    @MethodSource
    public void executor(
            String description,
            String file_name,
            List<String> lines,
            String ioCommands,
            boolean shouldThrow)
            throws URISyntaxException, IOException {
        try {
            new PVMParser(lines.stream())
                    .run(getIO(ioCommands));
        } catch (PVMError e) {
            assertTrue(shouldThrow, "execution should throw flag");

            //
            // THIS IS HEAVILY COPIED FROM @FLAMING LEO
            // https://github.com/FlamingLeo/pgdp2223-tests/blob/main/w13h03/test/PinguJVMVerifyTest.java
            //
            if (ioCommands.contains("ERR")) {
                var expectedMessage = ioCommands.substring(ioCommands.indexOf("ERR") + 3).trim();
                String actualMessage = e.getMessage();
                assertTrue(actualMessage.contains(expectedMessage));
            }
            //
            //
            //

            return;
        } catch (RuntimeException e) {
            fail(file_name + "\n[" + description.trim() + "]\nthrew exception:\n" + e);
        }
        assertFalse(shouldThrow, "execution did not throw when it should");
    }

    private static Stream<Arguments> executor() throws IOException {
        var directory = new File("test/scripts");
        var files = Arrays.stream(directory.listFiles());
        var disabled = 0;

        var args = new ArrayList<Arguments>();

        file_loop: for (File file_name : files.toList()) {
            var description = "";
            var specialLines = Files
                    .readAllLines(file_name.toPath())
                    .stream()
                    .filter(i -> i.startsWith("//#"))
                    .toList();
            var executions = new ArrayList<String>();

            for (String line : specialLines) {
                if (line.startsWith("//#DISABLED")) {
                    disabled++;
                    continue file_loop;
                }

                switch (line.substring(0, 4)) {
                    case "//#D":
                        description = line.substring(5);
                        break;

                    case "//#!":
                        executions.add(line.substring(5));
                        break;

                    case "//#M":
                        // MACRO SYSTEM FOR SCALABLE TESTS
                        break;

                    default:
                        throw new RuntimeException(
                                "There was a problem with the tests! " + file_name.getAbsolutePath() + " " + line);
                }
            }

            for (String execution : executions) {
                var lines = Files.readAllLines(file_name.toPath());

                var macros = Arrays.stream(execution.split("\\s+")).filter(i -> i.startsWith("M")).toList();

                for (String macro : macros) {
                    var indexOf = lines.indexOf("//#M");
                    assertNotEquals(-1, "There was an error with the tests: Less macros than specified.");
                    switch (macro) {
                        case "MT":
                            lines.set(indexOf, "TRUE // MACRO GENERATED");
                            break;

                        case "MF":
                            lines.set(indexOf, "FALSE // MACRO GENERATED");
                            break;

                        default:
                            throw new RuntimeException("There was a problem with the tests: Invalid macro.");
                    }
                }

                args.add(arguments(
                        description,
                        file_name.toString(),
                        lines,
                        execution,
                        execution.contains("ERR")));
            }
        }

        if (disabled > 0)
            System.err.println(disabled + " tests have been disabled. ");

        return args.stream();
    }

    private IO getIO(String ioCommands) {
        if (ioCommands.contains("ERR")) {
            // Ignore everything after ERR
            ioCommands = ioCommands.substring(0, ioCommands.indexOf("ERR"));
        }

        var commands = Arrays
                .stream(ioCommands.split("\\s+"))
                .filter(i -> i.length() > 0)
                .filter(i -> !i.startsWith("M"))
                .toList();
        var rwOrder = commands
                .stream()
                .map(i -> i.substring(0, 1).toLowerCase())
                .iterator();
        var intOrder = commands
                .stream()
                .map(i -> Integer.parseInt(i
                        .substring(1)))
                .iterator();

        // System.out.println(commands);

        return IO.of(
                () -> {
                    if (!intOrder.hasNext()) {
                        fail("Received READ call when no more calls were expected");
                    }
                    // If next is true = read
                    if (rwOrder.next().equals("r")) {
                        return intOrder.next();
                    }
                    fail("Received wrong call when WRITE was expected");
                    return 0;
                },
                i -> {
                    if (!intOrder.hasNext()) {
                        fail("Received WRITE call when no more calls were expected");
                    }
                    // if next is false = write
                    if (rwOrder.next().equals("w")) {
                        assertEquals(intOrder.next(), i);
                        return;
                    }
                    fail("Received wrong call when READ was expected");
                });
    }
}
