package pgdp.pingutrip;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathLengthTest {
    @Test
    @DisplayName("A simple Check of the path length")
    void pathLengthSimpleTest() {
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))));
        assertEquals(1.0, value, "Simple Check for one OneWay");
    }

    @Test
    @DisplayName("A simple Check of the path length")
    void pathLenghtSimpleTest2() {
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)), new OneWay(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0))));
        assertEquals(2.0, value, "Simple Check for two oneWays");
    }
}