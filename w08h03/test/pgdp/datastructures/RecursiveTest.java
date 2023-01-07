package pgdp.datastructures;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class RecursiveTest {

    Random random = new Random(420);

    ArrayList<Integer> getInput() {
        ArrayList<Integer> input = new ArrayList<>();
        int amountOfValues = random.nextInt(250);
        for (int i = 0; i < amountOfValues; i++) {
            input.add(random.nextInt());
        }
        return input;
    }

    @Test
    void test() {


        Set<String> treeCalls = new HashSet<>();
        Set<String> nodeCalls = new HashSet<>();

        for (int i = 0; i < 100; i++) {
            QuarternarySearchTreeWrapper<Integer> tree = new QuarternarySearchTreeWrapper<>();

            for (int e : getInput()) {
                tree.insert(e);
            }

            tree.reset();

            for (Integer discard : tree) ; // do nothing... just iterate and potentially call recursive methods

            treeCalls.addAll(tree.getRecursiveCalls());

            QuarternaryNodeWrapper<Integer> root = (QuarternaryNodeWrapper<Integer>) tree.getRoot();
            nodeCalls.addAll(root.getRecursiveCalls());

        }

        Set<String> treeCallsReadable = treeCalls.stream().map(s -> "QuarternarySearchTree." + s + "()").collect(Collectors.toSet());
        Assertions.assertTrue(treeCalls.isEmpty(), "You must not use recursion. Offending method(s): " + treeCallsReadable);

        Set<String> nodeCallsReadable = nodeCalls.stream().map(s -> "QuarternaryNode." + s + "()").collect(Collectors.toSet());
        Assertions.assertTrue(nodeCalls.isEmpty(), "You must not use recursion. Offending method(s): " + nodeCallsReadable);
    }


}

class ArrayListWrapper<T extends Comparable<T>> extends ArrayList<QuarternaryNode<T>> {

    @Override
    public QuarternaryNode<T> set(int index, QuarternaryNode<T> element) {

        ArrayListWrapper<T> listWrapper = new ArrayListWrapper<>();
        listWrapper.addAll(element.getChildren());
        element.setChildren(listWrapper);

        return super.set(index, new QuarternaryNodeWrapper<T>(element));
    }

}

class QuarternaryNodeWrapper<T extends Comparable<T>> extends QuarternaryNode<T> {

    Set<String> recursiveCalls = new HashSet<>();

    public void reset() {
        recursiveCalls.clear();
        List<QuarternaryNode<T>> children = getChildren();
        children.stream().filter(Objects::nonNull).forEach(c -> {
            QuarternaryNodeWrapper<T> node = (QuarternaryNodeWrapper<T>) c;
            node.reset();
        });
    }

    public Set<String> getRecursiveCalls() {

        Set<String> recursiveCalls = new HashSet<>(this.recursiveCalls);

        List<QuarternaryNode<T>> children = getChildren();
        children.stream().filter(Objects::nonNull).forEach(c -> {
            QuarternaryNodeWrapper<T> node = (QuarternaryNodeWrapper<T>) c;
            recursiveCalls.addAll(node.getRecursiveCalls());
        });

        return recursiveCalls;
    }

    public QuarternaryNodeWrapper(QuarternaryNode<T> node) {
        super(null);
        this.setNodeSize(node.getNodeSize());
        ArrayListWrapper<T> wrapper = new ArrayListWrapper<>();
        wrapper.addAll(node.getChildren());
        this.setChildren(wrapper);
        this.setValues(node.getValues());
    }

    @Override
    public int height() {
        recursiveCalls.add("height");
        return super.height();
    }

    @Override
    public int size() {
        recursiveCalls.add("size");
        return super.height();
    }

    @Override
    public void insert(T value) {
        recursiveCalls.add("insert");
        super.insert(value);
    }

    @Override
    public boolean contains(T value) {
        recursiveCalls.add("contains");
        return super.contains(value);
    }

    @Override
    public String toString() {
        recursiveCalls.add("toString");
        return super.toString();
    }

    @Override
    public String toGraphvizStringHelper() {
        recursiveCalls.add("toGraphvizStringHelper");
        return super.toGraphvizStringHelper();
    }
}

class QuarternarySearchTreeWrapper<T extends Comparable<T>> extends QuarternarySearchTree<T> {

    Set<String> recursiveCalls = new HashSet<>();

    public Set<String> getRecursiveCalls() {
        return recursiveCalls;
    }

    public void reset() {
        recursiveCalls.clear();
        QuarternaryNodeWrapper<T> root = (QuarternaryNodeWrapper<T>) getRoot();
        root.reset();
    }

    @Override
    public int size() {
        recursiveCalls.add("size");
        return super.size();
    }

    @Override
    public boolean contains(T value) {
        recursiveCalls.add("contains");
        return super.contains(value);
    }

    boolean firstInsert = true;

    @Override
    public void insert(T value) {
        recursiveCalls.add("insert");
        super.insert(value);
        if (firstInsert) {
            super.setRoot(new QuarternaryNodeWrapper<>(super.getRoot()));
            firstInsert = false;
        }
    }

    @Override
    public String toString() {
        recursiveCalls.add("toString");
        return super.toString();
    }

    @Override
    public String toGraphvizString() {
        recursiveCalls.add("toGraphvizString");
        return super.toGraphvizString();
    }
}