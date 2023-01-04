package pgdp.pingutrip;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class kidFriedlyTripTest {
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
    void kidFriendlyTripTest2() {
        List<OneWay> lists = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));

        List<OneWay> expected = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));
        List<OneWay> actual = PinguTrip.kidFriendlyTrip(lists);

        assertArrayEquals(expected.toArray(), actual.toArray());
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
