package pgdp.filter;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VideoContainerTest {

  @Test
  public void testConstructorFileNotFound() {
    // Create an invalid frameProvider
    FrameProvider fp = new MockFrameProvider(new BufferedImage[0]) {
      @Override
      public boolean fileExists() {
        return false;
      }
    };

    Assertions.assertThrows(FileNotFoundException.class, () -> new VideoContainer(fp));
  }
}
