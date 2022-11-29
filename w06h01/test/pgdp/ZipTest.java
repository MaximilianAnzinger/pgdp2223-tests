package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.datastructures.lists.RecIntList;

public class ZipTest extends TestBase {

    public static RecIntList l1;
    public static RecIntList l2;

    @Test
    @DisplayName("Should zip empty arrays")
    public void empty() {
        l1 = list();
        l2 = list();
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(), l1);
    }

    @Test
    @DisplayName("Should zip example arrays")
    public void example() {
        l1 = list(1, 3, 5, 7, 8);
        l2 = list(2, 4, 6);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 7, 8), l1);
    }

    @Test
    @DisplayName("Should zip when second array is empty")
    public void only_one() {
        l1 = list(1, 3, 5, 7, 8);
        l2 = list();
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 3, 5, 7, 8), l1);
    }

    @Test
    @DisplayName("Should zip when first array is empty")
    public void only_two() {
        l1 = list();
        l2 = list(1, 3, 5, 7, 8);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 3, 5, 7, 8), l1);
    }

    @Test
    @DisplayName("Should zip when first array is smaller by one")
    public void one_smaller() {
        l1 = list(1, 3, 5);
        l2 = list(2, 4, 6, 8);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 8), l1);
    }

    @Test
    @DisplayName("Should zip when second array is smaller by one")
    public void two_smaller() {
        l1 = list(1, 3, 5, 8);
        l2 = list(2, 4, 6);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 8), l1);
    }

    @Test
    @DisplayName("Should zip when first array is smaller")
    public void one_great_smaller() {
        l1 = list(1, 3, 5);
        l2 = list(2, 4, 6, 8, 9, 9, 9);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 8, 9, 9, 9), l1);
    }

    @Test
    @DisplayName("Should zip when second array is smaller")
    public void two_great_smaller() {
        l1 = list(1, 3, 5, 8, 9, 9, 9);
        l2 = list(2, 4, 6);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 8, 9, 9, 9), l1);
    }

    @Test
    @DisplayName("Should zip when both arrays contain one element.")
    public void equals_one() {
        l1 = list(1);
        l2 = list(2);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2), l1);
    }

    @Test
    @DisplayName("Should zip when both arrays are of equal length")
    public void equal() {
        l1 = list(1, 3, 5, 7, 9);
        l2 = list(2, 4, 6, 8, 10);
        var data = asDataList(l1, l2);

        RecIntList.zip(l1, l2);

        checkIllegalModification(l1, data);
        assertListEquals(list(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), l1);
    }
}
