package pgdp.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageGenerator {
  // Generate SMPTE color bar pattern (https://en.wikipedia.org/wiki/SMPTE_color_bars)
  public static BufferedImage generateSMTPEColorBars(int width, int height) {
    final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Color values for SMPTE color bars
    final Color[] colors = {
        Color.BLACK,
        Color.BLUE,
        Color.GREEN,
        Color.CYAN,
        Color.RED,
        Color.MAGENTA,
        Color.YELLOW,
        Color.WHITE,
        Color.GRAY,
        Color.DARK_GRAY,
        Color.LIGHT_GRAY,
        Color.BLACK
    };

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        int divisor = width / colors.length;
        int colorIndex = divisor == 0 ? 0 : i / divisor;
        image.setRGB(i, j, colors[colorIndex].getRGB());
      }
    }

    return image;
  }

  public static BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
  }

  public static void compareBufferedImages(BufferedImage expected, BufferedImage actual) {
    if (expected.getWidth() != actual.getWidth() || expected.getHeight() != actual.getHeight()) {
      throw new AssertionError("Dimensions of images do not match. Expected: "
          + expected.getWidth() + "x" + expected.getHeight() + ", actual: "
          + actual.getWidth()+ "x" + actual.getHeight());
    }
    for (int i = 0; i < expected.getWidth(); i++) {
      for (int j = 0; j < expected.getHeight(); j++) {
        if (expected.getRGB(i, j) != actual.getRGB(i, j)) {
          throw new AssertionError("Pixel at (" + i + ", " + j + ") does not match. Expected: "+ expected.getRGB(i, j) + ", actual: " + actual.getRGB(i, j));
        }
      }
    }
  }
}
