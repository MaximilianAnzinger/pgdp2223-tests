package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class KidFriendlyTripTest {
	@Test
	@DisplayName(value = "artemis example")
	void testKidFriendlyTripArtemisExample() {
		assertIterableEquals(List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))),
				PinguTrip.kidFriendlyTrip(List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
						new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)),
						new OneWay(new WayPoint(3.0, 0.0), new WayPoint(4.0, 0.0)))),
				"example from artemis");
	}

	@Test
	@DisplayName(value = "official example")
	void testKidFriendlyTripOfficialExample() {
		assertIterableEquals(List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
				PinguTrip.kidFriendlyTrip(List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
				"official example");
	}

	@Test
	@DisplayName(value = "empty oneWays")
	void testKidFriendlyTripEmpty() {
		assertIterableEquals(List.of(), PinguTrip.kidFriendlyTrip(List.of()), "empty oneWays");
	}
}
