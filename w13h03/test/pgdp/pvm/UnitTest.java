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
import static org.junit.jupiter.api.Assertions.fail;

public class UnitTest {
    @ParameterizedTest
    @MethodSource
    public void executor(String description, List<String> lines, String ioCommands)
            throws URISyntaxException, IOException {
        new PVMParser(lines.stream()).run(getIO(ioCommands));
    }

    private static Stream<Arguments> executor() throws IOException {
        var directory = new File("test/pgdp/pvm/scripts");
        var files = Arrays.stream(directory.listFiles());

        var args = new ArrayList<Arguments>();

        for (File file_name : files.toList()) {
            var description = "";
            var lines = Files.readAllLines(file_name.toPath()).stream().filter(i -> i.startsWith("//#")).toList();

            for (String line : lines) {
                switch (line.substring(0, 4)) {
                    case "//#D":
                        description = line.substring(4);
                        break;

                    case "//#!":
                        args.add(arguments(description, Files.readAllLines(file_name.toPath()), line.substring(4)));
                        break;

                    default:
                        throw new RuntimeException(
                                "There was a problem with the tests! " + file_name.getAbsolutePath() + " " + line);
                }
            }
        }

        return args.stream();
    }

    private IO getIO(String ioCommands) {
        var commands = Arrays.stream(ioCommands.split("\\s+")).filter(i -> i.length() > 0).toList();
        var rwOrder = commands.stream().map(i -> i.substring(0, 1).equals("R")).iterator();
        var intOrder = commands.stream().map(i -> Integer.parseInt(i.substring(1))).iterator();

        return IO.of(
                () -> {
                    if (!intOrder.hasNext()) {
                        fail("Received READ call when no more calls were expected");
                    }
                    // If next is true = read
                    if (rwOrder.next()) {
                        return intOrder.next();
                    }
                    fail("Received READ call when WRITE was expected");
                    return 0;
                },
                i -> {
                    if (!intOrder.hasNext()) {
                        fail("Received WRITE call when no more calls were expected");
                    }
                    // if next is false = write
                    if (rwOrder.next()) {
                        fail("Received WRITE call when READ was expected");
                    }
                    assertEquals(intOrder.next(), i);
                });
    }
}
