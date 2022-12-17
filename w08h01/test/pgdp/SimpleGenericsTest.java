package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleGenericsTest {
    private List<Integer> list = new ArrayList<Integer>();

    public  List<Integer> getList() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        return list;
    }

    private Stack<String> stack = new Stack<>();

    public Stack<String> getStack() {
        stack.push("a");
        stack.push("b");
        stack.push("c");
        stack.push("d");
        return stack;
    }

    private Map<Integer, Integer> map = new HashMap<>();

    public Map<Integer, Integer> getMap() {
        map.put(4,4);
        map.put(2,5);
        map.put(3,4);
        map.put(0,4);
        map.put(9,4);
        map.put(12,9);
        map.put(6,-1);
        map.put(7,-1);

        return map;
    }

    private HashSet<Integer> hashSet = new HashSet<>();

    public HashSet<Integer> getHashSet() {
        hashSet.add(4);
        hashSet.add(5);
        hashSet.add(9);
        hashSet.add(-1);
        return hashSet;
    }

    @Test
    @DisplayName("toStringArraylist")
    void toString1() {
        assertEquals("{1, 2, 3, 4, 5}", SimpleGenerics.toString(getList()));
    }

    @Test
    @DisplayName("empty collection toString")
    void toString2(){
        assertEquals("{}", SimpleGenerics.toString(new ArrayList<>()));
    }
    @Test
    @DisplayName("toStringStack")
    void toString3() {
        assertEquals("{a, b, c, d}", SimpleGenerics.toString(getStack()));
    }
    @Test
    @DisplayName("empty collection toIntArray")
    void intArray1() {
        assertEquals("[]", Arrays.toString(SimpleGenerics.toIntArray(new ArrayList<>())));
    }

    @Test
    @DisplayName("toIntArray ArrayList")
    void intArray2() {
        assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(SimpleGenerics.toIntArray(getList())));
    }

    @Test
    @DisplayName("specialsort ArrayList")
    void specialSort1() {
        assertEquals("[1, 2, 3, 4, 5]", Arrays.toString(SimpleGenerics.specialSort(Integer.class, getList(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        })));
    }

    @Test
    @DisplayName("specialsort empty list")
    void specialSort2() {
        assertEquals("[]", Arrays.toString(SimpleGenerics.specialSort(Integer.class, new ArrayList<>(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        })));
    }

    @Test
    @DisplayName("specialsort stringStack")
    void specialsort3() {
        assertEquals("[a, b, c, d]", Arrays.toString(SimpleGenerics.specialSort(String.class, getStack(), new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        })));
    }

    @Test
    @DisplayName("intersection empty collections array")
    void intersection1() {
        Collection<ArrayList>[] collection = new Collection[0];
        assertEquals("[]", Arrays.toString(SimpleGenerics.intersection(collection).toArray()));
    }

    @Test
    @DisplayName("intersection collections array")
    void intersection2() {
        Collection<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(4);
        Collection<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(4);
        Collection<Integer> list3 = new ArrayList<>();
        list3.add(1);
        list3.add(2);
        list3.add(4);
        Collection<ArrayList>[] listArray = new Collection[]{list1, list, list3};
        assertEquals("[1, 2, 4]", Arrays.toString(SimpleGenerics.intersection(listArray).toArray()));
    }

    @Test
    @DisplayName("intersection collections array, with one empty collection")
    void intersectionWithEmptyList() {
        Collection<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(4);
        Collection<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(4);
        Collection<Integer> list3 = new ArrayList<>();
        Collection<ArrayList>[] listArray = new Collection[]{list1, list, list3};
        assertEquals("[]", Arrays.toString(SimpleGenerics.intersection(listArray).toArray()));
    }

    @Test
    @DisplayName("getValues empty map")
    void getValues1() {
        assertEquals(new HashSet<Integer>(), (SimpleGenerics.getValues(new HashMap<Integer, Integer>())));
    }

    @Test
    @DisplayName("getValues non-empty map")
    void getValues2() {
        assertEquals(getHashSet(), SimpleGenerics.getValues(getMap()));
    }
}
