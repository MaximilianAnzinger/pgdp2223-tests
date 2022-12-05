package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.security.LightPanel;
import pgdp.security.SignalPost;


class LightPanelTest {
    private SignalPost testclass;

    @BeforeEach
    void setUp() {
        testclass = new LightPanel(0);
    }


    @Test
    void up() {
    }


    @Test
    @DisplayName("Level 0")
    void testLevel0() {
        assertEqualColorAndLevel("", 0, testclass);

        Assertions.assertTrue(testclass.up("green"));
        assertEqualColorAndLevel("green", 1, testclass);

        Assertions.assertTrue(testclass.up("blue"));
        assertEqualColorAndLevel("blue", 1, testclass);

        Assertions.assertTrue(testclass.up("yellow"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        Assertions.assertTrue(testclass.up("doubleYellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        Assertions.assertTrue(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 1: Green")
    void testLevel1Green() {
        String color = "green";

        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("blue"));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("yellow"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("doubleYellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 1: Blue")
    void testLevel1Blue() {
        String color = "blue";
        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("yellow"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("doubleYellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 2")
    void testLevel2() {
        String color = "yellow";
        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("yellow"));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("doubleYellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 3: doubleYellow")
    void testLevel3DoubleYellow() {
        String color = "doubleYellow";
        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("yellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("doubleYellow"));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 3: [SC]")
    void testLevel3SC() {
        String color = "[SC]";
        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel(color, 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel(color, 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel(color, 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("yellow"));
        assertEqualColorAndLevel(color, 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("doubleYellow"));
        assertEqualColorAndLevel(color, 3, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("[SC]"));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 4")
    void testLevel4() {
        String color = "red";
        testclass.up(color);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("yellow"));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("doubleYellow"));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("[SC]"));
        assertEqualColorAndLevel(color, 4, testclass);

        testclass.up(color);
        Assertions.assertFalse(testclass.up("red"));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up(color);
        Assertions.assertTrue(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Level 5")
    void testLevel5() {
        String color = "yellow";
        String end = "end";

        testclass.up(end);
        Assertions.assertFalse(testclass.up(""));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("green"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("blue"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("yellow"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("doubleYellow"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("[SC]"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("red"));
        assertEqualColorAndLevel(color, 5, testclass);

        testclass.up(end);
        Assertions.assertFalse(testclass.up("end"));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    private void assertEqualColorAndLevel(String expectedColor, int expectedLevel, SignalPost actual) {
        Assertions.assertEquals(expectedColor, actual.getDepiction());
        Assertions.assertEquals(expectedLevel, actual.getLevel());
        setUp();
    }

    @Test
    @DisplayName("Clear")
    void downClear() {
        Assertions.assertFalse(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("green");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("blue");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("yellow");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("doubleYellow");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("[SC]");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("red");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("end");
        Assertions.assertTrue(testclass.down("clear"));
        assertEqualColorAndLevel("", 0, testclass);
    }

    @Test
    @DisplayName("Green")
    void downGreen() {
        String arg = "green";

        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("green");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("blue");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up("yellow");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up("doubleYellow");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up("[SC]");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up("red");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up("end");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Blue")
    void downBlue() {
        String arg = "blue";

        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("green");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("blue");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("yellow");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("yellow", 2, testclass);

        testclass.up("doubleYellow");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("doubleYellow", 3, testclass);

        testclass.up("[SC]");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("[SC]", 3, testclass);

        testclass.up("red");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("red", 4, testclass);

        testclass.up("end");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }

    @Test
    @DisplayName("Danger")
    void downDanger() {
        String arg = "danger";

        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("", 0, testclass);

        testclass.up("green");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("blue");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("blue", 1, testclass);

        testclass.up("yellow");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("doubleYellow");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("[SC]");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("red");
        Assertions.assertTrue(testclass.down(arg));
        assertEqualColorAndLevel("green", 1, testclass);

        testclass.up("end");
        Assertions.assertFalse(testclass.down(arg));
        assertEqualColorAndLevel("yellow", 5, testclass);
    }


    @Test
    void testToString() {
    }

    @Test
    void getPostNumber() {
        Assertions.assertEquals(0, testclass.getPostNumber());
    }

    @Test
    void setPostNumber() {
        testclass.setPostNumber(1);
        Assertions.assertEquals(1, testclass.getPostNumber());
    }

    @Test
    void getDepiction() {
        Assertions.assertEquals("", testclass.getDepiction());
    }

    @Test
    void setDepiction() {
        testclass.setDepiction("yellow");
        Assertions.assertEquals("yellow", testclass.getDepiction());
    }

    @Test
    void getLevel() {
        Assertions.assertEquals(0, testclass.getLevel());
    }

    @Test
    void setLevel() {
        testclass.setLevel(1);
        Assertions.assertEquals(1, testclass.getLevel());
    }
}