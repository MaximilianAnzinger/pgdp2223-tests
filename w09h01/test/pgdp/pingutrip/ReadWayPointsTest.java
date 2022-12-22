package pgdp.pingutrip;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReadWayPointsTest {
	final String path = "test_paths/path.txt";
	private static final String TEST_FILES_DIRECTORY = "test_txt_files/",
			NORMAL_TEST_FILE = ReadWayPointsTest.TEST_FILES_DIRECTORY + "normal.txt";

	@BeforeAll
	public static void setUp() {
		// create test text file
		try {
			new File(ReadWayPointsTest.TEST_FILES_DIRECTORY).mkdirs();
			Path normalTest = Path.of(ReadWayPointsTest.NORMAL_TEST_FILE);
			if (!Files.exists(normalTest)) {
				normalTest.toFile().createNewFile();
				Files.writeString(normalTest, """
						0.0;1.0
						// comment
						//7.0;5.0
						0.0;0.0
						---
						5.0;5.0""");
			}
		}
		catch (Exception e) {
			System.err.println("error while setting up the ReadWayPoints tests");
		}
	}

	@Test
	@DisplayName(value = "normal test")
	void testReadWayPointsNormal() {
		assertIterableEquals(List.of(new WayPoint(0, 1), new WayPoint(0, 0)),
				PinguTrip.readWayPoints(ReadWayPointsTest.NORMAL_TEST_FILE).toList(), "normal test");
	}

	@Test
	void testReturnsValuesFromFile() {
		Stream<WayPoint> points = PinguTrip.readWayPoints(path);

		WayPoint[] expected = { new WayPoint(4.0, 11.5), new WayPoint(19.1, 3.2) };
		WayPoint[] actual = points.toArray(WayPoint[]::new);

		assertArrayEquals(expected, actual, "The stream should contain the values from the file");
	}

	@Test
	void testReturnsEmptyStreamOnError() {
		Stream<WayPoint> points = PinguTrip.readWayPoints("");
		assertEquals(0, points.count(), "The stream should be empty");
	}
}