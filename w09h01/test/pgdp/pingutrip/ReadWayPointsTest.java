package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
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

        assertArrayEquals(expected, actual, "The stream should contain the values from the file");
    }

    @Test
    void testReturnsEmptyStreamOnError() {
        Stream<WayPoint> points = PinguTrip.readWayPoints("not-existing-path");
        assertEquals(0, points.count(), "The stream should be empty");
    }
}
