package pgdp.filter;

import org.bytedeco.javacv.FrameGrabber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DecodeTest {

  private void closeProvider(FrameProvider fp) {
    try {
      fp.close();
    } catch (FrameGrabber.Exception e) {
      Assertions.fail(e);
    }
  }

  @Test
  @DisplayName("Decode first frame of noot")
  public void testDecodeFirstFrame() {
    // read the file
    VideoContainer in;
    FrameProvider fp = new FrameProvider("noot.mp4");

    Frame first = null;
    try {
      in = new VideoContainer(fp);
      first = fp.nextFrame();
    } catch (Exception e) {
      Assertions.fail(e);
    }

    String expected = """
        We2re no strangers to love
        You know the rules and so do I
        A full commitment's wh""";

    String actual = Operations.decode(first);

    Assertions.assertEquals(expected, actual);

    closeProvider(fp);
  }
}
