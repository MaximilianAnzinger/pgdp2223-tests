
import org.junit.jupiter.api.Test;
import pgdp.pvm.IO;
import pgdp.pvm.PVMParser;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTest {

    @Test
    void example() throws URISyntaxException, IOException {
        // example test using IO
        new PVMParser("example.jvm").run(IO.of(
                () -> 2,
                i -> assertEquals(2, i)));
    }

    @Test
    void example2() throws URISyntaxException, IOException {
        // from test dir
        new PVMParser("example2.jvm").run(IO.of(
                () -> 8,
                i -> assertEquals(40, i)));
    }
}
