package w10h01.test.pgdp.trials;

import java.util.function.Function;
import java.security.SecureRandom;
import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class TrialOfTheDreamsTest {

	// change for harder decoding
	int keyLen = 16;
	// change to test expected code duration in seconds (Artemis times out after 5s)
	int expectedTime = 5;
	// change to test a specific key with "test_not_random_LockPick"
	byte[] expectedKey = new byte[] { 40, 41, 42, 43 };

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
	//Remove @Disabled if you don't want time limit
	@Disabled
	@Test
	public void testRandomLockPickInfinite() {

		ByteArrayGenerator lock = new ByteArrayGenerator(keyLen);
		byte[] keyExpected = lock.generatedArray;
		byte[] keyActual = TrialOfTheDreams.lockPick(lock);

		assertArrayEquals(keyExpected, keyActual);
	}

	
	// This class provides a cryptographically strong random numbergenerator

	public class ByteArrayGenerator implements Function<byte[], Boolean> {
		private final byte[] generatedArray;

		public ByteArrayGenerator(int length) {
			SecureRandom random = new SecureRandom();
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