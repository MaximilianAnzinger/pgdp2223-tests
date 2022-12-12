package pgdp.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UnitTest {
	@Test
	public void emptyTest() {
		assertThrows(NoSuchElementException.class, () -> (new QuarternarySearchTree<Integer>()).iterator().next());
	}

	@ParameterizedTest
	@DisplayName("Dynamic Test")
	@MethodSource
	void dynamicTests(String type, int[] expected, int[] input) {
		QuarternarySearchTree<Integer> tree = new QuarternarySearchTree<Integer>();

		for (int i : input) {
			tree.insert(i);
		}

		int j = 0;
		for (int i : tree) {
			int e = expected[j++];
			assertEquals(e, i, type + ": Invalid Output at position [" + (j-1) + "]: Expected [" + e + "], got [" + i + "]");
		}

		assertEquals(expected.length, j, type + ": Invalid Iteration Count. Expected [" + expected.length + "] got [" + j + "]");
	}

	private static Stream<Arguments> dynamicTests() {
		// <region tests>
		return Stream.of(
				//
				// Artemis Example
				//
				arguments(
						"Artemis Example",
						new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
						new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 }),
				//
				// Empty Example
				//
				arguments(
						"Empty Test",
						new int[] {},
						new int[] {})
		// </end-region>
		);
	}
}
