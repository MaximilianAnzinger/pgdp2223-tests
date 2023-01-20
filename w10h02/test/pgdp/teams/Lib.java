package pgdp.teams;

import java.lang.reflect.Field;
import java.util.Set;

public class Lib {
    @SuppressWarnings("")
    public static Set<Penguin> getField(Lineup lineup, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = lineup.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Set<Penguin> ret = (Set<Penguin>) field.get(lineup);
        return ret;
    }

    //
    // Disclaimer: Ähnlichkeiten zu realen Pinguinen sind rein zufällig.
    //

    // Artemis Example 1
    public static Penguin jonas = new Penguin("Jonas", 10, 0, 0);
    public static Penguin anatoly = new Penguin("Anatoly", 10, 10, 0);
    public static Penguin julian = new Penguin("Julian", 10, 10, 0);
    public static Penguin simon = new Penguin("Simon", 0, 0, 10);

    // Artemis Example 2
    public static Penguin eve = new Penguin("Eve", 9151, 5, 11);
    public static Penguin enrico = new Penguin("Enrico", 97, 103, 3499);
    public static Penguin hanna = new Penguin("Hanna", 6367, 331, 337);
    public static Penguin sachmi = new Penguin("Sachmi", 103, 5701, 109);
    public static Penguin jasmine = new Penguin("Jasmine", 233, 5737, 239);
    public static Penguin jakob = new Penguin("Jakob", 307, 313, 3559);

    // Artemis Example 3
    public static Penguin eric = new Penguin("Eric", 10, 0, 0);
    public static Penguin nils = new Penguin("Nils", 10, 10, 0);
    public static Penguin felix = new Penguin("Felix", 10, 10, 0);
    public static Penguin thomas = new Penguin("Thomas", 0, 0, 10);

    // Artemis Example 4
    public static Penguin jan = new Penguin("Jan", -101, 177013, 777);
    public static Penguin georg = new Penguin("Georg", 9001, -25984, 66);
    public static Penguin anton = new Penguin("Anton", 300, 5180, -20000);
    public static Penguin johannes = new Penguin("Johannes", 0, 314, 2792);
    public static Penguin konrad = new Penguin("Konrad", 420, 8008, 911);
    public static Penguin max = new Penguin("Max", 1337, -161, 69);
    public static Penguin oliver = new Penguin("Oliver", 1, 271, 2319);
    public static Penguin robin = new Penguin("Robin", 13, 34, 666);
    public static Penguin laura = new Penguin("Laura", -37, 577, 1459);
    public static Penguin lukas = new Penguin("Lukas", -79, 549, 1123);

    // Test Examples + Requests
    // YOU CAN ADD YOUR OWN PENGUINS HERE

    public static Penguin faid = new Penguin("Faid", 0, 0, 5);
    public static Penguin zeynep = new Penguin("Zeynep", 10, 0, 0);
    public static Penguin cedric = new Penguin("Cedric", 5, 0, 0);
    public static Penguin jakov = new Penguin("Jakov", 5, 0, 0);
    public static Penguin yassine = new Penguin("Yassine", 0, 5, 5);
    public static Penguin malek = new Penguin("Malekoks", 0, 0, 10);

    public static Penguin glen = new Penguin("Glen", 101, 606, 101);
    public static Penguin fatjon = new Penguin("Fatjon", 0, 404, 1011);
    public static Penguin jani = new Penguin("Jani", 202, 303, 303);
    public static Penguin koco = new Penguin("Koco", 404, 404, 404);

    public static Penguin shrek = new Penguin("Shrek", 20, 15, 0);
    public static Penguin rick = new Penguin("Rick Astley", 20, 15, 0);
    public static Penguin guenther = new Penguin("Günther", -1, 100, -100);

    public static Penguin marcel = new Penguin("Marcel", 65_991_266, 38_925_528, 500);
    public static Penguin hansuwe = new Penguin("Hans-Uwe", 3, 81_492_381, -456);
    public static Penguin thorsten = new Penguin("Thorsten", 8, 108_558_119, -228_976_029);

    public static Penguin lester = new Penguin("lester", 10,0,0);

    public static Penguin roman = new Penguin("roman", 0, 10,0);

    public static Penguin levi = new Penguin("levi", 0, 0 ,10);
    // Artemis Examples
    static {
        Penguin.setSynergy(jonas, anatoly, 10);
        Penguin.setSynergy(jonas, julian, 5);

        Penguin.setSynergy(eve, hanna, 30);
        Penguin.setSynergy(enrico, jakob, 77);
        Penguin.setSynergy(sachmi, jasmine, 121);
        Penguin.setSynergy(jasmine, jakob, 34);
        Penguin.setSynergy(eve, sachmi, 1);

        Penguin.setSynergy(eric, nils, 20);
        Penguin.setSynergy(eric, felix, 5);

        Penguin.setSynergy(georg, max, 1137);
        Penguin.setSynergy(max, oliver, 33);
        Penguin.setSynergy(max, konrad, 9);
        Penguin.setSynergy(georg, anton, 2187);
        Penguin.setSynergy(oliver, anton, 1138);
        Penguin.setSynergy(jan, lukas, 883);
        Penguin.setSynergy(jan, laura, 787);
        Penguin.setSynergy(johannes, oliver, 420);
        Penguin.setSynergy(johannes, jan, 69);
    }

    // Tests
    static {
        Penguin.setSynergy(faid, zeynep, -50);
        Penguin.setSynergy(jakov, jakob, 500);

        Penguin.setSynergy(glen, fatjon, 50);
        Penguin.setSynergy(glen, jani, 140);
        Penguin.setSynergy(jani, koco, 200);

        Penguin.setSynergy(marcel, hansuwe, 2_000_000_000);
        Penguin.setSynergy(hansuwe, thorsten, -2_000_000_000);
        Penguin.setSynergy(marcel, thorsten, 1_000_000_000);

        Penguin.setSynergy(lester, roman, 20);
        Penguin.setSynergy(lester,levi,20);
        Penguin.setSynergy(levi,roman, 20);
    }
}
