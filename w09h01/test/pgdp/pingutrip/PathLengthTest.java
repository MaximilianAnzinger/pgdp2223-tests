package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathLengthTest {
	@Test
	@DisplayName(value = "normal test")
	void testPathLengthNormal() {
		assertEquals(15.0, PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(5.0, 0.0), new WayPoint(0.0, 0.0)),
				new OneWay(new WayPoint(10.0, 0.0), new WayPoint(0.0, 0.0)))), "normal test");
	}

	@Test
	@DisplayName(value = "artemis example")
	void testPathLengthArtemisExample() {
		assertEquals(1.0, PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)))),
				"example from artemis");
	}

	@Test
	@DisplayName(value = "official example")
	void testPathLengthOfficialExample() {
		assertEquals(17.23078640109035,
				PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
				"official example");
	}
}
