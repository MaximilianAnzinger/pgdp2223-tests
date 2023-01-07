package pgdp.datastructures;

import org.junit.jupiter.api.RepeatedTest;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomTest {
    Random intTest = new Random(100);
    Random doubleTest = new Random(100);
    Random dateTest = new Random(100);
    Random stringTest = new Random(100);
    
    // If you want to increase the number of runs per test, simply increase 
    // this number.
    //
    // Please note that the tests might be slow, if you are using VS Code.
    // IntelliJ seems to be much faster.
    final int numberOfTests = 1000;
    
    @RepeatedTest(numberOfTests) // amount tests
    public void randomlyTestIntValues() {
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<Integer> expected = new ArrayList<>();
        
        int amountOfValues = intTest.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(intTest.nextInt());
        }
        expected = new ArrayList<>(values);
        Collections.sort(expected);
        QuarternarySearchTree<Integer> n = new QuarternarySearchTree<Integer>();
        for (Integer i : values) {
            n.insert(i);
        }
        ArrayList<Integer> actual = new ArrayList<>();
        for (Integer r : n) {
            actual.add(r);
        }
        assertEquals(expected, actual);
    }

    @RepeatedTest(numberOfTests) // amount tests
    public void randomlyTestDoubleValues() {
        ArrayList<Double> values = new ArrayList<>();
        ArrayList<Double> expected = new ArrayList<>();
        
        int amountOfValues = doubleTest.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(doubleTest.nextDouble());
        }
        expected = new ArrayList<>(values);
        Collections.sort(expected);
        QuarternarySearchTree<Double> n = new QuarternarySearchTree<Double>();
        for (Double i : values) {
            n.insert(i);
        }
        ArrayList<Double> actual = new ArrayList<>();
        for (Double r : n) {
            actual.add(r);
        }
        assertEquals(expected, actual);
    }

    @RepeatedTest(numberOfTests) // amount tests
    public void randomlyTestDateValues() {
        ArrayList<Date> values = new ArrayList<>();
        ArrayList<Date> expected = new ArrayList<>();
        
        int amountOfValues = dateTest.nextInt(250); //max amount of values the tree should contain
        for (int i = 0; i < amountOfValues; i++) {
            values.add(new Date(dateTest.nextLong()));
        }
        expected = new ArrayList<>(values);
        Collections.sort(expected);
        QuarternarySearchTree<Date> n = new QuarternarySearchTree<Date>();
        for (Date i : values) {
            n.insert(i);
        }
        ArrayList<Date> actual = new ArrayList<>();
        for (Date r : n) {
            actual.add(r);
        }
        assertEquals(expected, actual);
    }

    @RepeatedTest(numberOfTests) // amount tests
    public void randomlyTestStringValues() {
        ArrayList<String> values = new ArrayList<>();
        ArrayList<String> expected = new ArrayList<>();

        int maxStringLength = 100; //max length of an individual string
        
        int amountOfValues = stringTest.nextInt(100); //max amount of values the tree should contain
        byte[] array = new byte[maxStringLength];
        for (int i = 0; i < amountOfValues; i++) {
            stringTest.nextBytes(array);
            values.add(new String(array, StandardCharsets.UTF_8));
        }
        expected = new ArrayList<>(values);
        Collections.sort(expected);
        QuarternarySearchTree<String> n = new QuarternarySearchTree<String>();
        for (String i : values) {
            n.insert(i);
        }
        ArrayList<String> actual = new ArrayList<>();
        for (String r : n) {
            actual.add(r);
        }
        assertEquals(expected, actual);
    }
}
