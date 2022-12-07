package pgdp.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinishPostTest extends FlagPostTest {
    @Override
    SignalPost createSignalPost(int level) {
        return new FinishPost(level);
    }

    @Test
    @Override
    void shouldChangeCorrectlyOnEnd() {
        sut.up("end");
        expectDepiction("chequered");
        expectLevel(5);
    }

    @Override
    @Test
    void correctToString() {
        final var depiction = "red";
        sut.up(depiction);
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵finish⎵post⎵is⎵in⎵level⎵4⎵and⎵is⎵⎵waving⎵⎵%s".replace("⎵", " "), sut.getPostNumber(), depiction);
        assertEquals(expected, sut.toString());
    }

    @Override
    @Test
    void correctToStringForLevelZero() {
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵finish⎵post⎵is⎵in⎵level⎵0⎵and⎵is⎵⎵doing⎵nothing".replace("⎵", " "), sut.getPostNumber());
        assertEquals(expected, sut.toString());
    }
}
