package pgdp.filter;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

public class GreyscaleTest {
  @Test
  void testGrayscaleBasic() {
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
