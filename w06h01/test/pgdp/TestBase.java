package pgdp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import pgdp.datastructures.lists.RecIntList;
import pgdp.datastructures.lists.RecIntListElement;

public class TestBase {

    private static Field headField;
    private static Field prevField;
    private static Field nextField;

    @BeforeAll
    static void beforeAll() throws NoSuchFieldException {
        headField = RecIntList.class.getDeclaredField("head");
        headField.setAccessible(true);
        prevField = RecIntListElement.class.getDeclaredField("prev");
        prevField.setAccessible(true);
        nextField = RecIntListElement.class.getDeclaredField("next");
        nextField.setAccessible(true);
    }

    static RecIntList list(int... args) {
        var r = new RecIntList();
        Arrays.stream(args).forEach(r::append);
        return r;
    }

    final void assertListEquals(RecIntList expected, RecIntList actual) {
        int size = expected.size();
        assertEquals(size, actual.size(), "Expected list does not match actual list in size");

        if (size == 0) {
            return;
        }
        try {
            RecIntListElement expEle = (RecIntListElement) headField.get(expected);
            RecIntListElement actEle = (RecIntListElement) headField.get(actual);
            for (int i = 0; i < size; i++) {
                RecIntListElement expPrev = (RecIntListElement) prevField.get(expEle);
                RecIntListElement expNext = (RecIntListElement) nextField.get(expEle);
                RecIntListElement actPrev = (RecIntListElement) prevField.get(actEle);
                RecIntListElement actNext = (RecIntListElement) nextField.get(actEle);
                int expValue = expEle.get(0);
                int actValue = actEle.get(0);

                assertEquals(expValue, actValue, "Your implementation does not produce the correct value at index " + i);
                if (expPrev == null) {
                    assertNull(actPrev, "Prev of element at index " + 0 + " has to be null.");
                } else {
                    assertEquals(expPrev.get(0), actPrev.get(0), "The value of prev of element at index " + i + " is not correct.");
                }
                if (expNext == null) {
                    assertNull(actNext, "Next of element at index " + 0 + " has to be null.");
                } else {
                    assertEquals(expNext.get(0), actNext.get(0), "The value of next of element at index " + i + " is not correct.");
                }

                expEle = expNext;
                actEle = actNext;
            }
        } catch (Exception e) {
            fail("Could not test for RecIntList equality because an exception occurred during the test's evaluation", e);
        }
    }
}
