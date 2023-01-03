package pgdp.pingutrip;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SouroundTest {
    @Test
    @DisplayName("A simple Check of the path length")
    void pathLengthSimpleTest(){
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))));
        assertEquals(1.0, value, "Simple Check for one OneWay");
    }

    @Test
    @DisplayName("A simple Check of the path length")
    void pathLenghtSimpleTest2(){
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),new OneWay(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0))));
        assertEquals(2.0, value, "Simple Check for two oneWays");
    }

    @Test
    @DisplayName("Checks if really stops in kidFriendlyTripTest when the first element is longer then the average ")
    void kidFriendlyTripTest(){
        List<OneWay> lists = List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)));
        List<OneWay> result = List.of();
        assertArrayEquals(PinguTrip.kidFriendlyTrip(lists), result);
    }

    @Test
    @DisplayName("Checks if every element gets returned, checks for the correctness of the math")
    void kidFriendlyTripTest2(){
        List<OneWay> lists = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));
        List<OneWay> result = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));
        assertArrayEquals(PinguTrip.kidFriendlyTrip(lists), result);
    }

    @Test
    @DisplayName("The Simple Artemis Test")
    void transformToWaysTest(){
        List<WayPoint> list = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4));
        assertArrayEquals(PinguTrip.transformToWays(list).toList(),List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)), new OneWay(new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4))));
    }

    @Test
    @DisplayName("Now with 4 elements and checks for the correctness of the element passing")
    void transformToWaysTest2(){
        List<WayPoint> list = List.of(new WayPoint(0.0,1.0), new WayPoint(1.0,1.0), new WayPoint(1.0,2.0), new WayPoint(2.0,2.0));
        assertArrayEquals(PinguTrip.transformToWays(list).toList(), List.of(new OneWay(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0)), new OneWay(new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0)), new OneWay(new WayPoint(1.0, 2.0), new WayPoint(2.0, 2.0))));
    }

    @Test
    @DisplayName("Now with 5 elements and checks for the correctness of the element passing")
    void transformToWaysTest3(){
        List<WayPoint> list = List.of(new WayPoint(0.0,1.0), new WayPoint(1.0,1.0), new WayPoint(1.0,2.0), new WayPoint(2.0,2.0), new WayPoint(2.0,3.0));
        assertArrayEquals(PinguTrip.transformToWays(list).toList(), List.of(new OneWay(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0)), new OneWay(new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0)), new OneWay(new WayPoint(1.0, 2.0), new WayPoint(2.0, 2.0)), new OneWay(new WayPoint(2.0, 2.0), new WayPoint(2.0, 3.0))));
    }
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