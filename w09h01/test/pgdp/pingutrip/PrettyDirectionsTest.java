package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class PrettyDirectionsTest {
    @Test
    void testIsOnWayTest() {
        Stream<OneWay> ways = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)),
                new OneWay(new WayPoint(3.0, 0.0), new WayPoint(4.0, 1.0)));

        String output = PinguTrip.prettyDirections(ways);

        assertEquals("""
                1 Schritte Richtung 0 Grad.
                3 Schritte Richtung 0 Grad.
                2 Schritte Richtung 45 Grad.""".replace("\n", System.lineSeparator()), output);
    }

    @Test
    void testIsOnWayTestEmpty() {
        Stream<OneWay> ways = Stream.of();

        String output = PinguTrip.prettyDirections(ways);

        assertEquals("", output);
    }
}
