package pgdp.pingutrip;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AllAround {
    @Test
    void pathLengthSimpleTest(){
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))));
        assertEquals(1.0, value, "Simple Check for one OneWay");
    }

    @Test
    void pathLenghtSimpleTest2(){
        Double value = PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),new OneWay(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0))));
        assertEquals(2.0, value, "Simple Check for two oneWays");
    }

    @Test
    void kidFriendlyTripTest(){
        List<OneWay> lists = List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)),
                new OneWay(new WayPoint(3.0, 0.0), new WayPoint(4.0, 0.0)));
        List<OneWay> result = List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)));
        assertArrayEquals(PinguTrip.kidFriendlyTrip(lists), result);
    }

    @Test
    void transformToWaysTest(){
        List<WayPoint> list = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4));
        assertArrayEquals(PinguTrip.transformToWays(list).toList(),List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)), new OneWay(new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4))));
    }

    @Test
    void transformToWaysTestVoid(){
        List<WayPoint> list = List.of();
        assertArrayEquals(PinguTrip.transformToWays(list).toList(),List.of());
    }

    @Test
    void furthestAWay(){
        Stream<WayPoint> path = Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0));
        WayPoint home = new WayPoint(0.0, 0.0);
        assertArrayEquals(PinguTrip.furthestAwayFromHome(path, home), new WayPoint(2.0, 0.0));
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