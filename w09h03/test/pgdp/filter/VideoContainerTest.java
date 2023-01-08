package pgdp.filter;

import org.bytedeco.javacv.FrameRecorder;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class VideoContainerTest {

    @Test
    void doesWriteCallFrameConsumerClose() throws FileNotFoundException, IllegalVideoFormatException, FrameRecorder.Exception {
        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        FrameConsumerWrapper fc = new FrameConsumerWrapper();
        in.write(fc);

        assertTrue(fc.isCloseCalled(), "call to write should lead to FrameConsumer getting closed");
    }

    @Test
    void doesWritePassException() throws FileNotFoundException, IllegalVideoFormatException {

        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        FrameRecorder.Exception onCloseException = new FrameRecorder.Exception("dummy");
        FrameConsumerWrapper fc = new FrameConsumerWrapper(onCloseException);

        try {
            in.write(fc);
            fail("exception should not get handled within write()");
        } catch (Exception e) {
            assertEquals(onCloseException, e, "exception should not have been rethrown/wrapped");
        }

    }

    static private class FrameConsumerWrapper extends FrameConsumer {
        private boolean closeCalled = false;
        private final FrameRecorder.Exception onCloseException;

        private FrameConsumerWrapper() {
            this.onCloseException = null;
        }

        private FrameConsumerWrapper(FrameRecorder.Exception onCloseException) {
            this.onCloseException = onCloseException;
        }

        @Override
        public void consume(Frame frame) {
            // noop
        }

        @Override
        public void close() throws FrameRecorder.Exception {
            closeCalled = true;

            if (onCloseException != null) {
                throw onCloseException;
            }
        }

        public boolean isCloseCalled() {
            return closeCalled;
        }
    }

}
