package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TransformToWaysTest {
    @Test
    void testTransformToWays() {
        List<WayPoint> points = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4));
        List<OneWay> ways = PinguTrip.transformToWays(points).toList();

        OneWay[] expected = { new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)),
                new OneWay(new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4)) };
        OneWay[] actual = ways.toArray(OneWay[]::new);

        assertArrayEquals(expected, actual);
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
