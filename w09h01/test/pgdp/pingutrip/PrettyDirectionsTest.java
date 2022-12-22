package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PrettyDirectionsTest {
	@Test
	@DisplayName(value = "artemis example")
	void testPrettyDirectionsArtemisExample() {
		assertEquals("""
				1 Schritte Richtung 0 Grad.
				3 Schritte Richtung 0 Grad.
				2 Schritte Richtung 45 Grad.""",
				PinguTrip.prettyDirections(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
						new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)),
						new OneWay(new WayPoint(3.0, 0.0), new WayPoint(4.0, 1.0)))),
				"example from artemis");
	}

	@Test
	@DisplayName(value = "official example")
	void testPrettyDirectionsOfficialExample() {
		assertEquals("25 Schritte Richtung 331 Grad.",
				PinguTrip.prettyDirections(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
				"official example");
	}
}
