package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OnTheWayTest {
	@Test
	@DisplayName(value = "artemis example")
	void testOnTheWayArtemisExample() {
		assertTrue(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))),
				new WayPoint(0.5, 0.0)), "example from artemis");
	}

	@Test
	@DisplayName(value = "official example 1")
	void testOnTheWayOfficialExample1() {
		assertFalse(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
				new WayPoint(0.0, 0.0)), "official example 1 (should be false");
	}

	@Test
	@DisplayName(value = "official example 2")
	void testOnTheWayOfficialExample2() {
		assertTrue(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
				new WayPoint(19.1, 3.2)), "official example 2 (should be true");
	}
}
