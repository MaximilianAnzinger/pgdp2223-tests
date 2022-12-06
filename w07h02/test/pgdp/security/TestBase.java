package pgdp.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TestBase {
    SignalPost sut;

    abstract SignalPost createSignalPost(int level);

    @BeforeEach
    void setUp() {
        sut = createSignalPost(1);
    }

    void expectLevel(int level) {
        assertEquals(level, sut.getLevel());
    }

    void expectDepiction(String depiction) {
        assertEquals(depiction, sut.getDepiction());
    }

    @Test
    void initialLevelShouldBeZero() {
        expectLevel(0);
    }

    @Test
    void checkTransitivityOnUp() {
        sut.up("yellow");
        sut.up("end");
        expectLevel(5);
    }

    @Test
    void upShouldOnlyBeAbleToGoRight() {
        sut.up("yellow");
        sut.up("green");
        expectLevel(2);
    }

    abstract void depictionShouldBeSetCorrectlyOnUp();

    abstract void shouldChangeStateCorrectlyWhenUpIsCalled();

    @Test
    void upShouldReturnTrueIfStateChanged() {
        assertTrue(sut.up("yellow"));
        assertTrue(sut.up("end"));
    }

    @Test
    void upShouldReturnFalseIfStateDidNotChange() {
        sut.up("red");
        sut.up("green");
        assertFalse(sut.up("green"));
    }

    @Test
    void upShouldReturnFalseOnInvalidInput() {
        assertFalse(sut.up("invalid"));
    }

    @Test
    void upShouldReturnFalseOnEmptyInput() {
        assertFalse(sut.up(""));
    }

    @Test
    void upShouldNotChangeStateOnInvalidInput() {
        sut.up("yellow");
        sut.up("invalid");
        expectLevel(2);
        assertEquals("yellow", sut.getDepiction());
    }

    @Test
    void downShouldReturnFalseIfStateDidNotChange() {
        sut.down("red");
        sut.down("green");
        assertFalse(sut.down("green"));
    }

    @Test
    void downShouldReturnTrueIfStateChanged() {
        sut.up("yellow");
        assertTrue(sut.down("clear"));
    }

    @Test
    void downShouldHandleClear() {
        assertTrue(sut.up("green"));
        assertTrue(sut.up("end"));
        assertTrue(sut.down("clear"));
        expectLevel(0);
        assertEquals("", sut.getDepiction());
    }

    @Test
    void downShouldReturnFalseOnClearIfStateDidNotChange() {
        assertFalse(sut.down("clear"));
    }

    @Test
    void downShouldReturnTrueOnClearIfStateChanged() {
        sut.up("green");
        assertTrue(sut.down("clear"));
    }

    @Test
    void onGreenDownShouldSwitchToInitialStateWhenStateIsGreen() {
        sut.up("green");
        assertTrue(sut.down("green"));
        expectLevel(0);
        assertEquals("", sut.getDepiction());
    }

    @Test
    void onGreenDownShouldNotSwitchToInitialStateWhenStateIsNotGreen() {
        sut.up("red");
        assertFalse(sut.down("green"));
        expectLevel(4);
        assertEquals("red", sut.getDepiction());
    }

    @Test
    void onBlueDownShouldSwitchToInitialStateWhenStateIsBlue() {
        sut.up("blue");
        assertTrue(sut.down("blue"));
        expectLevel(0);
        assertEquals("", sut.getDepiction());
    }

    @Test
    void onBlueDownShouldNotSwitchToInitialStateWhenStateIsNotBlue() {
        sut.up("red");
        assertFalse(sut.down("blue"));
        expectLevel(4);
        assertEquals("red", sut.getDepiction());
    }

    void downDangerAssert(String from) {
        assertTrue(sut.up(from));
        assertTrue(sut.down("danger"));
        expectLevel(1);
        assertEquals("green", sut.getDepiction());
    }

    @Test
    void dangerShouldSwitchCorrectly() {
        downDangerAssert("yellow");
        downDangerAssert("doubleYellow");
        downDangerAssert("[SC]");
        downDangerAssert("red");
    }

    @Test
    void dangerShouldNotChangeStateOnInvalidState() {
        sut.up("blue");
        sut.down("danger");
        expectLevel(1);
        assertEquals("blue", sut.getDepiction());
    }

    @Test
    void dangerShouldNotChangeStateOnEndYellow() {
        sut.up("end");
        sut.down("danger");
        expectLevel(5);
    }

    @Test
    void shouldNotBeAbleToGoUpFromEnd() {
        sut.up("end");
        assertFalse(sut.up("yellow"));
        expectLevel(5);
    }

    @Test
    void settersShouldNotCorruptState() {
        sut.up("yellow");
        sut.setLevel(4);
        sut.setDepiction("red");
        assertFalse(sut.up("doubleYellow"));
    }

    abstract void correctToStringForLevelZero();

    abstract void correctToString();

}
