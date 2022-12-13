package pgdp.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
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

	@Test
	public void artemisTest() {
		dynamicTests(new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 });
	}

	@ParameterizedTest
	@DisplayName("Dynamic Test")
	@MethodSource
	void dynamicTests(int[] input) {
		QuarternarySearchTree<Integer> tree = new QuarternarySearchTree<Integer>();
		int[] expected = Arrays.copyOf(input, input.length);
		Arrays.sort(expected);

		for (int element : input) {
			tree.insert(element);
		}

		int position = 0;
		for (int element : tree) {
			int exp = expected[position++];
			assertEquals(exp, element, "Invalid Output at position [" + (position - 1) + "]: Expected [" + exp
					+ "], got [" + element + "]");
		}

		assertEquals(expected.length, position,
				"Invalid Iteration Count. Expected [" + expected.length + "] got [" + position + "]");
	}

	private static Stream<Arguments> dynamicTests() {
		// <region tests>
		return Stream.of(
				//
				// Artemis Example
				//
				arguments(
						new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 }),
				//
				// Empty Example
				//
				arguments(
						new int[] {})
		// </end-region>
		);
	}
}
