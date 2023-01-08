package pgdp.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.function.Function;

public class CropTest {
  @Test
  void testCropExactSize() {
    BufferedImage img = ImageGenerator.generateSMTPEColorBars(8, 6);
    Frame frame = new Frame(img, 0);
    Function<Frame, Frame> cropFunc = Operations.crop(8, 6);

    Frame cropped = cropFunc.apply(frame);

    BufferedImage actual = cropped.getPixels();
    Assertions.assertEquals(img, actual);
  }

  @Test
  void testCropCorrectlyCentered() {
    var img = ImageGenerator.generateSMTPEColorBars(8, 6);
    // Create a subImage with an x offset of '2' (As we have cropped this area)
    var expected = ImageGenerator.deepCopy(img).getSubimage(2, 0, 4, 6);

    Frame frame = new Frame(img, 0);
    Function<Frame, Frame> cropFunc = Operations.crop(4, 6);

    Frame cropped = cropFunc.apply(frame);

    // We have to compare every pixel and not just the raster metadata
    ImageGenerator.compareBufferedImages(expected, cropped.getPixels());
  }

  @Test
  public void testCropHeightBy10Pixel() {
    final int width = 8;
    final int height = 20;
    final int cropHeight = 10;

    var img = ImageGenerator.generateSMTPEColorBars(width, height);
    var expected = ImageGenerator.deepCopy(img).getSubimage(0, 5, width, cropHeight);

    Function<Frame, Frame> cropFunc = Operations.crop(width, cropHeight);
    Frame frame = new Frame(img, 0);
    Frame cropped = cropFunc.apply(frame);

    ImageGenerator.compareBufferedImages(expected, cropped.getPixels());
  }

  @Test
  void testCrop1x1() {
    BufferedImage img = ImageGenerator.generateSMTPEColorBars(480, 480);
    BufferedImage expectedImage = ImageGenerator.deepCopy(img).getSubimage(239, 239, 1, 1);
    Frame testFrame = new Frame(img, 0);

    Function<Frame, Frame> cropFunc = Operations.crop(1, 1);
    Frame croppedFrame = cropFunc.apply(testFrame);
    ImageGenerator.compareBufferedImages(expectedImage, croppedFrame.getPixels());
  }
}
