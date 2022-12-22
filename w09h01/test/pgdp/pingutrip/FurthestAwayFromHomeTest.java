package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FurthestAwayFromHomeTest {
	@Test
	@DisplayName(value = "compare double high precision test")
	void testFurthestAwayFromHomeCompareSmallValues() {
		List<WayPoint> wayPoints = List.of(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0), new WayPoint(1.1, 0.0));
		assertEquals(wayPoints.get(2), PinguTrip.furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0)),
				"compare double with a very small difference difference");
	}

	@Test
	@DisplayName(value = "artemis example")
	void testFurthestAwayFromHomeArtemisExample() {
		assertEquals(new WayPoint(2.0, 0.0),
				PinguTrip.furthestAwayFromHome(Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0)),
						new WayPoint(0.0, 0.0)),
				"example from artemis");
	}

	@Test
	@DisplayName(value = "no other point -> return home")
	void testFurthestAwayFromHomeNoPointsReturnHome() {
		WayPoint home = new WayPoint(1.0, 1.0);
		assertEquals(home, PinguTrip.furthestAwayFromHome(Stream.empty(), home), "no WayPoints -> return home");
	}

	@Test
	@DisplayName(value = "official example")
	void testFurthestAwayFromHomeOfficialExample() {
		List<WayPoint> wayPoints = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));
		assertEquals(wayPoints.get(1), PinguTrip.furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0)),
				"official example");
	}
}
