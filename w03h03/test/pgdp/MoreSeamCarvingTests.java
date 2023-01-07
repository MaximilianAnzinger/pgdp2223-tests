package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pgdp.image.SeamCarving;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MoreSeamCarvingTests {

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST computeGradientMagnitude()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void computeGradientMagnitudeBlackAndWhitePixel() {
        var sc = new SeamCarving();
        int black = 0xffffff;
        int white = 0x0;
        assertEquals(195075, sc.computeGradientMagnitude(black, white));
    }

    @Test
    void computeGradientMagnitudeWhiteAndRedPixel() {
        var sc = new SeamCarving();
        int red = 0x0000ff;
        int white = 0x0;
        assertEquals(65025, sc.computeGradientMagnitude(white, red));
    }

    @Test
    void computeGradientMagnitudeWithAlpha() {
        var sc = new SeamCarving();
        int red = 0x840000ff;
        int evolut1on_orange = 0xffff8b00;
        assertEquals(149371, sc.computeGradientMagnitude(evolut1on_orange, red));
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST toGradientMagnitude()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void toGradientMagnitude3x3Image() {
        int[] image1 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0x0, 0x12bbff37, 0x2800ed,
                0xffff0064, 0xff7f24, 0xffffffff,
                0x49b8ff, 0x46ff0064, 0x00ff2424,
        };
        int width = 3;
        int height = 3;
        int[] gradientMagnitudesOfFlatImage = new int[width * height];
        var seamcarving = new SeamCarving();
        seamcarving.toGradientMagnitude(image1, gradientMagnitudesOfFlatImage, width, height);

        int[] image1_result = new int[]{
                2147483647, 2147483647, 2147483647,
                2147483647, 160724, 2147483647,
                2147483647, 2147483647, 2147483647
        };
        assertArrayEquals(image1_result, gradientMagnitudesOfFlatImage);
    }

    @Test
    /*
        TODO check manually whether the result is actually correct; this currently just tests whether image2 gives the
         output expected assuming that the method is correct in its current state
         (where it correctly works with toGradientMagnitude3x3Image), basically to see whether changing the method
         changes the output
     */
    void toGradientMagnitude5x6Image() {
        int[] image2 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0xffffff, 0x780000, 0x000, 0x0, 0xfffffff,
                0xffffffff, 0x0, 0x23ff1291, 0x008200, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xf6dff9f, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0xda80ff, 0x0,
                0xffffffff, 0xdeffffff, 0x00, 0xffffff, 0x56912ff,
                0x01ffffff, 0x0, 0xffffff, 0x80caff, 0xfffffff
        };
        int width = 5;
        int height = 6;
        int[] gradientMagnitudesOfFlatImage = new int[width * height];
        var seamcarving = new SeamCarving();
        seamcarving.toGradientMagnitude(image2, gradientMagnitudesOfFlatImage, width, height);

        int[] image2_result = new int[]{
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647,
                2147483647, 192379, 119087, 243044, 2147483647,
                2147483647, 118757, 60630, 143085, 2147483647,
                2147483647, 106934, 151917, 76481, 2147483647,
                2147483647, 283300, 72146, 89950, 2147483647,
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647
        };
        assertArrayEquals(image2_result, gradientMagnitudesOfFlatImage);
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST combineMagnitudeWithMask()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void combineMagnitudeWithMaskThatHasNoEffect() {
        int[] gradientMagnitude1 = {
                2147483647, 2147483647, 2147483647,
                2147483647, 160724, 2147483647,
                2147483647, 2147483647, 2147483647
        };

        int width = 3;
        int height = 3;

        int[] ineffectiveMask = {0xf, 0x1, 0xfff00000,   //this mask has no effect, due to the middle pixel not being black
                0x46, 0x72ad, 0x76aaff,
                0x0, 0xff, 0x10,
        };
        int[] expectedResult = Arrays.copyOf(gradientMagnitude1, gradientMagnitude1.length);

        new SeamCarving().combineMagnitudeWithMask(gradientMagnitude1, ineffectiveMask, width, height);
        assertArrayEquals(expectedResult, gradientMagnitude1);
    }

    @Test
    void combineMagnitudeWithMaskThatHasAnEffect() {
        int[] gradientMagnitude1 = {
                2147483647, 2147483647, 2147483647,
                2147483647, 160724, 2147483647,
                2147483647, 2147483647, 2147483647
        };

        int width = 3;
        int height = 3;

        int[] effectiveMask = {0xf, 0x1, 0xfff00000,   //this mask has an effect, due to the middle pixel being black
                0x46, 0x0, 0x76aaff,
                0x0, 0xff, 0x10,
        };
        int[] expectedResult = Arrays.copyOf(gradientMagnitude1, gradientMagnitude1.length);
        expectedResult[4] = Integer.MAX_VALUE;

        new SeamCarving().combineMagnitudeWithMask(gradientMagnitude1, effectiveMask, width, height);
        assertArrayEquals(expectedResult, gradientMagnitude1);
    }

    @Test
    void combineMagnitudeWithMaskNoWidthHeight() {
        int[] gradientMagnitude1 = {
                2147483647, 2147483647, 2147483647,
                2147483647, 160724, 2147483647,
                2147483647, 2147483647, 2147483647
        };

        int width = 0;
        int height = 0;

        int[] effectiveMask = {0xf, 0x1, 0xfff00000,   //this mask has an effect, due to the middle pixel being black
                0x46, 0x0, 0x76aaff,
                0x0, 0xff, 0x10,
        };
        int[] expectedResult = Arrays.copyOf(gradientMagnitude1, gradientMagnitude1.length);

        new SeamCarving().combineMagnitudeWithMask(gradientMagnitude1, effectiveMask, width, height);
        assertArrayEquals(expectedResult, gradientMagnitude1);
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST buildSeams()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void buildSeams3x3Image() {
        int[] gradientMagnitude = {
                2147483647, 2147483647, 2147483647,
                2147483647, 160724, 2147483647,
                2147483647, 2147483647, 2147483647
        };
        int width = 3;
        int height = 3;
        int[][] seams = new int[width][height];
        long[] seamWeights = new long[width];

        var seamcarving = new SeamCarving();
        seamcarving.buildSeams(seams, seamWeights, gradientMagnitude, width, height);

        int[][] seams_expectedResult = {
                {0, 1, 1},
                {1, 1, 1},
                {2, 1, 1}
        };
        long[] seamWeights_expectedResult = {4295128018L, 4295128018L, 4295128018L};

        assertArrayEquals(seamWeights_expectedResult, seamWeights);
        assertTrue(Arrays.deepEquals(seams_expectedResult, seams));
    }

    @Test
    void buildSeams5x6Image() {
        int[] gradientMagnitude = {
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647,
                2147483647, 192379, 119087, 243044, 2147483647,
                2147483647, 118757, 60630, 143085, 2147483647,
                2147483647, 106934, 151917, 76481, 2147483647,
                2147483647, 283300, 72146, 89950, 2147483647,
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647
        };
        int width = 5;
        int height = 6;
        int[][] seams = new int[width][height];
        long[] seamWeights = new long[width];

        var seamcarving = new SeamCarving();
        seamcarving.buildSeams(seams, seamWeights, gradientMagnitude, width, height);

        int[][] seams_expectedResult = {
                {0, 1, 2, 3, 2, 2},
                {1, 2, 2, 3, 2, 2},
                {2, 2, 2, 3, 2, 2},
                {3, 2, 2, 3, 2, 2},
                {4, 3, 2, 3, 2, 2}
        };
        long[] seamWeights_expectedResult = {4295368930L, 4295295638L, 4295295638L, 4295295638L, 4295419595L};

        assertArrayEquals(seamWeights_expectedResult, seamWeights);
        assertTrue(Arrays.deepEquals(seams_expectedResult, seams));
    }

    @Test
    void buildSeams0x0Image() {
        int[] gradientMagnitude = {
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647,
                2147483647, 192379, 119087, 243044, 2147483647,
                2147483647, 118757, 60630, 143085, 2147483647,
                2147483647, 106934, 151917, 76481, 2147483647,
                2147483647, 283300, 72146, 89950, 2147483647,
                2147483647, 2147483647, 2147483647, 2147483647, 2147483647
        };
        int width = 0;
        int height = 0;
        int[][] seams = new int[width][height];
        long[] seamWeights = new long[width];

        var seamcarving = new SeamCarving();
        seamcarving.buildSeams(seams, seamWeights, gradientMagnitude, width, height);

        int[][] seams_expectedResult = new int[height][width];
        long[] seamWeights_expectedResult = {};

        assertArrayEquals(seamWeights_expectedResult, seamWeights);
        assertTrue(Arrays.deepEquals(seams_expectedResult, seams));
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST removeSeam()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void removeSeam011From3x3Image() {
        int[] image1 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0x0, 0x12bbff37, 0x2800ed,
                0xffff0064, 0xff7f24, 0xffffffff,
                0x49b8ff, 0x46ff0064, 0x00ff2424
        };
        int[] seam = {0, 1, 1};
        int width = 3;
        int height = 3;

        var seamcarving = new SeamCarving();
        seamcarving.removeSeam(seam, image1, height, width);

        int[] expected_image_without_seam = {
                0x12bbff37, 0x2800ed,
                0xffff0064, 0xffffffff,
                0x49b8ff, 0x00ff2424, 0x49b8ff, 0x46ff0064, 0x00ff2424
        };

        assertArrayEquals(Arrays.copyOf(expected_image_without_seam, 6), Arrays.copyOf(image1, 6));
    }

    @Test
    void removeSeam432321From5x6Image() {
        int[] image2 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0xffffff, 0x780000, 0x000, 0x0, 0xfffffff,
                0xffffffff, 0x0, 0x23ff1291, 0x008200, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xf6dff9f, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0xda80ff, 0x0,
                0xffffffff, 0xdeffffff, 0x00, 0xffffff, 0x56912ff,
                0x01ffffff, 0x0, 0xffffff, 0x80caff, 0xfffffff
        };
        int[] seam = {4, 3, 2, 3, 2, 1};
        int width = 5;
        int height = 6;

        var seamcarving = new SeamCarving();
        seamcarving.removeSeam(seam, image2, height, width);

        int[] expected_image_without_seam = {
                0xffffff, 0x780000, 0x000, 0x0,
                0xffffffff, 0x0, 0x23ff1291, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0x0,
                0xffffffff, 0xdeffffff, 0xffffff, 0x56912ff,
                0x01ffffff, 0xffffff, 0x80caff, 0xfffffff, 0xffffff, 0x56912ff, 0x01ffffff, 0xffffff, 0x80caff, 0xfffffff
        };

        assertArrayEquals(Arrays.copyOf(expected_image_without_seam, 24), Arrays.copyOf(image2, 24));
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++
    // TEST shrink()
    // ++++++++++++++++++++++++++++++++++++++++++++++++

    @Test
    void shrink3x3ImageBy1PixelIneffectiveMask() {
        int[] image1 = {
                0x0, 0x12bbff37, 0x2800ed,
                0xffff0064, 0xff7f24, 0xffffffff,
                0x49b8ff, 0x46ff0064, 0x00ff2424
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff
        };

        int width = 3;
        int newWidth = 2;
        int height = 3;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = {
                0x12bbff37, 0x2800ed,
                0xffff0064, 0xffffffff,
                0x49b8ff, 0x00ff2424
        };

        assertArrayEquals(expectedImage, shrunkImage);
    }

    @Test
    void shrink3x3ImageBy2PixelsIneffectiveMask() {
        int[] image1 = {
                0x0, 0x12bbff37, 0x2800ed,
                0xffff0064, 0xff7f24, 0xffffffff,
                0x49b8ff, 0x46ff0064, 0x00ff2424
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff
        };

        int width = 3;
        int newWidth = 1;
        int height = 3;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = { //despite the removed values not being the smallest, they are removed because they are all image borders
                0x2800ed,
                0xffffffff,
                0x00ff2424
        };

        assertArrayEquals(expectedImage, shrunkImage);
    }

    @Test
    void shrink3x3ImageBy1PixelEffectiveMask() {
        int[] image1 = {
                0x0, 0x12bbff37, 0x2800ed,
                0xffff0064, 0xff7f24, 0xffffffff,
                0x49b8ff, 0x46ff0064, 0x00ff2424
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0x0, 0xffffff,
                0xffffff, 0xffffff, 0x0
        };

        int width = 3;
        int newWidth = 2;
        int height = 3;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = { //despite the removed values not being the smallest, they are removed because they are all image borders
                0x12bbff37, 0x2800ed,
                0xff7f24, 0xffffffff,
                0x46ff0064, 0x00ff2424
        };

        assertArrayEquals(expectedImage, shrunkImage);
    }

    @Test
    void shrink5x6ImageBy1PixelIneffectiveMask() {
        int[] image1 = {
                0xffffff, 0x780000, 0x000, 0x0, 0xfffffff,
                0xffffffff, 0x0, 0x23ff1291, 0x008200, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xf6dff9f, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0xda80ff, 0x0,
                0xffffffff, 0xdeffffff, 0x00, 0xffffff, 0x56912ff,
                0x01ffffff, 0x0, 0xffffff, 0x80caff, 0xfffffff
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff
        };

        int width = 5;
        int newWidth = 4;
        int height = 6;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = {
                16777215, 0, 0, 268435455,
                -1, 0, 33280, -6456,
                16777215, 14409471, 16777215, 16777215,
                7279359, 587174948, 12568576, 0,
                -1, -553648129, 16777215, 90772223,
                33554431, 0, 8440575, 268435455

        };

        assertArrayEquals(expectedImage, shrunkImage);
    }

    @Test
    void shrink5x0ImageBy2PixelsIneffectiveMask() {
        int[] image1 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0xffffff, 0x780000, 0x000, 0x0, 0xfffffff,
                0xffffffff, 0x0, 0x23ff1291, 0x008200, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xf6dff9f, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0xda80ff, 0x0,
                0xffffffff, 0xdeffffff, 0x00, 0xffffff, 0x56912ff,
                0x01ffffff, 0x0, 0xffffff, 0x80caff, 0xfffffff
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff
        };

        int width = 5;
        int newWidth = 3;
        int height = 0;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = {};

        assertArrayEquals(expectedImage, shrunkImage);
    }

    @Test
    void shrink0x6ImageBy2PixelsIneffectiveMask() {
        int[] image1 = {   //the format of the hex codes is mixed on purpose to make sure every valid entry (w and w/o transparency) works
                0xffffff, 0x780000, 0x000, 0x0, 0xfffffff,
                0xffffffff, 0x0, 0x23ff1291, 0x008200, 0xffffe6c8,
                0xffffff, 0xdbdeff, 0xf6dff9f, 0xffffff, 0xffffff,
                0x6f12ff, 0x22ff9424, 0xbfc800, 0xda80ff, 0x0,
                0xffffffff, 0xdeffffff, 0x00, 0xffffff, 0x56912ff,
                0x01ffffff, 0x0, 0xffffff, 0x80caff, 0xfffffff
        };

        int[] mask = {
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff,
                0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff
        };

        int width = 0;
        int newWidth = 0;
        int height = 6;

        var seamcarving = new SeamCarving();
        int[] shrunkImage = seamcarving.shrink(image1, mask, width, height, newWidth);

        int[] expectedImage = {};

        assertArrayEquals(expectedImage, shrunkImage);
    }

}
