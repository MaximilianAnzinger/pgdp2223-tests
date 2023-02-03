package pgdp.pvm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class OrderTest {
    public static String explanation = """
            !!! THIS TEST IS CORRECT !!!

            The order of operations is different in W13H03
            when comparing with H01, H02 or Praenzaufgaben

            const 5
            const 10
            sub

            expects 5, got -5

            Read More:

            https://zulip.in.tum.de/#narrow/stream/1533-PGdP-W13H03/topic/.E2.9C.94.20Umgekehrte.20Instruction.20Input.20Reihenfolge
            https://github.com/MaximilianAnzinger/pgdp2223-tests/pull/304#discussion_r1093906021
            https://artemis.in.tum.de/api/files/markdown/Markdown_2023-01-27T11-59-55-438_40b7a494.pdf

            """;

    @Test
    public void sub() throws IOException {
        new PVMParser(Files
                .readAllLines(Path.of("minijvm/sub.jvm")).stream())
                .run(IO.of(() -> 0,
                        (i) -> {
                            switch (i) {
                                case 5:
                                    break;

                                case -5:
                                    System.err.println(explanation);
                                    fail(explanation);
                                    break;

                                default:
                                    fail("Unexpected behaviour! Expected 5, Got: " + i);
                                    break;
                            }
                        }));
    }
}
