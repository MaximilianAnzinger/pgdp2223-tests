package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class ReadWayPointsTest {
    final String path = "test_paths/path.txt";

    @Test
    void testReturnsValuesFromFile() {
        Stream<WayPoint> points = PinguTrip.readWayPoints(path);

        WayPoint[] expected = { new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2) };
        WayPoint[] actual = points.toArray(WayPoint[]::new);

        assertEquals(expected.length, actual.length,
                "The stream should contain exactly " + expected.length + " values");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], "The value at index " + i + " is wrong");
        }
    }
    
    /* Comments:
    Mac OS may throw IOException lazyly if the directory does not exist.
    So if you have dealt with IOException on Mac, you still might encounter it.
    However, this JUnit test will not be executed in Artemis.
    So if you are sure that you have dealt with IOException, you can ignore this test.
    (for MacOS) By: BSLK11
    */
    @Test
    void testReturnsEmptyStreamOnError() {
        Stream<WayPoint> points = PinguTrip.readWayPoints("");
        assertEquals(0, points.count(), "The stream should be empty");
    }
}
