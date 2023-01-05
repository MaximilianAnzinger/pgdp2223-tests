package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathLengthTest {
    @Test
    @DisplayName("A simple Check of the path length")
    void pathLengthSimpleTest() {
        Stream<OneWay> oneWay = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)));

        double expected = 1.0;
        double actual = PinguTrip.pathLength(oneWay);

        assertEquals(expected, actual, "Simple Check for one OneWay");
    }

    @Test
    @DisplayName("A simple Check of the path length")
    void pathLenghtSimpleTest2() {
        Stream<OneWay> oneWay = Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0)));

        double expected = 2.0;
        double actual = PinguTrip.pathLength(oneWay);

        assertEquals(expected, actual, "Simple Check for two oneWays");
    }
}
