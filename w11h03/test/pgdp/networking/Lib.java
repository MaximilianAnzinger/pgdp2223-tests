package pgdp.networking;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Lib {
    private static int seed = 10;
    private static Random random = new Random(seed);

    private static Set<String> vowels = Set.of("a", "e", "i", "o", "u");
    private static Set<String> consonants = IntStream.range((int) 'a', (int) 'z' + 1).mapToObj(i -> "" + (char) i)
            .collect(Collectors.toSet());
    private static Set<String> digits = IntStream.range(0, 10).mapToObj(i -> "" + i).collect(Collectors.toSet());

    public static String generateKennung() {
        consonants.removeAll(vowels);

        return (String) consonants.toArray()[random.nextInt(consonants.size())]
                + vowels.toArray()[random.nextInt(vowels.size())]
                + digits.toArray()[random.nextInt(digits.size())]
                + digits.toArray()[random.nextInt(digits.size())]
                + consonants.toArray()[random.nextInt(consonants.size())]
                + vowels.toArray()[random.nextInt(vowels.size())]
                + consonants.toArray()[random.nextInt(consonants.size())];

    }

    //
    // https://github.com/MaximilianAnzinger/pgdp2223-tests/blob/main/w10h02/test/pgdp/teams/Lib.java
    //

    public static <T, F> Field getField(T obj, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static <T, F> Method getMethod(T obj, String fieldName)
            throws NoSuchMethodException, IllegalAccessException {
        Method method = obj.getClass().getDeclaredMethod(fieldName);
        method.setAccessible(true);
        return method;
    }
}
