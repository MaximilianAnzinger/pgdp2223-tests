package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Now with 4 elements and checks for the correctness of the element passing")
    void testTransformToWaysFourElements() {
        List<WayPoint> wayPoints = List.of(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0),
                new WayPoint(2.0, 2.0));

        OneWay[] expected = { new OneWay(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0)),
                new OneWay(new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0)),
                new OneWay(new WayPoint(1.0, 2.0), new WayPoint(2.0, 2.0)) };
        OneWay[] actual = PinguTrip.transformToWays(wayPoints).toArray(OneWay[]::new);

        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("Now with 5 elements and checks for the correctness of the element passing")
    void testTransformToWaysFiveElements() {
        List<WayPoint> wayPoints = List.of(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0),
                new WayPoint(2.0, 2.0), new WayPoint(2.0, 3.0));

        OneWay[] expected = List.of(new OneWay(new WayPoint(0.0, 1.0), new WayPoint(1.0, 1.0)),
                new OneWay(new WayPoint(1.0, 1.0), new WayPoint(1.0, 2.0)),
                new OneWay(new WayPoint(1.0, 2.0), new WayPoint(2.0, 2.0)),
                new OneWay(new WayPoint(2.0, 2.0), new WayPoint(2.0, 3.0))).toArray(OneWay[]::new);
        OneWay[] actual = PinguTrip.transformToWays(wayPoints).toList().toArray(OneWay[]::new);

        assertArrayEquals(expected, actual);
    }
}
