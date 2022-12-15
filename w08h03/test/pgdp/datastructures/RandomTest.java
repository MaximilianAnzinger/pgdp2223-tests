package pgdp.datastructures;

import org.junit.jupiter.api.RepeatedTest;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTest {
    @RepeatedTest(10000) // amount tests
    public void randomlyTestIntValues() {
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> solution = new ArrayList<>();

        Random num = new Random();
        int amountOfValues = num.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(num.nextInt());
            solution = new ArrayList<>(values);
            Collections.sort(solution);
        }
        QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
        for (Integer i : values) {
            n.insert(i);
        }
        ArrayList<Integer> actual = new ArrayList<>();
        for (Integer r : n) {
            actual.add(r);
        }
        assertEquals(actual, solution);
    }

    @RepeatedTest(10000) // amount tests
    public void randomlyTestDoubleValues() {
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Double> solution = new ArrayList<>();

        Random num = new Random();
        int amountOfValues = num.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(num.nextDouble());
            solution = new ArrayList<>(values);
            Collections.sort(solution);
        }
        QuarternarySearchTree<Double> n = new QuarternarySearchTree<Double>();
        for (Double i : values) {
            n.insert(i);
        }
        ArrayList<Double> actual = new ArrayList<>();
        for (Double r : n) {
            actual.add(r);
        }
        assertEquals(actual, solution);
    }

    @RepeatedTest(10000) // amount tests
    public void randomlyTestDateValues() {
        ArrayList<Date> values = new ArrayList<>();
        ArrayList<Date> solution = new ArrayList<>();

        Random num = new Random();
        int amountOfValues = num.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(new Date(num.nextLong()));
            solution = new ArrayList<>(values);
            Collections.sort(solution);
        }
        QuarternarySearchTree<Date> n = new QuarternarySearchTree<Date>();
        for (Date i : values) {
            n.insert(i);
        }
        ArrayList<Date> actual = new ArrayList<>();
        for (Date r : n) {
            actual.add(r);
        }
        assertEquals(actual, solution);
    }

    @RepeatedTest(10000) // amount tests
    public void randomlyTestStringValues() {
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> solution = new ArrayList<>();

        int maxStringLength = 100; //max length of an individual string

        Random num = new Random();
        int amountOfValues = num.nextInt(100); //max amount of values the tree should contain
        byte[] array = new byte[maxStringLength];
        for (int i = 0; i < amountOfValues; i++) {
            num.nextBytes(array);
            values.add(new String(array, StandardCharsets.UTF_8));
            solution = new ArrayList<>(values);
            Collections.sort(solution);
        }
        QuarternarySearchTree<String> n = new QuarternarySearchTree<String>();
        for (String i : values) {
            n.insert(i);
        }
        ArrayList<String> actual = new ArrayList<>();
        for (String r : n) {
            actual.add(r);
        }
        assertEquals(actual, solution);
    }
}
