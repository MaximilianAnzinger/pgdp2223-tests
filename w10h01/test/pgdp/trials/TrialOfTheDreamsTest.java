package pgdp.trials;

import java.util.function.Function;
import java.util.Random;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class TrialOfTheDreamsTest {

	// change for harder decoding
	static final int keyLen = 5;
	// change to test expected code duration in seconds (Artemis times out after 5s)
	static final int expectedTime = 5;
	// change to test a specific key with "test_not_random_LockPick"
	static final byte[] expectedKey = new byte[] { 40, 41, 42, 43 };
	// Use this seed in Random
	static final int seed = 420;

	@Test
	public void testRandomLockPick() {
		assertTimeoutPreemptively(Duration.ofSeconds(expectedTime), () -> {

			ByteArrayGenerator lock = new ByteArrayGenerator(keyLen);
			byte[] keyExpected = lock.generatedArray;
			byte[] keyActual = TrialOfTheDreams.lockPick(lock);

			assertArrayEquals(keyExpected, keyActual);
			Thread.sleep(500);

		});
	}

	@Test
	public void test_not_random_LockPick() {
		assertTimeoutPreemptively(Duration.ofSeconds(expectedTime), () -> {
			Function<byte[], Boolean> lock = key -> Arrays.equals(key, expectedKey);
			byte[] result = TrialOfTheDreams.lockPick(lock);
			assertArrayEquals(expectedKey, result);

			Thread.sleep(500);
		});
	}
	//Remove @Disabled if you want to test random lock without a time limit
	@Disabled
	@Test
	public void testRandomLockPickInfinite() {

		ByteArrayGenerator lock = new ByteArrayGenerator(keyLen);
		byte[] keyExpected = lock.generatedArray;
		byte[] keyActual = TrialOfTheDreams.lockPick(lock);

		assertArrayEquals(keyExpected, keyActual);
	}


	public class ByteArrayGenerator implements Function<byte[], Boolean> {
		private final byte[] generatedArray;

		public ByteArrayGenerator(int length) {
			Random random = new Random(seed);
			generatedArray = new byte[length];
			random.nextBytes(generatedArray);

		}

		@Override
		public Boolean apply(byte[] key) {
			if (key.length != generatedArray.length) {
				return false;
			}

			for (int i = 0; i < key.length; i++) {
				if (key[i] != generatedArray[i]) {
					return false;
				}
			}

			return true;
		}
	}

}
