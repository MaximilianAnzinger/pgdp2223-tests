package pgdp.filter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FrameIteratorTest {

  private static Iterator<Frame> getFrameIteratorFromContainer(VideoContainer con) {
    Class<?> clazz = null;
    Constructor<?> constructor = null;
    Object instance = null;
    try {
      clazz = Class.forName("pgdp.filter.VideoContainer$FrameIterator");
      constructor = clazz.getDeclaredConstructor(VideoContainer.class);
      constructor.setAccessible(true);
      instance = constructor.newInstance(con);
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
             InvocationTargetException e) {
      throw new RuntimeException(e);
    }

    return (Iterator<Frame>) instance;
  }

  private static VideoContainer getContainerFromProvider(FrameProvider p) {
    try {
      return new VideoContainer(p);
    } catch (FileNotFoundException | IllegalVideoFormatException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  @DisplayName("FrameIterator should throw exception when nextFrame() throws")
  public void testFrameIterator_emptyProvider() {
    // Create an empty mock provider
    final var mockProvider = new MockFrameProvider(new BufferedImage[0]) {
      // Overwrite width and height to avoid triggering an exception
      @Override
      public int getWidth() {
        return 42;
      }

      @Override
      public int getHeight() {
        return 42;
      }
    };

    final var container = getContainerFromProvider(mockProvider);
    final var it = FrameIteratorTest.getFrameIteratorFromContainer(container);
    Assertions.assertFalse(it.hasNext());
    Assertions.assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  @DisplayName("FrameIterator should throw exception when nextFrame() returns null")
  public void testFrameIterator_nextFrameNull() {
    // Create an empty mock provider
    final var mockProvider = new MockFrameProvider(new BufferedImage[0]) {
      // Overwrite width and height to avoid triggering an exception
      @Override
      public int getWidth() {
        return 42;
      }

      @Override
      public int getHeight() {
        return 42;
      }

      @Override
      public Frame nextFrame() throws FFmpegFrameGrabber.Exception {
        return null;
      }
    };

    final var container = getContainerFromProvider(mockProvider);
    final var it = FrameIteratorTest.getFrameIteratorFromContainer(container);
    Assertions.assertFalse(it.hasNext());
    Assertions.assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  @DisplayName("hasNext() is called multiple times")
  public void testFrameIterator_multipleHasNextCalls() {
    final var mockProvider = VideoFactory.getSmallSMPTEFeed();

    final var container = getContainerFromProvider(mockProvider);
    final var it = FrameIteratorTest.getFrameIteratorFromContainer(container);

    for (int i = 0; i < mockProvider.getTestFrames().length; i++) {
      Assertions.assertTrue(it.hasNext());
    }

    for (int i = 0; i < mockProvider.getTestFrames().length; i++) {
      Assertions.assertEquals(i, it.next().getFrameNumber());
    }

    Assertions.assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  @DisplayName("next() is called without hasNext()")
  public void testFrameIterator_onlyNext() {
    final var mockProvider = VideoFactory.getSmallSMPTEFeed();

    final var container = getContainerFromProvider(mockProvider);
    final var it = FrameIteratorTest.getFrameIteratorFromContainer(container);

    for (int i = 0; i < mockProvider.getTestFrames().length; i++) {
      Assertions.assertEquals(i, it.next().getFrameNumber());
    }

    Assertions.assertThrows(NoSuchElementException.class, it::next);
  }
}
