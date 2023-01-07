package pgdp.pingunetwork.structural;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pingunetwork.Picture;

public class PictureStructuralTest extends StructuralTest<Picture> {

    public PictureStructuralTest() {
        super(Picture.class);
    }

    @Test
    @DisplayName("Check fields")
    void checkFields() {
        expectAttribute("location", 0, String.class, PRIVATE);
        expectAttribute("width", 1, int.class, PRIVATE);
        expectAttribute("height", 2, int.class, PRIVATE);
        expectAttribute("data", 3, int[][].class, PRIVATE);
        expectAttribute("thumbnails", Picture[].class, PRIVATE);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(PUBLIC, String.class, int[][].class);
    }

    @Test
    @DisplayName("Check getters")
    void checkGetters() {
        expectMethod(PUBLIC, "getLocation");
        expectMethod(PUBLIC, "getWidth");
        expectMethod(PUBLIC, "getHeight");
        expectMethod(PUBLIC, "getData");
        expectMethod(PUBLIC, "getThumbnails");
    }

    @Test
    @DisplayName("Check setters")
    void checkSetters() {
        expectMethod(PUBLIC, "setThumbnails", Picture[].class);
        dontExpectMethod(PUBLIC, "setLocation", String.class);
        dontExpectMethod(PUBLIC, "setWidth", int.class);
        dontExpectMethod(PUBLIC, "setHeight", int.class);
        dontExpectMethod(PUBLIC, "setData", int[][].class);
    }

}
