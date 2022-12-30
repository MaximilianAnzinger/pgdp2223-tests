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
}
