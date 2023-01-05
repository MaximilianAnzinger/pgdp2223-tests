package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FurthestAwayFromHomeTest {
    @Test
    @DisplayName("The Artemis tests")
    void testExample() {
        Stream<WayPoint> path = Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        
        WayPoint actual = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(2.0, 0.0);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("With to identical biggest element")
    void testWithIdenticalBiggestElement() {
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.0), new WayPoint(3.0, 4.1), new WayPoint(1.0, 0.0),
                new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);

        WayPoint actual = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(3.0, 4.1);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Stream with no elements should return home")
    void testWithEmptyStream() {
        Stream<WayPoint> path = Stream.of();
        WayPoint home = new WayPoint(0.0, 0.0);

        WayPoint actual = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(0.0, 0.0);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Home that is not a 0.0")
    void testHomeIsZero() {
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.1), new WayPoint(4.1, 3.0), new WayPoint(1.0, 0.0),
                new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(4.1, 3.0);

        WayPoint actual = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(1.0, 0.0);

        assertEquals(expected, actual);
    }
}
