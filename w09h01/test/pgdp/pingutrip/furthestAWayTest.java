package pgdp.pingutrip;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

public class furthestAWayTest {
    @Test
    @DisplayName("The Artemis tests")
    void furthestAWayTest(){
        Stream<WayPoint> path = Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        assertArrayEquals(PinguTrip.furthestAwayFromHome(path, home), new WayPoint(2.0, 0.0));
    }

    @Test
    @DisplayName("With to identical biggest element ")
    void furthestAWayTest1(){
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.0), new WayPoint(3.0, 4.1) ,new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        assertArrayEquals(PinguTrip.furthestAwayFromHome(path, home), new WayPoint(3.0, 4.1));
    }

    @Test
    @DisplayName("Stream with no elements should return home")
    void furthestAWayTest2(){
        Stream<WayPoint> path = Stream.of();
        WayPoint home = new WayPoint(0.0, 0.0);
        assertArrayEquals(PinguTrip.furthestAwayFromHome(path, home), new WayPoint(0.0, 0.0));
    }

    @Test
    @DisplayName("Now with a home that is not a 0.0")
    void furthestAWayTest3(){
        Stream<WayPoint> path = Stream.of(new WayPoint(3.0, 4.1), new WayPoint(4.1, 3.0) ,new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(4.1, 3.0);
        assertArrayEquals(PinguTrip.furthestAwayFromHome(path, home), new WayPoint(1.0, 0.0));
    }

    private void assertArrayEquals(WayPoint furthestAwayFromHome, WayPoint wayPoint) {
        if(!furthestAwayFromHome.equals(wayPoint)) fail("They dont match expected:"+ wayPoint +" but was: "+ furthestAwayFromHome);
    }

    private void assertArrayEquals(List<OneWay> kidFriendlyTrip, List<OneWay> result) {
        if(kidFriendlyTrip.size() != result.size()) fail("Unequal already in size. Expected: "+result.size()+" but were: "+kidFriendlyTrip.size());
        else{
            for (int i = 0; i < kidFriendlyTrip.size(); i++) {
                if(!kidFriendlyTrip.get(i).equals(result.get(i))) fail("Unequal at element: "+i+" your were: "+kidFriendlyTrip.get(i)+" the expectet where: "+result.get(i));
            }
        }
    }
}
