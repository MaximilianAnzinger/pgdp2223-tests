package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KidFriedlyTripTest {
    @Test
    @DisplayName("Checks if really stops in kidFriendlyTripTest when the first element is longer then the average ")
    void testKidFriendlyTrip() {
        List<OneWay> lists = List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
                new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)));

        List<OneWay> expected = List.of();
        List<OneWay> actual = PinguTrip.kidFriendlyTrip(lists);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

    @Test
    @DisplayName("Checks if every element gets returned, checks for the correctness of the math")
    void testKidFriendlyTrip2() {
        List<OneWay> lists = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));

        List<OneWay> expected = List.of(new OneWay(new WayPoint(2.0, 0.0), new WayPoint(4.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)),
                new OneWay(new WayPoint(0.0, 0.0), new WayPoint(2.0, 0.0)));
        List<OneWay> actual = PinguTrip.kidFriendlyTrip(lists);

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
