package pgdp.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LightPanelTest extends TestBase {

    @Test
    @Override
    void shouldChangeStateCorrectlyWhenUpIsCalled() {
        sut.up("green");
        expectLevel(1);
        sut.up("blue");
        expectLevel(1);
        sut.up("yellow");
        expectLevel(2);
        sut.up("doubleYellow");
        expectLevel(3);
        sut.up("[SC]");
        expectLevel(3);
        sut.up("red");
        expectLevel(4);
        sut.up("end");
        expectLevel(5);
    }

    @Test
    @Override
    void depictionShouldBeSetCorrectlyOnUp() {
        sut.up("green");
        assertEquals("green", sut.getDepiction());
        sut.up("blue");
        assertEquals("blue", sut.getDepiction());
        sut.up("yellow");
        assertEquals("yellow", sut.getDepiction());
        sut.up("doubleYellow");
        assertEquals("doubleYellow", sut.getDepiction());
        sut.up("[SC]");
        assertEquals("[SC]", sut.getDepiction());
        sut.up("red");
        assertEquals("red", sut.getDepiction());
        sut.up("end");
        assertEquals("yellow", sut.getDepiction());
    }


    @Test
    @Override
    void correctToStringForLevelZero() {
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵light⎵panel⎵is⎵in⎵level⎵%d⎵and⎵is⎵switched⎵off".replace("⎵", " "), sut.getPostNumber(), sut.getLevel());
        assertEquals(expected, sut.toString());
    }

    @Test
    @Override
    void correctToString() {
        sut.up("green");
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵light⎵panel⎵is⎵in⎵level⎵%d⎵and⎵is⎵blinking⎵%s".replace("⎵", " "), sut.getPostNumber(), sut.getLevel(), sut.getDepiction());
        assertEquals(expected, sut.toString());
    }


    @Test
    void artemisLightPanelPublic() {
        sut.setLevel(4);
        sut.setDepiction("red");
        assertTrue(sut.down("clear"));
        expectLevel(0);
        expectDepiction("");
    }

    @Override
    SignalPost createSignalPost(int level) {
        return new LightPanel(level);
    }
}
