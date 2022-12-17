package pgdp.datastructures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerformanceTest {
	final int seed = 69420;

	/*
	 * The ÜL has declined to give us an order of magnitude on Zulip so I went with
	 * one million nodes. This is surely enough, but it might even be too much. It
	 * is unlikely but possible to have a valid solution and fail these performance
	 * tests. If you like, you can change the variable NUMBER_OF_VALUES_IN_TREE and
	 * try some lower values.
	 * 
	 * This test should absolutely pass for low values such as 1000 and only take a
	 * few milliseconds to do so.
	 */
	@Test
	@DisplayName("Read 1M values")
	public void testPerformance1M() {
		int NUMBER_OF_VALUES_IN_TREE = (int) 1e6;

		List<Integer> values = IntStream.range(0, NUMBER_OF_VALUES_IN_TREE).boxed().collect(Collectors.toList());
		List<Integer> solution = IntStream.range(0, NUMBER_OF_VALUES_IN_TREE).boxed().collect(Collectors.toList());
		Collections.shuffle(values, new Random(seed));

		QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
		for (Integer i : values) {
			n.insert(i);
		}

		ArrayList<Integer> actual = new ArrayList<>();

		System.out.println(
				"Keep in mind the RAM usage and its differences during iteration is not a terribly useful metric here:");
		long memUsageMBBefore = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
				/ (1024 * 1024);
		System.out.println("Memory usage before iteration: " + memUsageMBBefore + "MB");

		long startTime = System.nanoTime();

		for (int r : n) {

			if (r == 0) {
				long memUsageMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
						/ (1024 * 1024);
				System.out.println("Memory usage at the start of iteration: " + memUsageMB + "MB");
			} else if (r == NUMBER_OF_VALUES_IN_TREE - 1) {
				long memUsageMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
						/ (1024 * 1024);
				System.out.println("Memory usage at the end of iteration: " + memUsageMB + "MB");
			}

			actual.add(r);
		}

		long endTime = System.nanoTime();

		System.out.println("Time taken to read " + NUMBER_OF_VALUES_IN_TREE + " nodes from a Tree of "
				+ NUMBER_OF_VALUES_IN_TREE + ": "
				+ (endTime - startTime) / 1000000 + "ms");

		assertEquals(solution, actual);
	}

	/*
	 * The same disclaimer is valid here as in the testPerformance1M test above.
	 * 
	 * Here you can again change the two variables NUMBER_OF_VALUES_IN_TREE and
	 * NUMBER_OF_VALUES_TO_ITERATE_OVER to see if it passes with lower values.
	 * 
	 * This is much faster for lazy implementations than it is for non-lazy ones but
	 * should easily be solvable for both.
	 * 
	 * A non-lazy implementation will take roughly as long here as for the
	 * "Read 1M values" test.
	 */
	@Test
	@DisplayName("1M values but only read first 1k")
	public void testPerformance1MRead1k() {
		int NUMBER_OF_VALUES_IN_TREE = (int) 1e6;
		int NUMBER_OF_VALUES_TO_ITERATE_OVER = (int) 1000;

		List<Integer> values = IntStream.range(0, NUMBER_OF_VALUES_IN_TREE).boxed().collect(Collectors.toList());
		List<Integer> solution = IntStream.range(0, NUMBER_OF_VALUES_TO_ITERATE_OVER).boxed()
				.collect(Collectors.toList());
		Collections.shuffle(values, new Random(seed));

		QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
		for (Integer i : values) {
			n.insert(i);
		}

		ArrayList<Integer> actual = new ArrayList<>();

		long startTime = System.nanoTime();

		for (int r : n) {
			if (r >= NUMBER_OF_VALUES_TO_ITERATE_OVER) {
				break;
			}
			actual.add(r);
		}

		long endTime = System.nanoTime();

		System.out.println("Time taken to read " + NUMBER_OF_VALUES_TO_ITERATE_OVER + " nodes from a Tree of "
				+ NUMBER_OF_VALUES_IN_TREE + ": "
				+ (endTime - startTime) / 1000000 + "ms");

		assertEquals(solution, actual);
	}
}
