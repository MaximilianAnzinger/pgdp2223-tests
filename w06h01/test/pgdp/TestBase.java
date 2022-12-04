package pgdp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            RecIntListElement expEle = head(expected);
            RecIntListElement actEle = head(actual);
            for (int i = 0; i < size; i++) {
                RecIntListElement expPrev = prev(expEle);
                RecIntListElement expNext = next(expEle);
                RecIntListElement actPrev = prev(actEle);
                RecIntListElement actNext = next(actEle);
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

    final void checkIllegalModification(RecIntList after, List<RecIntListElementData> dataBefore) {
        try {
            List<RecIntListElement> elementsAfter = readElements(after);

            for (int index = 0; index < elementsAfter.size(); index++) {
                RecIntListElement elementAfter = elementsAfter.get(index);
                var elementBefore = dataBefore.stream().filter($ -> $._hashCode == elementAfter.hashCode()).findFirst();

                int finalIndex = index;
                elementBefore.ifPresentOrElse(prev -> {
                    int beforeVal = prev.value;
                    int afterVal = elementAfter.get(0);
                    assertEquals(beforeVal, afterVal, illegalObjectModificationMessage(after, finalIndex));
                }, () -> illegalObjectCreationMessage(after, finalIndex));
            }
        } catch (Exception e) {
            fail("Could not test for illegal modifications on RecIntList because an exception occurred during the test's evaluation", e);
        }
    }

    /**
     * Maps each element of {@code lists} to one object of RecIntListElementData.<p>
     * Required for some exercises as they require the list elements not to be modified,
     * but we can't pass the same list after the method execution as the list before and
     * after the method execution would be identical.
     *
     * @param lists The list whose elements should be mapped to a data object
     * @return the list of mapped data objects
     */
    final List<RecIntListElementData> asDataList(RecIntList... lists) {
        try {
            List<RecIntListElement> elements = new ArrayList<>();
            for (RecIntList list : lists) {
                elements.addAll(readElements(list));
            }
            return elements.stream().map($ -> new RecIntListElementData($.hashCode(), $.get(0))).toList();
        } catch (IllegalAccessException e) {
            fail("Could not transform RecIntList to List<RecIntListElementData> because an exception occurred during the test's evaluation", e);
            return new ArrayList<>();
        }
    }

    private static void illegalObjectCreationMessage(RecIntList list, int index) {
        fail(">> Illegal object creation in list detected! <<\nThis means that you might have " +
             "created a new RecIntListElement object when it was not allowed by the " +
             "exercise.\nDetected illegal element creation in resulting list at index " + index + ".\n" +
             "-> " + list.toConnectionString() + "\n");
    }

    private static String illegalObjectModificationMessage(RecIntList list, int index) {
        return ">> Illegal object modification in list detected! << \nThis means that you might have " +
               "changed the value of a RecIntListElement when it was not allowed by the " +
               "exercise.\nDetected illegal element modification in resulting list at index " + index + ".\n" +
               "-> " + list.toConnectionString() + "\n";
    }

    //<editor-fold desc="Helper methods and record">
    private List<RecIntListElement> readElements(RecIntList list) throws IllegalAccessException {
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        }
        List<RecIntListElement> elements = new ArrayList<>();
        RecIntListElement ele = head(list);
        do {
            elements.add(ele);
            ele = next(ele);
        } while (ele != null);
        return elements;
    }

    private RecIntListElement head(RecIntList list) throws IllegalAccessException {
        return (RecIntListElement) headField.get(list);
    }

    private RecIntListElement prev(RecIntListElement list) throws IllegalAccessException {
        return (RecIntListElement) prevField.get(list);
    }

    private RecIntListElement next(RecIntListElement list) throws IllegalAccessException {
        return (RecIntListElement) nextField.get(list);
    }

    record RecIntListElementData(int _hashCode, int value) {

    }
    //</editor-fold>
}
