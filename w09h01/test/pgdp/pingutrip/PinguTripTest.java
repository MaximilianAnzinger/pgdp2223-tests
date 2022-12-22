package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PinguTripTest
{

	@Nested
	@DisplayName("readWayPoints(...) tests")
	class ReadWayPoints
	{
		private static final String TEST_FILES_DIRECTORY = "test_txt_files/",
				NORMAL_TEST_FILE = ReadWayPoints.TEST_FILES_DIRECTORY + "normal.txt",
				EMPTY = ReadWayPoints.TEST_FILES_DIRECTORY + "empty.txt", OFFICIAL_TEST_FILE = "test_paths/path.txt";

		@BeforeAll
		public static void setUp()
		{
			// create test text files
			try
			{
				new File(ReadWayPoints.TEST_FILES_DIRECTORY).mkdirs();
				Path normalTest = Path.of(ReadWayPoints.NORMAL_TEST_FILE);
				if (!Files.exists(normalTest))
				{
					normalTest.toFile().createNewFile();
					Files.writeString(normalTest, """
							0.0;1.0
							// comment
							//7.0;5.0
							0.0;0.0
							---
							5.0;5.0""");
				}
				Path empty = Path.of(ReadWayPoints.EMPTY);
				if (!Files.exists(empty))
				{
					empty.toFile().createNewFile();
					Files.writeString(empty, "");
				}
			}
			catch (Exception e)
			{
				System.err.println("error while setting up the ReadWayPoints tests");
			}
		}

		@Test
		@DisplayName(value = "normal test")
		void testReadWayPointsNormal()
		{
			assertIterableEquals(List.of(new WayPoint(0, 1), new WayPoint(0, 0)),
					PinguTrip.readWayPoints(ReadWayPoints.NORMAL_TEST_FILE).toList(), "normal test");
		}

		@Test
		@DisplayName(value = "artemis example")
		void testReadWayPointsArtemisExample()
		{
			assertIterableEquals(List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)),
					PinguTrip.readWayPoints(ReadWayPoints.OFFICIAL_TEST_FILE).toList(), "example from artemis");
		}

		@Test
		@DisplayName(value = "empty test")
		void testReadWayPointsEmptyStreamOnEmptyStream()
		{
			assertEquals(0, PinguTrip.readWayPoints(ReadWayPoints.EMPTY).count(), "empty test");
		}

		@Test
		@DisplayName(value = "empty stream should be returned on error")
		void testReadWayPointsEmptyStreamOnError()
		{
			assertEquals(0, PinguTrip.readWayPoints(null).count(), "empty stream should be returned on error");
		}
	}

	@Nested
	@DisplayName("transformToWays(...) tests")
	class TransformToWays
	{
		@Test
		@DisplayName(value = "artemis example")
		void testTransformToWaysArtemisExample()
		{
			assertIterableEquals(
					List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)),
							new OneWay(new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4))),
					PinguTrip
							.transformToWays(
									List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2), new WayPoint(2.1, 7.4)))
							.toList(),
					"example from artemis");
		}
	}

	@Nested
	@DisplayName("pathLength(...) tests")
	class PathLength
	{
		@Test
		@DisplayName(value = "normal test")
		void testPathLengthNormal()
		{
			assertEquals(15.0,
					PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(5.0, 0.0), new WayPoint(0.0, 0.0)),
							new OneWay(new WayPoint(10.0, 0.0), new WayPoint(0.0, 0.0)))),
					"normal test");
		}

		@Test
		@DisplayName(value = "artemis example")
		void testPathLengthArtemisExample()
		{
			assertEquals(1.0,
					PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)))),
					"example from artemis");
		}

		@Test
		@DisplayName(value = "official example")
		void testPathLengthOfficialExample()
		{
			assertEquals(17.23078640109035,
					PinguTrip.pathLength(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
					"official example");
		}
	}

	@Nested
	@DisplayName("kidFriendlyTrip(...) tests")
	class KidFriendlyTrip
	{
		@Test
		@DisplayName(value = "artemis example")
		void testKidFriendlyTripArtemisExample()
		{
			assertIterableEquals(List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))),
					PinguTrip.kidFriendlyTrip(List.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0)),
							new OneWay(new WayPoint(1.0, 0.0), new WayPoint(3.0, 0.0)),
							new OneWay(new WayPoint(3.0, 0.0), new WayPoint(4.0, 0.0)))),
					"example from artemis");
		}

		@Test
		@DisplayName(value = "official example")
		void testKidFriendlyTripOfficialExample()
		{
			assertIterableEquals(List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
					PinguTrip.kidFriendlyTrip(List.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
					"official example");
		}

		@Test
		@DisplayName(value = "empty oneWays")
		void testKidFriendlyTripEmpty()
		{
			assertIterableEquals(List.of(), PinguTrip.kidFriendlyTrip(List.of()), "empty oneWays");
		}
	}

	@Nested
	@DisplayName("furthestAwayFromHome(...) tests")
	class FurthestAwayFromHome
	{
		@Test
		@DisplayName(value = "compare double high precision test")
		void testFurthestAwayFromHomeCompareSmallValues()
		{
			List<WayPoint> wayPoints = List.of(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0), new WayPoint(1.1, 0.0));
			assertEquals(wayPoints.get(2), PinguTrip.furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0)),
					"compare double with a very small difference difference");
		}

		@Test
		@DisplayName(value = "artemis example")
		void testFurthestAwayFromHomeArtemisExample()
		{
			assertEquals(new WayPoint(2.0, 0.0),
					PinguTrip.furthestAwayFromHome(Stream.of(new WayPoint(1.0, 0.0), new WayPoint(2.0, 0.0)),
							new WayPoint(0.0, 0.0)),
					"example from artemis");
		}

		@Test
		@DisplayName(value = "no other point -> return home")
		void testFurthestAwayFromHomeNoPointsReturnHome()
		{
			WayPoint home = new WayPoint(1.0, 1.0);
			assertEquals(home, PinguTrip.furthestAwayFromHome(Stream.empty(), home), "no WayPoints -> return home");
		}

		@Test
		@DisplayName(value = "official example")
		void testFurthestAwayFromHomeOfficialExample()
		{
			List<WayPoint> wayPoints = List.of(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2));
			assertEquals(wayPoints.get(1), PinguTrip.furthestAwayFromHome(wayPoints.stream(), wayPoints.get(0)),
					"official example");
		}
	}

	@Nested
	@DisplayName("onTheWay(...) tests")
	class OnTheWay
	{

		@Test
		@DisplayName(value = "artemis example")
		void testOnTheWayArtemisExample()
		{
			assertTrue(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(0.0, 0.0), new WayPoint(1.0, 0.0))),
					new WayPoint(0.5, 0.0)), "example from artemis");
		}

		@Test
		@DisplayName(value = "official example 1")
		void testOnTheWayOfficialExample1()
		{
			assertFalse(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
					new WayPoint(0.0, 0.0)), "official example 1 (should be false");
		}

		@Test
		@DisplayName(value = "official example 2")
		void testOnTheWayOfficialExample2()
		{
			assertTrue(PinguTrip.onTheWay(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2))),
					new WayPoint(19.1, 3.2)), "official example 2 (should be true");
		}
	}

	@Nested
	@DisplayName("prettyDirections(...) tests")
	class PrettyDirections
	{

		@Test
		@DisplayName(value = "artemis example")
		void testPrettyDirectionsArtemisExample()
		{
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
		void testPrettyDirectionsOfficialExample()
		{
			assertEquals("25 Schritte Richtung 331 Grad.",
					PinguTrip.prettyDirections(Stream.of(new OneWay(new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2)))),
					"official example");
		}

	}
}
