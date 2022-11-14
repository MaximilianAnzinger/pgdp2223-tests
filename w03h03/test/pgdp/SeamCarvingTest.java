// File: ./src/pgdp/tests/SeamCarvingTest.java
// If you put this file in the same location as I did, you'll have to change the test dir in the build.gradle
// (add missing dependencies for junit in build.gradle)
// run 'gradle test'
// Remember to change the filePathPrefix to the location relative to the project root, where you saved the images
// Use 'package pgdp.tests' if your tests are in ./src/tests/
package pgdp;

import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.image.SeamCarving;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class SeamCarvingTest {
    // Change this to wherever you decide to save the png-files
    static final String PATH_OUT = "./test/out/";

    static final String PATH_TEST_RESOURCES = "./test/resources/test/";

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_compute_gradient_magnitude {
        @ParameterizedTest
        @MethodSource
        void testing_compute_gradient_magnitude(int v1, int v2, int expected) {
            assertEquals(expected, new SeamCarving().computeGradientMagnitude(v1, v2));
        }

        static Stream<Arguments> testing_compute_gradient_magnitude() {
            return Stream.of(
                    arguments(0, 0, 0),
                    arguments(1, 2, 1),
                    arguments(16777215, 0, 195075),
                    arguments(-1, 0, 195075),
                    arguments(2066367881, 18428516, 2019),
                    arguments(1337, 420, 11465),
                    arguments(6669420, 80085, 40625),
                    arguments(-4206669, -1337_83745, 49985)
            );
        }
    }

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_to_gradient_magnitude {
        @ParameterizedTest
        @MethodSource
        void testing_to_gradient_magnitude(int[] image, int[] gradientMagnitude, int width, int height, int[] expected) {
            new SeamCarving().toGradientMagnitude(image, gradientMagnitude, width, height);
            assertArrayEquals(expected, gradientMagnitude);
        }

        static Stream<Arguments> testing_to_gradient_magnitude() {
            return Stream.of(
                    // Fun times calculating this by hand
                    arguments(
                            new int[]{0, 1, 2, 3, 4, 5,
                                    6, 7, 8, 9, 10, 11,
                                    12, 13, 14, 15, 16, 17,
                                    18, 19, 20, 21, 22, 23},
                            new int[24],
                            6,
                            4,
                            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, 148, 148, 148, 148, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, 148, 148, 148, 148, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,}),
                    arguments(
                            new int[]{2, -3, -5, 7, -11, 13,
                                    -17, 19, 23, 29, -31, 37,
                                    -41, 43, 47, 53, -59, 61,
                                    -67, 71, 73, 79, -83, 97},
                            new int[24],
                            6,
                            4,
                            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, 350856, 171766, 172970, 2368, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, 160978, 2600, 155050, 2768, Integer.MAX_VALUE,
                                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,})
            );
        }
    }

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_combine_magnitude_with_mask {
        @ParameterizedTest
        @MethodSource
        void testing_combine_magnitude_with_mask(int[] gradientMagnitude, int[] mask, int width, int height, int[] expected) {
            new SeamCarving().combineMagnitudeWithMask(gradientMagnitude, mask, width, height);
            assertArrayEquals(expected, gradientMagnitude);
        }

        static Stream<Arguments> testing_combine_magnitude_with_mask() {
            return Stream.of(
                    arguments(
                            new int[]{2, -3, -5, 7, -11, 13,
                                    -17, 19, 23, 29, -31, 37,
                                    -41, 43, 47, 53, -59, 61,
                                    -67, 71, 73, 79, -83, 97},
                            new int[]{0, 1, 2, 3, 4, 5,
                                    6, 7, 2063597568, 0, 1, 1,
                                    0, -16777216, 0, 0, 0, 0,
                                    9, 9, 9, 9, 9, 9},
                            6,
                            4,
                            new int[]{Integer.MAX_VALUE, -3, -5, 7, -11, 13,
                                    -17, 19, Integer.MAX_VALUE, Integer.MAX_VALUE, -31, 37,
                                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                                    -67, 71, 73, 79, -83, 97}),
                    arguments(
                            new int[]{2, -3, -5, 7, -11, 13,
                                    -17, 19, 23, 29, -31, 37,
                                    -41, 43, 47, 53, -59, 61,
                                    -67, 71, 73, 79, -83, 97},
                            new int[]{1, 1, 1, 1, 1, 1,
                                    1, 1, 1, 1, 1, 1,
                                    1, 1, 1, 1, 1, 1,
                                    1, 1, 1, 1, 1, 1},
                            6,
                            4,
                            new int[]{2, -3, -5, 7, -11, 13,
                                    -17, 19, 23, 29, -31, 37,
                                    -41, 43, 47, 53, -59, 61,
                                    -67, 71, 73, 79, -83, 97})
            );
        }
    }

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_build_seam {
        @ParameterizedTest
        @MethodSource
        void testing_build_seams_seamsArray(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height, int[][] expected) {
            new SeamCarving().buildSeams(seams, seamWeights, gradientMagnitude, width, height);
            assertArrayEquals(expected, seams);
        }

        static Stream<Arguments> testing_build_seams_seamsArray() {
            return Stream.of(
                arguments(
                    new int[6][5],
                    new long[6],
                    new int[] {
                        Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        1, 1, 1, 1, 1, 1,
                        69, 420, 42, 42, 42, 1337,
                        100, 10, 666, 20, 3, 10,
                        Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE
                    },
                    6,
                    5,
                    new int[][] {
                        {0, 0, 0, 1, 1},
                        {1, 1, 2, 1, 1},
                        {2, 2, 2, 1, 1},
                        {3, 3, 3, 4, 4},
                        {4, 4, 4, 4, 4},
                        {5, 5, 4, 4, 4}
                    }
                ),
                arguments(
                    new int[2][2],
                    new long[2],
                    new int[] {
                        Integer.MAX_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE,
                    },
                    2,
                    2,
                    new int[][] {
                        {0, 0},
                        {1, 1}
                    }
                )
            );
        }

        @ParameterizedTest
        @MethodSource
        void testing_build_seams_seamWeights(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height, long[] expected) {
            new SeamCarving().buildSeams(seams, seamWeights, gradientMagnitude, width, height);
            assertArrayEquals(expected, seamWeights);
        }

        static Stream<Arguments> testing_build_seams_seamWeights() {
            return Stream.of(
                    arguments(
                            new int[6][5],
                            new long[6],
                            new int[]{
                                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                                    1, 1, 1, 1, 1, 1,
                                    69, 420, 42, 42, 42, 1337,
                                    100, 10, 666, 20, 3, 10,
                                    Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE
                            },
                            6,
                            5,
                            new long[]{4294967374L, 4294967347L, 4294967347L, 4294967340L, 4294967340L, 4294967340L}
                    )
            );
        }
    }

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_remove_seam {
        @ParameterizedTest
        @MethodSource
        void testing_remove_seam(int[] image, int[] seam, int width, int height, int[] expected) {
            new SeamCarving().removeSeam(seam, image, height, width);
            int[] newImage = new int[25];
            System.arraycopy(image, 0, newImage, 0, 25);
            assertArrayEquals(expected, newImage);
        }

        static Stream<Arguments> testing_remove_seam() {
            return Stream.of(
                    arguments(
                            new int[]{
                                    0, 1, 2, 3, 4, 5,
                                    6, 7, 8, 9, 10, 11,
                                    12, 13, 14, 15, 16, 17,
                                    18, 19, 20, 21, 22, 23,
                                    24, 25, 26, 27, 28, 29
                            },
                            new int[]{3, 3, 3, 4, 4},
                            6,
                            5,
                            new int[]{
                                    0, 1, 2, 4, 5,
                                    6, 7, 8, 10, 11,
                                    12, 13, 14, 16, 17,
                                    18, 19, 20, 21, 23,
                                    24, 25, 26, 27, 29
                            }
                    )
            );
        }
    }

    // Except for the example.png your results may differ from mine
    // Who is right? That's up to you to decide
    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_shrink {
        @ParameterizedTest
        @MethodSource
        void testing_image(int[] input, int[] mask, int width, int height, int newWidth, int[] expected, String name) {
            int[] shrinkResult = new SeamCarving().shrink(input, mask, width, height, newWidth);
            saveImage("./" + name + "_test-output.png", shrinkResult, newWidth, height);

            outputDiffImage(expected, shrinkResult, "./" + name + "_test-diff.png", newWidth, height);
            assertArrayEquals(expected, shrinkResult);
        }

        static Stream<Arguments> testing_image() {

            Map<String, int[]> images = new Hashtable<>();
            images.put("wikipedia", new int[]{274, 186, 160});
            images.put("tux", new int[]{712, 860, 420});
            images.put("test-noise", new int[]{92, 66, 91});
            images.put("test-noise-seam", new int[]{92, 66, 91});

            Map<String, int[]> imagesMasked = new Hashtable<>();
            imagesMasked.put("example", new int[]{876, 534, 875});
            imagesMasked.put("test-noise-seam", new int[]{92, 66, 91});
            imagesMasked.put("test-noise", new int[]{92, 66, 80});

            return Stream.concat(images.keySet().stream().map(s -> {
                try {
                    int width = images.get(s)[0];
                    int height = images.get(s)[1];
                    int newWidth = images.get(s)[2];

                    int[] mask = new int[width * height];
                    Arrays.fill(mask, -1);

                    return arguments(
                            imageToArray("./" + s + ".png", width, height),
                            mask,
                            width,
                            height,
                            newWidth,
                            imageToArray("./" + s + "_expected.png", newWidth, height),
                            s
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }), imagesMasked.keySet().stream().map(s -> {
                try {
                    int width = imagesMasked.get(s)[0];
                    int height = imagesMasked.get(s)[1];
                    int newWidth = imagesMasked.get(s)[2];

                    int[] mask = imageToArray("./" + s + "_mask.png", width, height);

                    return arguments(
                            imageToArray("./" + s + ".png", width, height),
                            mask,
                            width,
                            height,
                            newWidth,
                            imageToArray("./" + s + "_masked-expected.png", newWidth, height),
                            s + "-masked"
                    );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
        }

        static void removeAlpha(int[] img) {
            for (int i = 0; i < img.length; i++) {
                img[i] = img[i] & 0xFFFFFF;
            }
        }

        static int[] imageToArray(String filePath, int width, int height) throws IOException {
            BufferedImage inputImage = ImageIO.read(new File(PATH_TEST_RESOURCES + filePath));
            int[] input = inputImage.getRGB(0, 0, width, height, null, 0, width);
            removeAlpha(input);

            return input;
        }

        void saveImage(String filePath, int[] image, int width, int height) {
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            output.setRGB(0, 0, width, height, image, 0, width);
            saveImage(filePath, output);
        }

        void saveImage(String filePath, BufferedImage image) {
            File outDir = new File(PATH_OUT);
            try {
                if (outDir.exists() || outDir.mkdirs()) {
                    ImageIO.write(image, "png", new File(PATH_OUT + filePath));
                } else {
                    throw new IOException("Could not create directories!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Outputs a file showcasing the differences between the expected output (example/mine) and the tested function
        // This diff is primitive, so it doesn't reveal part of the solution
        // Black pixels -> pixels in expected match the tested output
        // Red pixels -> pixels differ from expected output
        void outputDiffImage(int[] imageExpected, int[] imageTest, String diffName, int width, int height) {

            BufferedImage expected = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            expected.setRGB(0, 0, width, height, imageExpected, 0, width);

            BufferedImage actual = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            actual.setRGB(0, 0, width, height, imageTest, 0, width);

            BufferedImage diff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {

                    Color c = new Color(expected.getRGB(x, y));
                    float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                    int desaturated = Color.HSBtoRGB(hsb[0], hsb[1] / 5f, hsb[2]);
                    if (expected.getRGB(x, y) == actual.getRGB(x, y)) {

                        diff.setRGB(x, y, desaturated);
                    } else {
                        c = new Color(desaturated);
                        int r = (c.getRed() + 255 * 5) / 6;
                        int g = (c.getRed()) / 6;
                        int b = (c.getRed()) / 6;
                        diff.setRGB(x, y, new Color(r, g, b).getRGB());
                    }


                }
            }

            saveImage(diffName, diff);
        }
    }
}
