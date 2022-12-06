package pgdp.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FlagPostTest extends TestBase {
    @Override
    SignalPost createSignalPost(int level) {
        return new FlagPost(level);
    }

    @Test
    void shouldNotBeAbleToGoFromLevelZeroToGreenBlue() {
        assertFalse(sut.up("green/blue"));
        expectLevel(0);
        expectDepiction("");
    }

    @Test
    void shouldBeAbleToGoToGreenBlueViaGreenAndBlue() {
        sut.up("green");
        expectDepiction("green");
        sut.up("blue");
        expectLevel(1);
        expectDepiction("green/blue");
    }

    @Test
    void shouldBeAbleToGoToGreenBlueViaBlueAndGreen() {
        sut.up("blue");
        expectDepiction("blue");
        sut.up("green");
        expectLevel(1);
        expectDepiction("green/blue");
    }

    @Override
    @Test
    void depictionShouldBeSetCorrectlyOnUp() {


    }

    @Override
    void shouldChangeStateCorrectlyWhenUpIsCalled() {
        sut.up("green");
        expectDepiction("green");
        expectLevel(1);
        sut.up("blue");
        expectDepiction("green/blue");
        expectLevel(1);

        sut.up("yellow");
        expectDepiction("yellow");
        expectLevel(2);

        sut.up("doubleYellow");
        expectDepiction("doubleYellow");
        expectLevel(3);

        sut.up("[SC]");
        expectDepiction("doubleYellow/[SC]");
        expectLevel(3);

        sut.up("red");
        expectDepiction("red");
        expectLevel(4);


    }

    @Test
    void shouldChangeCorrectlyOnEnd() {
        sut.up("end");
        expectDepiction("green/yellow/red/blue");
        expectLevel(5);
    }

    @Test
    void shouldNotBeAbleToGoToDYSCViaDYSC() {
        sut.up("doubleYellow/[SC]");
        expectDepiction("");
        expectLevel(0);
    }

    @Test
    void downShouldGoToBlueWhenCurrentStateIsGreenBlue() {
        sut.up("green");
        sut.up("blue");
        sut.down("green");
        expectDepiction("blue");
        expectLevel(1);
    }

    @Test
    void downShouldGoToLevelZeroWhenCurrentStateIsGreen() {
        sut.up("green");
        sut.down("green");
        expectDepiction("");
        expectLevel(0);
    }

    @Test
    void downShouldGoToGreenWhenCurrentStateIsGreenBlue() {
        sut.up("green");
        sut.up("blue");
        sut.down("blue");
        expectDepiction("green");
        expectLevel(1);
    }

    @Test
    void downShouldGoToLevelZeroWhenCurrentStateIsBlue() {
        sut.up("blue");
        sut.down("blue");
        expectDepiction("");
        expectLevel(0);
    }

    @Override
    @Test
    void correctToStringForLevelZero() {
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵⎵flag⎵post⎵⎵is⎵in⎵level⎵%d⎵and⎵is⎵⎵doing⎵nothing".replace("⎵", " "), sut.getPostNumber(), sut.getLevel());
        assertEquals(expected, sut.toString());
    }

    @Override
    @Test
    void correctToString() {
        sut.up("end");
        final var expected = String.format("Signal⎵post⎵%d⎵of⎵type⎵⎵flag⎵post⎵⎵is⎵in⎵level⎵%d⎵and⎵is⎵⎵waving⎵⎵%s".replace("⎵", " "), sut.getPostNumber(), sut.getLevel(), sut.getDepiction());
        assertEquals(expected, sut.toString());
    }
}
