package pgdp.filter;

import org.bytedeco.javacv.FrameRecorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class IsClosedTest {

    @Test
    void test() throws FileNotFoundException, IllegalVideoFormatException, FrameRecorder.Exception {
        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        FrameConsumerWrapper fc = new FrameConsumerWrapper();
        in.write(fc);

        Assertions.assertTrue(fc.isClosed(), "call to write should lead to FrameConsumer getting closed");
    }

    static private class FrameConsumerWrapper extends FrameConsumer {
        private boolean closed = false;

        @Override
        public void consume(Frame frame) {
            // noop
        }

        @Override
        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }

}
