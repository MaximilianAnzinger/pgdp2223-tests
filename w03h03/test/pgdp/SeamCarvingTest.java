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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


public class SeamCarvingTest {
    // Change this to whereever you decide to save the png-files
    static final String filePathPrefixOut = "./test/pgdp/out/";

    static final String filePathPrefix = "./test/pgdp/";

    @Nested
    @IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
    class Test_compute_gradient_magnitude {
        @ParameterizedTest
        @MethodSource
        void testing_compute_gradient_magnitude(int v1, int v2, int res) {
            assertEquals((new SeamCarving()).computeGradientMagnitude(v1, v2), res);
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
        void testing_to_gradient_magnitude(int[] image, int[] gradientMagnitude, int width, int height, int[] res) {
            (new SeamCarving()).toGradientMagnitude(image, gradientMagnitude, width, height);
            assertArrayEquals(gradientMagnitude, res);
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
        void testing_combine_magnitude_with_mask(int[] gradientMagnitude, int[] mask, int width, int height, int[] res) {
            (new SeamCarving()).combineMagnitudeWithMask(gradientMagnitude, mask, width, height);
            assertArrayEquals(gradientMagnitude, res);
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
        void testing_build_seams_seamsArray(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height, int[][] res) {
            (new SeamCarving()).buildSeams(seams, seamWeights, gradientMagnitude, width, height);
            assertArrayEquals(seams, res);
        }

        static Stream<Arguments> testing_build_seams_seamsArray() {
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
                            new int[][]{new int[]{0, 0, 0, 1, 1}, new int[]{1, 1, 2, 1, 1}, new int[]{2, 2, 2, 1, 1}, new int[]{3, 3, 3, 4, 4}, new int[]{4, 4, 4, 4, 4}, new int[]{5, 5, 4, 4, 4}}
                    )
            );
        }

        @ParameterizedTest
        @MethodSource
        void testing_build_seams_seamWeights(int[][] seams, long[] seamWeights, int[] gradientMagnitude, int width, int height, long[] res) {
            (new SeamCarving()).buildSeams(seams, seamWeights, gradientMagnitude, width, height);
            assertArrayEquals(seamWeights, res);
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
        void testing_remove_seam(int[] image, int[] seam, int width, int height, int[] res) {
            (new SeamCarving()).removeSeam(seam, image, height, width);
            int[] newImage = new int[25];
            System.arraycopy(image, 0, newImage, 0, 25);
            assertArrayEquals(newImage, res);
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
        void testing_example_image(int[] image, int[] mask, int width, int height, int newWidth, int[] res) {
            int[] shrinkResult = (new SeamCarving()).shrink(image, mask, width, height, newWidth);
            arrayToImage("./exampleTestOutput.png", shrinkResult, newWidth, height);
            outputDiffImage(res, shrinkResult, "./exampleDiff.png", newWidth, height);
            assertArrayEquals(res, shrinkResult);
        }

        static Stream<Arguments> testing_example_image() throws IOException {
            return Stream.of(
                    arguments(
                            imageToArray("./example.png", 876, 534),
                            imageToArray("./mask.png", 876, 534),
                            876,
                            534,
                            875,
                            imageToArray("./expected.png", 875, 534)
                    )
            );
        }

        @ParameterizedTest
        @MethodSource
        void testing_wikipedia_image(int[] image, int[] mask, int width, int height, int newWidth, int[] res) {
            int[] shrinkResult = (new SeamCarving()).shrink(image, mask, width, height, newWidth);
            arrayToImage("./wikipediaTestOutput.png", shrinkResult, newWidth, height);
            outputDiffImage(res, shrinkResult, "./wikipediaDiff.png", newWidth, height);
            assertArrayEquals(res, shrinkResult);
        }

        static Stream<Arguments> testing_wikipedia_image() throws IOException {
            return Stream.of(
                    arguments(
                            imageToArray("./wikipedia.png", 274, 186),
                            // Note: this is just a white png
                            imageToArray("./wikipediaMask.png", 274, 186),
                            274,
                            186,
                            160,
                            imageToArray("./wikipediaExpected.png", 160, 186)
                    )
            );
        }

        // NOTE: The main function in './src/pgdp/image/Main.java' removes alpha values before saving an image
        // Meaning: 0 (which should be completely transparent) will be converted to black (not so transparent)
        // Therefore an image with alpha values like 'tux.png' will be black where it should be transparent
        // See: Main.java, l.107, setRGB(..., BufferedImage.INT_TYPE_RGB
        @ParameterizedTest
        @MethodSource
        void testing_tux_image(int[] image, int[] mask, int width, int height, int newWidth, int[] res) {
            int[] shrinkResult = (new SeamCarving()).shrink(image, mask, width, height, newWidth);
            arrayToImage("./tuxTestOutput.png", shrinkResult, newWidth, height);
            outputDiffImage(res, shrinkResult, "./tuxDiff.png", newWidth, height);
            assertArrayEquals(res, shrinkResult);
        }

        static Stream<Arguments> testing_tux_image() throws IOException {
            int[] mask = new int[612320];
            Arrays.fill(mask, 1);
            return Stream.of(
                    arguments(
                            imageToArray("./tux.png", 712, 860),
                            // Note: this is just a white png
                            mask,
                            712,
                            860,
                            420,
                            imageToArray("./tuxExpected.png", 420, 860)
                    )
            );
        }

        static int[] imageToArray(String filePath, int width, int height) throws IOException {
            BufferedImage in = ImageIO.read(new FileImageInputStream(new File(filePathPrefix + filePath)));
            BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics graphic = image.getGraphics();
            graphic.drawImage(in, 0, 0, null);
            graphic.dispose();
            return image.getRGB(0, 0, width, height, null, 0, width);
        }

        void arrayToImage(String filePath, int[] image, int width, int height) {
            File outDir = new File(filePathPrefixOut);
            BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            output.setRGB(0, 0, width, height, image, 0, width);
            try {
                if (outDir.exists() | outDir.mkdirs()) {
                    ImageIO.write(output, "png", new File(filePathPrefixOut + filePath));
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
        // Red pixels -> pixels differ from expedted output
        void outputDiffImage(int[] imageExpected, int[] imageTest, String diffName, int width, int height) {
            int[] diff = new int[imageExpected.length];
            for (int i = 0; i < imageExpected.length; i++) {
                if (imageExpected[i] == imageTest[i]) {
                    diff[i] = 0;
                } else {
                    diff[i] = 16711680;
                }
            }
            arrayToImage(diffName, diff, width, height);
        }
    }
}
