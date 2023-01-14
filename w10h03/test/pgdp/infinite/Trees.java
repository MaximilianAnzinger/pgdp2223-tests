package pgdp.infinite;

import java.util.function.Supplier;
import pgdp.infinite.tree.InfiniteTree;

public class Trees {
    private Trees() {}

    public static Supplier<InfiniteTree<Integer>> binaryCounter = () ->
            new InfiniteTree<>(Lambdas.binaryCounter);

    public static Supplier<InfiniteTree<Integer>> reverseCollatz = () ->
            new InfiniteTree<>(Lambdas.reverseCollatz);

    public static Supplier<InfiniteTree<Integer>> childAnomaly = () ->
            new InfiniteTree<>(Lambdas.childAnomaly);

    public static Supplier<InfiniteTree<String>> libraryOfBabel = () ->
            new InfiniteTree<>(Lambdas.libraryOfBabel);

    public static Supplier<InfiniteTree<Integer>> countUpDown = () ->
            new InfiniteTree<>(Lambdas.countUpDown);
    
    public static Supplier<InfiniteTree<String[]>> makeHeavyChildren = () ->
            new InfiniteTree<>(Lambdas.makeHeavyChildren);
    
    public static Supplier<InfiniteTree<Integer>> makeManyChildren = () ->
            new InfiniteTree<>(Lambdas.makeManyChildren);
}
