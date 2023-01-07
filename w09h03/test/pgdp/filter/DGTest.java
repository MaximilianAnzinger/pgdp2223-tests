package pgdp.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * HINWEISE:
 * <p>
 * - DAS VIDEO NOOT.MP4 MUSS KORREKT HERUNTERGELADEN UND IM KORREKTEN ORDNER SEIN (wie für das Beispiel in der Main.java)
 * </p> <p>
 * - einige tests erstellen output png und jgp dateien (nach durchlauf der test können diese natürlich auch gelöscht werden)
 * (abschalten mit {@link #outputFiles}, bei test02 darf dies nicht auskommentiert werden!)
 * </p> <p>
 * - die lücken in der testnummerierung dienen nur dazu, nachträglich noch tests einfügen zu können
 */
public class DGTest {
    public static final boolean outputFiles = true;

    private static void outputFrame(String pathname, Frame output, String PNG) throws IOException {
        if (!outputFiles) return;

        File outputfile = new File("DGTest_output/" + pathname);

        if (outputfile.mkdirs()) {
            ImageIO.write(output.getPixels(), PNG, outputfile);
        }
    }

    // OPERATIONS DECODE TESTS

    @Test
    @DisplayName("Test DG 01 Decode Noot first Frame")
    public void test01() throws IOException {
        // read the file
        FrameProvider fp = new FrameProvider("noot.mp4");

        Frame first = fp.nextFrame();
        fp.close();

        outputFrame("test01.jpg", first, "jpg");
        fp.close();

        String shouldBe = """
                We2re no strangers to love
                You know the rules and so do I
                A full commitment's wh""";

        System.out.println(shouldBe);
        String actual = Operations.decode(first);
        Assertions.assertEquals(shouldBe, actual);
    }


    @Test
    @DisplayName("Test DG 02 Decode Noot first 200 Frames")
    public void test02() throws IOException, IllegalVideoFormatException {
        // read the file
        VideoContainer in;
        FrameProvider fp = new FrameProvider("noot.mp4");
        in = new VideoContainer(fp);

        // limitiere Laufzeit
        in.limit(200);

        // Ausschreiben
        try {
            FrameConsumer fc = new FrameConsumer(in.getProvider(), "test02Written.mp4", in.getProvider().getWidth(), in.getProvider().getHeight());
            in.write(fc);
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            System.err.println("Error writing File");
        }

        fp = new FrameProvider("test02Written.mp4");
        in = new VideoContainer(fp);

        Iterator<Frame> itf = in.getFrameStream().iterator();
        StringBuilder builder = new StringBuilder();
        while (itf.hasNext()) {
            Frame f = itf.next();
            builder.append(Operations.decode(f));
        }
        fp.close();

        Assertions.assertEquals(DecodedTextFromVideo.shouldBe, builder.toString());
    }


    @Test
    @DisplayName("Test DG 03 Decode neues BufferedImage 8x8 mit Buchstabe e")
    public void test03() {
        BufferedImage image = yellowImage(8);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 8; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    private static BufferedImage yellowImage(int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                image.setRGB(i, j, Color.YELLOW.getRGB());
            }
        }
        return image;
    }

    @Test
    @DisplayName("Test DG 04 Decode neues BufferedImage 9x9 mit Buchstabe e")
    public void test04() {
        BufferedImage image = yellowImage(9);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 8; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 05 Decode neues BufferedImage 7x7 mit Buchstabe e")
    public void test05() {
        BufferedImage image = yellowImage(7);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 7; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 7; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 06 Decode neues BufferedImage 16x16 mit Buchstabe ee")
    public void test06() {
        BufferedImage image = yellowImage(16);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 8; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
            image.setRGB(8 + i, image.getHeight() - 1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("ee", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < 16; i++) {
            Assertions.assertEquals(colorsE[i - 8], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 16; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 07 Decode neues BufferedImage 18x18 mit Buchstabe ee")
    public void test07() {
        BufferedImage image = yellowImage(18);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 8; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
            image.setRGB(8 + i, image.getHeight() - 1, colorsE[i]);
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("ee", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < 16; i++) {
            Assertions.assertEquals(colorsE[i - 8], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 16; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 08 Decode neues BufferedImage 14x14 mit Buchstabe ee")
    public void test08() {
        BufferedImage image = yellowImage(14);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};
        for (int i = 0; i < 8; i++) {
            image.setRGB(i, image.getHeight() - 1, colorsE[i]);
            if (8 + i < image.getWidth()) {
                image.setRGB(8 + i, image.getHeight() - 1, colorsE[i]);
            }
        }

        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        String output = Operations.decode(frame);

        Assertions.assertEquals("e", output);

        // test frame nicht verändert
        Assertions.assertEquals(width, frame.getWidth());
        Assertions.assertEquals(height, frame.getHeight());
        Assertions.assertEquals(number, frame.getFrameNumber());
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 8; i < 14; i++) {
            Assertions.assertEquals(colorsE[i - 8], frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 16; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, frame.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), frame.getPixels().getRGB(i, j));
            }
        }
    }

    // OPERATIONS ENCODE FRAME TESTS

    @Test
    @DisplayName("Test DG 20 Encode neues BufferedImage 500x500 mit Buchstabe e")
    public void test20() throws IOException {
        BufferedImage image = yellowImage(500);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        outputFrame("test20Output.png", output, "PNG");
        outputFrame("test20Original.png", frame, "PNG");

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 21 Encode neues BufferedImage 500x500 mit Buchstabe e falsche frameNumber")
    public void test21() throws IOException {
        BufferedImage image = yellowImage(500);
        Frame frame = new Frame(image, 1);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());

        outputFrame("test03Output.png", output, "PNG");
        outputFrame("test03Original.png", frame, "PNG");

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 22 Encode firstPixel überschrieben und mit Original text")
    public void test22() throws IOException {
        // read the file
        FrameProvider fp = new FrameProvider("noot.mp4");
        Frame first = fp.nextFrame();

        int[] nums = new int[first.getWidth()];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = first.getPixels().getRGB(i, first.getHeight() - 1);
            if (nums[i] < -9_999_999) { // filtere farbungenauigkeiten und runde auf black and white
                nums[i] = Color.BLACK.getRGB();
            } else {
                nums[i] = Color.WHITE.getRGB();
            }
        }

        for (int i = 0; i < first.getWidth(); i++) {
            for (int j = 0; j < first.getHeight(); j++) {
                first.getPixels().setRGB(i, j, Color.BLUE.getRGB());
            }
        }
        int height = first.getHeight();
        int width = first.getWidth();
        int number = first.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("""
                We2re no strangers to love
                You know the rules and so do I
                A full commitment's wh""");
        Frame output = function.apply(first);

        outputFrame("test04Output.png", output, "PNG");

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < first.getWidth(); i++) {
            Assertions.assertEquals(nums[i], output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        // rest
        System.out.println(Color.BLUE.getRGB());
        for (int i = 0; i < first.getWidth(); i++) {
            for (int j = 0; j < first.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(i, j));
            }
        }

        fp.close();
    }

    @Test
    @DisplayName("Test DG 23 Encode firstPixel überschrieben und mit anderem Text encoded")
    public void test23() throws IOException {
        // read the file
        FrameProvider fp = new FrameProvider("noot.mp4");
        Frame first = fp.nextFrame();

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < first.getWidth(); i++) {
            for (int j = 0; j < first.getHeight(); j++) {
                first.getPixels().setRGB(i, j, Color.BLUE.getRGB());
            }
        }
        int height = first.getHeight();
        int width = first.getWidth();
        int number = first.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(first);

        outputFrame("test05Output.png", output, "PNG");

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        // rest
        System.out.println(Color.BLUE.getRGB());
        for (int i = 0; i < first.getWidth(); i++) {
            for (int j = 0; j < first.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(i, j));
            }
        }

        fp.close();
    }

    @Test
    @DisplayName("Test DG 24 Encode neues BufferedImage 9x9 mit Buchstabe e")
    public void test24() {
        BufferedImage image = yellowImage(9);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 25 Encode neues BufferedImage 8x8 mit Buchstabe e")
    public void test25() {
        BufferedImage image = yellowImage(8);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(frame);
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 26 Encode neues BufferedImage 7x7 mit Buchstabe e")
    public void test26() {
        BufferedImage image = yellowImage(7);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("e");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 7; i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 27 Encode neues BufferedImage 18x18 mit Buchstabe ee")
    public void test27() {
        BufferedImage image = yellowImage(18);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(8 + i, output.getHeight() - 1));
        }
        // rest
        for (int i = 16; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 28 Encode neues BufferedImage 16x16 mit Buchstabe ee")
    public void test28() {
        BufferedImage image = yellowImage(16);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(8 + i, output.getHeight() - 1));
        }
        // rest
        for (int i = 16; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 29 Encode neues BufferedImage 14x14 mit Buchstabe ee")
    public void test29() {
        BufferedImage image = yellowImage(14);
        Frame frame = new Frame(image, 0);

        int height = frame.getHeight();
        int width = frame.getWidth();
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.encode("ee");
        Frame output = function.apply(frame);
        System.out.println(Color.RED.getRGB());
        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        Assertions.assertEquals(width, output.getWidth());
        Assertions.assertEquals(height, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());
        // encoding path
        for (int i = 0; i < 8; i++) {
            Assertions.assertEquals(colorsE[i], output.getPixels().getRGB(i, output.getHeight() - 1), "failed at " + i);
        }
        // rest
        for (int i = 8; i < frame.getWidth(); i++) {
            Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, output.getHeight() - 1));
        }
        for (int i = 0; i < frame.getWidth(); i++) {
            for (int j = 0; j < frame.getHeight() - 1; j++) {
                Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }


    // OPERATIONS ENCODE STREAM TESTS

    @Test
    @DisplayName("Test DG 31 Encode Stream 5 frames 8x8 mit Buchstabe eee")
    public void test31() throws IOException {
        int size = 8;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
                outputFrame("test31OutputFrame" + i + ".png", output[i], "PNG");
            } catch (Exception e) {
                Assertions.fail();
            }
        }


        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 3) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 32 Encode Stream 5 frames 15x15 mit Buchstabe eee")
    public void test32() {
        int size = 15;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 3) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 33 Encode Stream 5 frames 4x4 mit Buchstabe eee")
    public void test33() {
        int size = 4;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 3) {
                // encoding path
                for (int j = 0; j < 4; j++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 34 Encode Stream 5 frames 16x16 mit Buchstabe eee")
    public void test34() {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 2) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    if (i < 1) {
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                    }
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    if (i == 1) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    }
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 35 Encode Stream 5 frames 16x16 mit Buchstabe eeee")
    public void test35() {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 2) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 36 Encode Stream 5 frames 16x16 mit Buchstabe eeeee")
    public void test36() {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 3) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    if (i < 2) {
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                    }
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    if (i == 2) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    }
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 37 Encode Stream 5 frames 16x16 mit Buchstabe eeeeeeeeee vollmachen")
    public void test37() {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeeeeeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            // encoding path
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
            }
            // rest
            for (int j = 0; j < output[i].getWidth(); j++) {
                for (int k = 0; k < output[i].getHeight() - 1; k++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 38 Encode Stream 5 frames 16x16 mit Buchstabe eeeeeeeeeeee overflow")
    public void test38() {
        int size = 16;
        int amount = 5;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeeeeeeeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            // encoding path
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
            }
            // rest
            for (int j = 0; j < output[i].getWidth(); j++) {
                for (int k = 0; k < output[i].getHeight() - 1; k++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 40 Encode Stream 3 frames 16x16 mit Buchstabe eeee")
    public void test40() {
        int size = 16;
        int amount = 3;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 2) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 41 Encode Stream 3 frames 16x16 mit Buchstabe eeeee")
    public void test41() throws IOException {
        int size = 16;
        int amount = 3;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
                outputFrame("test31OutputFrame" + i + ".png", output[i], "PNG");
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            // encoding path
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                if (i < 2) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                }
            }
            // rest
            for (int j = 8; j < output[i].getWidth(); j++) {
                if (i == 2) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                }
            }
            for (int j = 0; j < output[i].getWidth(); j++) {
                for (int k = 0; k < output[i].getHeight() - 1; k++) {
                    Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 42 Encode Stream 500 frames 16x16 mit Buchstabe eeee")
    public void test42() {
        int size = 16;
        int amount = 500;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 2) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                }
                // rest
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 43 Encode Stream 500 frames 16x16 mit Buchstabe eeeee")
    public void test43() {
        int size = 16;
        int amount = 500;
        BufferedImage[] images = new BufferedImage[amount];
        for (int k = 0; k < amount; k++) {
            images[k] = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    images[k].setRGB(i, j, Color.YELLOW.getRGB());
                }
            }
        }


        int height = images[0].getHeight();
        int width = images[0].getWidth();
        Frame[] frames = new Frame[amount];
        int[] numbers = new int[amount];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = new Frame(images[i], i);
            numbers[i] = i;
        }
        Stream<Frame> frameStream = Arrays.stream(frames);

        Function<Frame, Frame> function = Operations.encode("eeeee");

        Object[] outputObject = frameStream.map(function).toArray();
        Assertions.assertEquals(amount, outputObject.length);
        Frame[] output = new Frame[amount];
        for (int i = 0; i < amount; i++) {
            try {
                output[i] = (Frame) outputObject[i];
            } catch (Exception e) {
                Assertions.fail();
            }
        }

        int[] colorsE = new int[]{Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.WHITE.getRGB(),
                Color.BLACK.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB(), Color.BLACK.getRGB(), Color.WHITE.getRGB()};

        for (int i = 0; i < amount; i++) {
            Assertions.assertEquals(width, output[i].getWidth());
            Assertions.assertEquals(height, output[i].getHeight());
            Assertions.assertEquals(numbers[i], output[i].getFrameNumber());
            if (i < 3) {
                // encoding path
                for (int j = 0; j < 8; j++) {
                    Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    if (i < 2) {
                        Assertions.assertEquals(colorsE[j], output[i].getPixels().getRGB(8 + j, output[i].getHeight() - 1));
                    }
                }
                // rest
                for (int j = 8; j < output[i].getWidth(); j++) {
                    if (i == 2) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, output[i].getHeight() - 1));
                    }
                }
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight() - 1; k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k));
                    }
                }
            } else {
                for (int j = 0; j < output[i].getWidth(); j++) {
                    for (int k = 0; k < output[i].getHeight(); k++) {
                        Assertions.assertEquals(Color.YELLOW.getRGB(), output[i].getPixels().getRGB(j, k), "failed at frame " + i + " at index " + j + "," + k);
                    }
                }
            }
        }
    }

    // OPERATIONS CROP TEST

    // beide erweitern

    @Test
    @DisplayName("Test DG 50 Crop x erweitern ungerade, y erweitern ungerade")
    public void test50() throws IOException {
        int size = 3;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 2; j++) {
                image.setRGB(j, i, Color.YELLOW.getRGB());
                imageCopy.setRGB(j, i, Color.YELLOW.getRGB());
            }
            for (int j = size - 2; j < size; j++) {
                image.setRGB(j, i, Color.RED.getRGB());
                imageCopy.setRGB(j, i, Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test50Output.png", output, "PNG");

        Assertions.assertEquals(0, Color.BLACK.getRed());
        Assertions.assertEquals(0, Color.BLACK.getGreen());
        Assertions.assertEquals(0, Color.BLACK.getBlue());

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken darüber
        int endOben = 1;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < endOben; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for (int i = 0; i < newWidth; i++) {
            for (int j = endOben; j < endMitte; j++) {
                if (i >= 1 && i <= 3) {
                    Assertions.assertEquals(imageCopy.getRGB(i - 1, j - 1), output.getPixels().getRGB(i, j));
                } else {
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
                }
            }
        }
        // balken danach
        for (int i = 0; i < newWidth; i++) {
            for (int j = endMitte; j < newHeight; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }

        if (!outputFiles) return;
        File outputfile = new File("test50Original.png");
        outputfile.deleteOnExit();
        ImageIO.write(imageCopy, "PNG", outputfile);
    }

    @Test
    @DisplayName("Test DG 51 Crop x erweitern gerade, y erweitern gerade")
    public void test51() throws IOException {
        int size = 2;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                image.setRGB(j, i, Color.YELLOW.getRGB());
                imageCopy.setRGB(j, i, Color.YELLOW.getRGB());
            }
            for (int j = size - 1; j < size; j++) {
                image.setRGB(j, i, Color.RED.getRGB());
                imageCopy.setRGB(j, i, Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test51Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken darüber
        int endOben = 2;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < endOben; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for (int i = 0; i < newWidth; i++) {
            for (int j = endOben; j < endMitte; j++) {
                if (i >= 2 && i <= 3) {
                    Assertions.assertEquals(imageCopy.getRGB(i - 2, j - 2), output.getPixels().getRGB(i, j), "failed at " + i + "," + j);
                } else {
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
                }
            }
        }
        // balken danach
        for (int i = 0; i < newWidth; i++) {
            for (int j = endMitte; j < newHeight; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }

    }

    @Test
    @DisplayName("Test DG 52 Crop x erweitern ungerade, y erweitern gerade")
    public void test52() throws IOException {
        int sizeX = 3;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX - 2; j++) {
                image.setRGB(j, i, Color.YELLOW.getRGB());
                imageCopy.setRGB(j, i, Color.YELLOW.getRGB());
            }
            for (int j = sizeX - 2; j < sizeX; j++) {
                image.setRGB(j, i, Color.RED.getRGB());
                imageCopy.setRGB(j, i, Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test52Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken darüber
        int endOben = 2;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < endOben; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for (int i = 0; i < newWidth; i++) {
            for (int j = endOben; j < endMitte; j++) {
                if (i >= 1 && i <= 3) {
                    Assertions.assertEquals(imageCopy.getRGB(i - 1, j - 2), output.getPixels().getRGB(i, j));
                } else {
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
                }
            }
        }
        // balken danach
        for (int i = 0; i < newWidth; i++) {
            for (int j = endMitte; j < newHeight; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 53 Crop x erweitern gerade, y erweitern ungerade")
    public void test53() throws IOException {
        int sizeX = 2;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX - 1; j++) {
                image.setRGB(j, i, Color.YELLOW.getRGB());
                imageCopy.setRGB(j, i, Color.YELLOW.getRGB());
            }
            for (int j = sizeX - 1; j < sizeX; j++) {
                image.setRGB(j, i, Color.RED.getRGB());
                imageCopy.setRGB(j, i, Color.RED.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int newHeight = 6;
        int newWidth = 6;
        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test53Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        // balken darüber
        int endOben = 1;
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < endOben; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
        // balken mitte
        int endMitte = 4;
        for (int i = 0; i < newWidth; i++) {
            for (int j = endOben; j < endMitte; j++) {
                if (i >= 2 && i <= 3) {
                    Assertions.assertEquals(imageCopy.getRGB(i - 2, j - 1), output.getPixels().getRGB(i, j));
                } else {
                    Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
                }
            }
        }
        // balken danach
        for (int i = 0; i < newWidth; i++) {
            for (int j = endMitte; j < newHeight; j++) {
                Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    // beide kürzen

    @Test
    @DisplayName("Test DG 54 Crop x kürzen ungerade, y kürzen ungerade")
    public void test54() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 3;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 3) {
                    if (j < 3) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 3) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test54Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 2));
    }

    @Test
    @DisplayName("Test DG 55 Crop x kürzen gerade, y kürzen gerade")
    public void test55() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 2;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 3) {
                    if (j < 3) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 3) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test55Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 0));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 1));
    }

    @Test
    @DisplayName("Test DG 56 Crop x kürzen ungerade, y kürzen gerade")
    public void test56() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 3;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 3) {
                    if (j < 3) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 3) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test56Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 1));
    }

    @Test
    @DisplayName("Test DG 57 Crop x kürzen gerade, y kürzen ungerade")
    public void test57() throws IOException {
        int sizeX = 6;
        int sizeY = 6;
        int newWidth = 2;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 3) {
                    if (j < 3) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 3) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test57Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 2));
    }

    // x kürzen, y erweitern

    @Test
    @DisplayName("Test DG 58 Crop x kürzen ungerade, y erweitern ungerade")
    public void test58() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 2;
        int newHeight = 5;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 2) {
                    if (j < 1) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 1) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test58Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 3));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 4));
    }

    @Test
    @DisplayName("Test DG 59 Crop x kürzen gerade, y erweitern gerade")
    public void test59() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 3;
        int newHeight = 4;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 2) {
                    if (j < 1) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 1) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test59Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2, 3));
    }

    @Test
    @DisplayName("Test DG 60 Crop x kürzen ungerade, y erweitern gerade")
    public void test60() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 2;
        int newHeight = 4;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 2) {
                    if (j < 1) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 1) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test60Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 3));
    }

    @Test
    @DisplayName("Test DG 61 Crop x kürzen gerade, y erweitern ungerade")
    public void test61() throws IOException {
        int sizeX = 5;
        int sizeY = 2;
        int newWidth = 3;
        int newHeight = 5;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 2) {
                    if (j < 1) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 1) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test61Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2, 0));

        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 1));

        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(1, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 2));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 3));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2, 3));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(1, 4));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(2, 4));
    }

    // x erweitern, y kürzen

    @Test
    @DisplayName("Test DG 62 Crop x erweitern ungerade, y kürzen ungerade")
    public void test62() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 5;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 1) {
                    if (j < 2) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 2) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test62Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4, 0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4, 1));
    }

    @Test
    @DisplayName("Test DG 63 Crop x erweitern gerade, y kürzen gerade")
    public void test63() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 4;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 1) {
                    if (j < 2) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 2) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test63Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 1));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 2));
    }

    @Test
    @DisplayName("Test DG 64 Crop x erweitern ungerade, y kürzen gerade")
    public void test64() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 5;
        int newHeight = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 1) {
                    if (j < 2) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 2) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test64Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4, 0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4, 1));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 2));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 2));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 2));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(4, 2));
    }

    @Test
    @DisplayName("Test DG 65 Crop x erweitern gerade, y kürzen ungerade")
    public void test65() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        int newWidth = 4;
        int newHeight = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        BufferedImage imageCopy = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (i < 1) {
                    if (j < 2) {
                        // links oben
                        image.setRGB(i, j, Color.RED.getRGB());
                        imageCopy.setRGB(i, j, Color.RED.getRGB());
                    } else {
                        // links unten
                        image.setRGB(i, j, Color.YELLOW.getRGB());
                        imageCopy.setRGB(i, j, Color.YELLOW.getRGB());
                    }
                } else {
                    if (j < 2) {
                        // rechts oben
                        image.setRGB(i, j, Color.BLUE.getRGB());
                        imageCopy.setRGB(i, j, Color.BLUE.getRGB());
                    } else {
                        // rechts unten
                        image.setRGB(i, j, Color.GREEN.getRGB());
                        imageCopy.setRGB(i, j, Color.GREEN.getRGB());
                    }
                }
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Function<Frame, Frame> function = Operations.crop(newWidth, newHeight);
        Frame output = function.apply(frame);

        outputFrame("test65Output.png", output, "PNG");

        Assertions.assertEquals(newWidth, output.getWidth());
        Assertions.assertEquals(newHeight, output.getHeight());
        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(Color.RED.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(Color.BLUE.getRGB(), output.getPixels().getRGB(2, 0));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 0));

        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(Color.YELLOW.getRGB(), output.getPixels().getRGB(1, 1));
        Assertions.assertEquals(Color.GREEN.getRGB(), output.getPixels().getRGB(2, 1));
        Assertions.assertEquals(Color.BLACK.getRGB(), output.getPixels().getRGB(3, 1));
    }
    // OPERATIONS GRAYSCALE TESTS

    @Test
    @DisplayName("Test DG 80 GrayScale Frame 3x3 yellow")
    public void test80() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.YELLOW.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test80Output.png", output, "PNG");

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(225, 225, 225);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 81 GrayScale Frame 10x1 pink")
    public void test81() throws IOException {
        int sizeX = 10;
        int sizeY = 1;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.PINK.getRGB());
            }
        }
        Frame frame = new Frame(image, 5);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test81Output.png", output, "PNG");

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(198, 198, 198);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 82 GrayScale Frame 2x5 black")
    public void test82() throws IOException {
        int sizeX = 2;
        int sizeY = 5;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        Frame frame = new Frame(image, 5);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test82Output.png", output, "PNG");

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(0, 0, 0);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 83 GrayScale Frame 3x3 white")
    public void test83() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.WHITE.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test83Output.png", output, "PNG");

        Assertions.assertEquals(number, output.getFrameNumber());

        Color gray = new Color(255, 255, 255);

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 84 GrayScale Frame 3x3 darkgray")
    public void test84() throws IOException {
        int sizeX = 3;
        int sizeY = 3;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test84Output.png", output, "PNG");

        Color gray = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 85 GrayScale Frame 1x1 darkgray")
    public void test85() throws IOException {
        int sizeX = 1;
        int sizeY = 1;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test85Output.png", output, "PNG");

        Color gray = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 86 GrayScale Frame 100x100 darkgray")
    public void test86() throws IOException {
        int sizeX = 100;
        int sizeY = 100;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test86Output.png", output, "PNG");

        Color gray = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 87 GrayScale Frame 2x50 darkgray")
    public void test87() throws IOException {
        int sizeX = 2;
        int sizeY = 50;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test87Output.png", output, "PNG");

        Color gray = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 88 GrayScale Frame 50x2 darkgray")
    public void test88() throws IOException {
        int sizeX = 50;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                image.setRGB(i, j, Color.DARK_GRAY.getRGB());
            }
        }
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test88Output.png", output, "PNG");

        Color gray = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                Assertions.assertEquals(gray.getRGB(), output.getPixels().getRGB(i, j));
            }
        }
    }

    @Test
    @DisplayName("Test DG 89 GrayScale Frame 2x2 mixed")
    public void test89() throws IOException {
        int sizeX = 2;
        int sizeY = 2;
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, Color.YELLOW.getRGB());
        image.setRGB(1, 0, Color.WHITE.getRGB());
        image.setRGB(0, 1, Color.PINK.getRGB());
        image.setRGB(1, 1, Color.DARK_GRAY.getRGB());
        Frame frame = new Frame(image, 0);

        int number = frame.getFrameNumber();

        Frame output = Operations.grayscale(frame);

        outputFrame("test89Output.png", output, "PNG");

        Color gray1 = new Color(225, 225, 225);
        Color gray2 = new Color(255, 255, 255);
        Color gray3 = new Color(198, 198, 198);
        Color gray4 = new Color(63, 63, 63);

        Assertions.assertEquals(number, output.getFrameNumber());

        Assertions.assertEquals(gray1.getRGB(), output.getPixels().getRGB(0, 0));
        Assertions.assertEquals(gray2.getRGB(), output.getPixels().getRGB(1, 0));
        Assertions.assertEquals(gray3.getRGB(), output.getPixels().getRGB(0, 1));
        Assertions.assertEquals(gray4.getRGB(), output.getPixels().getRGB(1, 1));
    }

    // VIDEOCONTAINER KONSTRUKTOR TESTS

    @Test
    @DisplayName("Test DG 100 VideoContainer Konstruktor Noot")
    public void test100() {
        try {
            new FrameProvider("noot.mp4");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 101 VideoContainer Konstruktor Noot2")
    public void test101() {
        try {
            new FrameProvider("noot2.mp4");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 102 VideoContainer Konstruktor width 0")
    public void test102() {
        MyBufferedImageWidthZero image = new MyBufferedImageWidthZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 103 VideoContainer Konstruktor width negative")
    public void test103() {
        MyBufferedImageWidthNegative image = new MyBufferedImageWidthNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 104 VideoContainer Konstruktor height 0")
    public void test104() {
        MyBufferedImageHeightZero image = new MyBufferedImageHeightZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 105 VideoContainer Konstruktor height negative")
    public void test105() {
        MyBufferedImageHeightNegative image = new MyBufferedImageHeightNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 106 VideoContainer Konstruktor both 0")
    public void test106() {
        MyBufferedImageBothZero image = new MyBufferedImageBothZero(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    @Test
    @DisplayName("Test DG 107 VideoContainer Konstruktor both negative")
    public void test107() {
        MyBufferedImageBothNegative image = new MyBufferedImageBothNegative(3, 3, BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                image.setRGB(i, j, Color.RED.getRGB());
            }
        }
        MyFrameProvider provider = new MyFrameProvider(image);
        try {
            new VideoContainer(provider);
        } catch (IllegalVideoFormatException ivfe) {
            System.out.println("correct exception thrown!");
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    // VIDEOCONTAINER LIMIT TESTS

    @Test
    @DisplayName("Test DG 120 VideoContainer Limit noot auf 100 Frames")
    public void test120() throws FileNotFoundException, IllegalVideoFormatException {
        int length = 100;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.getFrameStream().limit(length).toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.getFrameStream().toArray();

        Assertions.assertEquals(output.length, length);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < fp.getWidth(); j++) {
                for (int k = 0; k < fp.getHeight(); k++) {
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j, k), ((Frame) output[i]).getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 121 VideoContainer Limit noot auf 10 Frames")
    public void test121() throws FileNotFoundException, IllegalVideoFormatException {
        int length = 10;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.getFrameStream().limit(length).toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.getFrameStream().toArray();

        Assertions.assertEquals(output.length, length);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < fp.getWidth(); j++) {
                for (int k = 0; k < fp.getHeight(); k++) {
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j, k), ((Frame) output[i]).getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 122 VideoContainer Limit noot auf 2 Frames")
    public void test122() throws FileNotFoundException, IllegalVideoFormatException {
        int length = 2;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.getFrameStream().limit(length).toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.getFrameStream().toArray();

        Assertions.assertEquals(output.length, length);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < fp.getWidth(); j++) {
                for (int k = 0; k < fp.getHeight(); k++) {
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j, k), ((Frame) output[i]).getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 123 VideoContainer Limit noot auf 1 Frames")
    public void test123() throws FileNotFoundException, IllegalVideoFormatException {
        int length = 1;

        FrameProvider fp = new FrameProvider("noot.mp4");
        FrameProvider fp1 = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);
        VideoContainer in1 = new VideoContainer(fp1);

        Object[] array = in1.getFrameStream().limit(length).toArray();

        // limitiere Laufzeit
        in.limit(length);

        Object[] output = in.getFrameStream().toArray();

        Assertions.assertEquals(output.length, length);

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < fp.getWidth(); j++) {
                for (int k = 0; k < fp.getHeight(); k++) {
                    Assertions.assertEquals(((Frame) array[i]).getPixels().getRGB(j, k), ((Frame) output[i]).getPixels().getRGB(j, k));
                }
            }
        }
    }

    @Test
    @DisplayName("Test DG 124 VideoContainer Limit noot auf 0 Frames")
    public void test124() throws FileNotFoundException, IllegalVideoFormatException {
        int length = 0;

        FrameProvider fp = new FrameProvider("noot.mp4");
        VideoContainer in = new VideoContainer(fp);

        // limitiere Laufzeit
        try {
            in.limit(length);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        Object[] output = in.getFrameStream().toArray();

        Assertions.assertEquals(output.length, length);
    }
}

class MyFrameProvider extends FrameProvider {

    private final BufferedImage image;
    private int frameCount;

    public MyFrameProvider(BufferedImage image) {
        this.image = image;
        this.frameCount = 0;
    }

    public boolean fileExists() {
        return true;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public double getFrameRate() {
        return 1;
    }

    public int getBitrate() {
        return 1;
    }

    public Frame nextFrame() {
        if (frameCount >= 1) {
            return null;
        } else {
            return new Frame(deepCopy(image), frameCount++);
        }
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}

class MyBufferedImageHeightZero extends BufferedImage {
    public MyBufferedImageHeightZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getHeight() {
        return 0;
    }
}

class MyBufferedImageHeightNegative extends BufferedImage {
    public MyBufferedImageHeightNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getHeight() {
        return -1;
    }
}

class MyBufferedImageWidthZero extends BufferedImage {
    public MyBufferedImageWidthZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getWidth() {
        return 0;
    }
}

class MyBufferedImageWidthNegative extends BufferedImage {
    public MyBufferedImageWidthNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getWidth() {
        return -1;
    }
}

class MyBufferedImageBothZero extends BufferedImage {
    public MyBufferedImageBothZero(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}

class MyBufferedImageBothNegative extends BufferedImage {
    public MyBufferedImageBothNegative(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public int getWidth() {
        return -1;
    }

    @Override
    public int getHeight() {
        return -1;
    }
}
