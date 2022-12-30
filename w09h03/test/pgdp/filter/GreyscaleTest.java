package pgdp.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

public class GreyscaleTest {

  @Test
  void testGrayscale_basic() {
    // Test a single-pixel image
    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    image.setRGB(0, 0, 0xFFFFFF);
    Frame frame = new Frame(image, 0);

    Frame result = Operations.grayscale(frame);

    BufferedImage expected = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    expected.setRGB(0, 0, 0xFFFFFF);
    ImageGenerator.compareBufferedImages(expected, Operations.grayscale(frame).getPixels());
  }


}
