package pgdp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class GameInputStream extends InputStream {

	private final Deque<String> inputQueue;

	public GameInputStream(String... inputs) {
		inputQueue = new ArrayDeque<>(inputs.length);
		for (String s : inputs) {
			inputQueue.addLast(s + "\n");
		}
	}

	public static GameInputStream fullAI(int restartCount) {
		String[] inputs = new String[1 + restartCount * 2 + 1];

		inputs[0] = "0";
		for (int i = 0; i < restartCount; i++) {
			inputs[i * 2 + 1] = "1";
			inputs[i * 2 + 2] = "0";
		}
		inputs[inputs.length - 1] = "0";

		return new GameInputStream(inputs);
	}

	public static GameInputStream fullAI() {
		return fullAI(0);
	}

	@Override
	public int read() throws IOException {
		byte[] b = new byte[1];
		int len = read(b, 0, 1);
		if (len == -1) {
			return -1;
		}
		return b[0];
	}

	@Override
	public synchronized int read(byte[] b, int off, int len) {
		String line = inputQueue.pop();

		byte[] lineBytes = line.getBytes();
		int readLen = Math.min(len, lineBytes.length);
		System.arraycopy(lineBytes, off, b, 0, readLen);

		System.out.print(line);

		return readLen;
	}
}
