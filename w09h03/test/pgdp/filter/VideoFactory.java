package pgdp.filter;

import java.awt.image.BufferedImage;

public class VideoFactory {
  private static final BufferedImage smpte_src;

  static {
    // Generate smpte color bars only once
    smpte_src = ImageGenerator.generateSMTPEColorBars(720, 420);
  }

  // Create a new MockFrameProvider with two SMPTE frames
  public static MockFrameProvider getSmallSMPTEFeed() {
    // BufferedImage array with 2 frames
    final var smpte_array_2f = new BufferedImage[] {
        // A deep copy is required for image manipulation
        ImageGenerator.deepCopy(smpte_src),
        ImageGenerator.deepCopy(smpte_src),
    };

    return new MockFrameProvider(smpte_array_2f);
  }
}