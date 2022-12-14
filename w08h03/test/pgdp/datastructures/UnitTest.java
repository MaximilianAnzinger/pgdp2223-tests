package pgdp.datastructures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UnitTest {
	static int seed = 69420;

	<Type extends Comparable<Type>> void testArray(Type[] input) {
		QuarternarySearchTree<Type> tree = new QuarternarySearchTree<Type>();
		Type[] expected = Arrays.copyOf(input, input.length);
		Arrays.sort(expected);

		for (Type element : input) {
			tree.insert(element);
		}

		int position = 0;
		for (Type element : tree) {
			Type exp = expected[position++];
			assertEquals(exp, element, "Invalid Output at position [" + (position - 1) + "]: Expected [" + exp
					+ "], got [" + element + "]");
		}

		assertEquals(expected.length, position,
				"Invalid Iteration Count. Expected [" + expected.length + "] got [" + position + "]");
	}

	@Test
	@DisplayName("should iterate over empty graph")
	public void emptyTest() {
		assertFalse((new QuarternarySearchTree<Integer>()).iterator().hasNext(), "hasNext() should return false on an empty graph");
		assertThrows(NoSuchElementException.class, () -> (new QuarternarySearchTree<Integer>()).iterator().next(), "next() should throw on an empty graph");
	}

	@Test
	@DisplayName("should iterate over graph in artemis example")
	public void artemisTest() {
		testArray(new Integer[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 });
	}

	@Test
	@DisplayName("should iterate over a graph of one repeat element")
	public void oneElementTest() {
		testArray(new Integer[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
	}

	@Test
	@DisplayName("should iterate over a numeric graph")
	public void numericTest() {
		IntStream.range(0, 10).forEach(i -> {
			var args = new Integer[i];
			var rnd = new Random(seed + i);

			for (int j = 0; j < args.length; j++) {
				args[j] = rnd.nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
			}

			testArray(args);
		});
	}

	// private static int seedDifference = 0;
	// private static Stream<Arguments> testArray() {
	// 	// TODO improve this
	// 	return Stream.generate(() -> {
	// 		return arguments((new Random(seed + seedDifference++)).ints(seedDifference, 0, Integer.MAX_VALUE).toArray());
	// 	}).limit(10);
	// }

	private String generateString(Random rnd) {
		StringBuilder s = new StringBuilder();
		rnd.ints(rnd.nextInt(100), 97, 122).mapToObj(i -> (char) i).forEach(s::append);
		return s.toString();
	}

	@Test
	@DisplayName("should iterate over a non-numeric graph")
	public void stringTest() {
		IntStream.range(0, 10).forEach(i -> {
			var args = new String[i];
			var rnd = new Random(seed + i);

			for (int j = 0; j < args.length; j++) {
				args[j] = generateString(rnd);
				// System.out.println(args[j]);
			}

			testArray(args);
		});
	}

	@Test
	@DisplayName("should return the correct values for hasNext() on full tree")
	public void hasNextTest() {
		QuarternarySearchTree<Integer> tree = new QuarternarySearchTree<>();
		int i = 100;

		// fill the tree with a bunch of numbers
		for (int j = 0; j <= i; j++) {
			tree.insert(j);
		}

		var treeIt = tree.iterator();
		Assertions.assertTrue(treeIt.hasNext());
		for (int j = 0; j <= i; j++) {
			treeIt.next();
			if(j == i) {
				Assertions.assertFalse(treeIt.hasNext());
			} else {
				Assertions.assertTrue(treeIt.hasNext());
			}
		}
	}

	private class PositionTest implements Comparable<PositionTest> {
		private int number;
		private Function<PositionTest, Integer> query = (a) -> this.number - a.number;

		public PositionTest (int n) {
			number = n;
		}

		@Override
		public int compareTo(PositionTest o) {
			return query.apply(o);
		}

		public void resetQuery() {
			// Destroy Query
			query = (a) -> 0;
		}

		public int getNumber() {
			return number;
		}
	}

	//
	// THIS METHOD TESTS WETHER INORDER WORKS WITH OBJECTS WHOSE COMPARABILITY
	// HAS BEEN DESTROYED AFTER TREE GENERATION
	//
	// YOU CAN DISABLE THE TEST BY UNCOMMENT THE DISABLED LINE IF YOU FEEL SO
	//

	@Test
	@DisplayName("should rely on position of tree instead of comparability")
	//@Disabled
	public void doesRelyOnPositionTest() {
		var artemis = new Integer[] { 8, 4, 12, 1, 5, 9, 13, 3, 7, 11, 15, 2, 6, 10, 14 };
		var objects_cache = new ArrayList<PositionTest>();
		var tree = new QuarternarySearchTree<PositionTest>();

		// Add objects to tree and objects_cache
		for (Integer element : artemis) {
			var obj = new PositionTest(element);
			tree.insert(obj);
			objects_cache.add(obj);
		}

		// Reset query 
		for (PositionTest element : objects_cache) {
			element.resetQuery();
		}

		int position = 0;
		for (PositionTest element : tree) {
			int got = element.getNumber();
			int exp = position + 1;
			assertEquals(exp, got, "Invalid Output at position [" + position + "]: Expected [" + exp
					+ "], got [" + got + "]");
			position++;
		}

		assertEquals(15, position,
				"Invalid Iteration Count. Expected [15] got [" + position + "]");
	}
}
