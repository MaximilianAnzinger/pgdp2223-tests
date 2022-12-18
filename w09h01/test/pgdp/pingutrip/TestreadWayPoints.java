package pgdp.pingutrip;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pgdp.pingutrip.PinguTrip.readWayPoints;

public class TestreadWayPoints {
    @Test
    @DisplayName("No end (---)")
    void test1() {
        Stream<WayPoint> wayPointStream = readWayPoints("test/pgdp/pingutrip/path1.txt");

        WayPoint[] expected = {new WayPoint(9.11, 1.1), new WayPoint(9.11, 1.1)};
        WayPoint[] actual = wayPointStream.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }

    }
    @Test
    @DisplayName("Comment and end with no WayPoints")
    void test2() {
        Stream<WayPoint> wayPointStream = readWayPoints("test/pgdp/pingutrip/path2.txt");
        WayPoint[] expected = new WayPoint[0];
        WayPoint[] actual = wayPointStream.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }
    }

    @Test
    @DisplayName("End in the beginning")
    void test3() {
        Stream<WayPoint> wayPointStream = readWayPoints("test/pgdp/pingutrip/path3.txt");
        WayPoint[] expected = new WayPoint[0];
        WayPoint[] actual = wayPointStream.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }
    }

    @Test
    @DisplayName("No comments")
    void test4() {
        Stream<WayPoint> wayPointStream = readWayPoints("test/pgdp/pingutrip/path4.txt");
        WayPoint[] expected = {new WayPoint(9.11, 1.1), new WayPoint(9.11, 1.1)};
        WayPoint[] actual = wayPointStream.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }
    }

    @Test
    @DisplayName("Only comments with no WayPoints or end")
    void test5() {
        Stream<WayPoint> wayPointStream = readWayPoints("test/pgdp/pingutrip/path3.txt");
        WayPoint[] expected = new WayPoint[0];
        WayPoint[] actual = wayPointStream.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length);

        for(int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }
    }
}
