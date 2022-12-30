package pgdp.filter;

import org.bytedeco.javacv.FFmpegFrameGrabber;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class MockFrameProvider extends FrameProvider {
  private final BufferedImage[] testFrames;
  private int currentFrame;

  public MockFrameProvider(BufferedImage[] testFrames) {
    Objects.requireNonNull(testFrames);

    this.testFrames = testFrames;
    currentFrame = 0;
  }

  @Override
  public boolean fileExists() {
    return true;
  }

  @Override
  public int getWidth() {
    return testFrames[0].getWidth();
  }

  @Override
  public int getHeight() {
    return testFrames[0].getHeight();
  }

  @Override
  public double getFrameRate() {
    return 0;
  }

  @Override
  public int getBitrate() {
    return 0;
  }

  @Override
  public Frame nextFrame() throws FFmpegFrameGrabber.Exception {
    BufferedImage currentImage = testFrames[currentFrame];

    Frame frame = new Frame(currentImage, currentFrame);
    currentFrame++;
    if (currentFrame >= testFrames.length) {
      throw new FFmpegFrameGrabber.Exception("Reached the end of the video.");
    }

    return frame;
  }

  @Override
  public void close() {
    // Do nothing
  }
}
