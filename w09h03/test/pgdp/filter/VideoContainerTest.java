package pgdp.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

public class VideoContainerTest {

  @Test
  public void testConstructor_fileNotFound() {
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
