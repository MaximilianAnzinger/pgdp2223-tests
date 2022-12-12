package pgdp.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class UnitTest {
	static int[] values = new int[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 };

	@Test
	public void artemisExample() {
		QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();

		for (int i : values) {
			n.insert(i);
		}

		int j = 1;
		for (int i : n) {
			assertEquals(j++, i, "Invalid Output at position [" + (j - 1) + "]: Expected [" + j + "], got [" + i + "]");
		}
		j--;

		assertEquals(15, j, "Invalid Iteration Count. Expected [15] got [" + j + "]");
	}

	@Test
	public void emptyTest() {
		assertThrows(NoSuchElementException.class, () -> (new QuarternarySearchTree<Integer>()).iterator().next());
	}
}
