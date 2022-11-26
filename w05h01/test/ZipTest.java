import pgdp.datastructures.lists.RecIntList;

import org.junit.jupiter.api.*;

public class ZipTest {
    public RecIntList RecList(int... args) {
        var r = new RecIntList();

        for (int i = 0; i < args.length; i++) {
            r.append(args[i]);
        }

        return r;
    }

    public void assertListEquals(RecIntList l, String s) {
        if (s == "") {
            Assertions.assertEquals("Empty list", l.toString());
        } else {
            Assertions.assertEquals("List: [" + s + "]", l.toString());
        }
    }

    @Test
    @DisplayName("Should zip empty arrays")
    public void empty() {
        var l1 = RecList();
        var l2 = RecList();

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "");
    }

    @Test
    @DisplayName("Should zip example arrays")
    public void example() {
        var l1 = RecList(1,3,5,7,8);
        var l2 = RecList(2,4,6);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 7, 8");
    }

    @Test
    @DisplayName("Should zip when second array is empty")
    public void only_one() {
        var l1 = RecList(1,3,5,7,8);
        var l2 = RecList();

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 3, 5, 7, 8");
    }

    @Test
    @DisplayName("Should zip when first array is empty")
    public void only_two() {
        var l1 = RecList();
        var l2 = RecList(1,3,5,7,8);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 3, 5, 7, 8");
    }

    @Test
    @DisplayName("Should zip when first array is smaller by one")
    public void one_smaller() {
        var l1 = RecList(1,3,5);
        var l2 = RecList(2,4,6,8);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 8");
    }

    @Test
    @DisplayName("Should zip when second array is smaller by one")
    public void two_smaller() {
        var l1 = RecList(1,3,5,8);
        var l2 = RecList(2,4,6);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 8");
    }

    @Test
    @DisplayName("Should zip when first array is smaller")
    public void one_great_smaller() {
        var l1 = RecList(1,3,5);
        var l2 = RecList(2,4,6,8,9,9,9);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 8, 9, 9, 9");
    }

    @Test
    @DisplayName("Should zip when second array is smaller")
    public void two_great_smaller() {
        var l1 = RecList(1,3,5,8,9,9,9);
        var l2 = RecList(2,4,6);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 8, 9, 9, 9");
    }

    @Test
    @DisplayName("Should zip when both arrays contain one element.")
    public void equals_one() {
        var l1 = RecList(1);
        var l2 = RecList(2);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2");
    }

    @Test
    @DisplayName("Should zip when both arrays are of equal length")
    public void equal() {
        var l1 = RecList(1,3,5,7,9);
        var l2 = RecList(2,4,6,8,10);

        RecIntList.zip(l1, l2);

        assertListEquals(l1, "1, 2, 3, 4, 5, 6, 7, 8, 9, 10");
    }
}
