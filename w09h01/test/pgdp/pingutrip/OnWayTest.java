package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class OnWayTest {
    @Test
    void testIsOnWayTest() {
        Stream<OneWay> oneWays = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)));
        WayPoint visit = new WayPoint(0.5, 0.0);

        boolean isOnWay = PinguTrip.onTheWay(oneWays, visit);

        assertTrue(isOnWay);
    }

    @Test
    void testIsNotOnWayTest() {
        Stream<OneWay> oneWays = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)));
        WayPoint visit = new WayPoint(1.5, 0.0);

        boolean isOnWay = PinguTrip.onTheWay(oneWays, visit);

        assertFalse(isOnWay);
    }

    @Test
    void testIsOnWayLargeStream() {
        Stream<OneWay> oneWays = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(2.0, 0.0), new WayPoint(3.0, 0.0)));
        WayPoint visit = new WayPoint(1.5, 0.0);

        boolean isOnWay = PinguTrip.onTheWay(oneWays, visit);

        assertTrue(isOnWay);
    }
}
