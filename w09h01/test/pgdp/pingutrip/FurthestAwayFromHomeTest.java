package pgdp.pingutrip;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FurthestAwayFromHomeTest {
    @Test
    @DisplayName("The Artemis tests")
    void FurthestAwayFromHomeTest() {
        Stream<WayPoint> path = Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        WayPoint actual = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(2.0, 0.0);
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("With to identical biggest element ")
    void FurthestAwayFromHomeTest1(){
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.0), new WayPoint(3.0, 4.1) ,new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        WayPoint result = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(3.0, 4.1);
        assertEquals(result, expected);
    }

    @Test
    @DisplayName("Stream with no elements should return home")
    void FurthestAwayFromHomeTest2(){
        Stream<WayPoint> path = Stream.of();
        WayPoint home = new WayPoint(0.0, 0.0);
        WayPoint result = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected = new WayPoint(0.0, 0.0);
        assertEquals(result, expected);
    }

    @Test
    @DisplayName("Now with a home that is not a 0.0")
    void FurthestAwayFromHomeTest3(){
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.1), new WayPoint(4.1, 3.0) ,new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(4.1, 3.0);
        WayPoint result = PinguTrip.furthestAwayFromHome(path, home);
        WayPoint expected =  new WayPoint(1.0, 0.0);
        assertEquals(result ,expected);
    }

}
