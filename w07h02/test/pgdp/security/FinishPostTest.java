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
        sut.up("red");
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵finish⎵post⎵is⎵in⎵level⎵%d⎵and⎵is⎵⎵waving⎵⎵%s".replace("⎵", " "), sut.getPostNumber(), sut.getLevel(), sut.getDepiction());
        assertEquals(expected, sut.toString());
    }

    @Override
    @Test
    void correctToStringForLevelZero() {
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵finish⎵post⎵is⎵in⎵level⎵%d⎵and⎵is⎵⎵doing⎵nothing".replace("⎵", " "), sut.getPostNumber(), sut.getLevel());
        assertEquals(expected, sut.toString());
    }
}
