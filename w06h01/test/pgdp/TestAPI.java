package pgdp;
import pgdp.datastructures.lists.RecIntList;
import org.junit.jupiter.api.*;

public class TestAPI {
    public static RecIntList RecList(int... args) {
        var r = new RecIntList();

        for (int i = 0; i < args.length; i++) {
            r.append(args[i]);
        }

        return r;
    }

    public static void assertListEquals(RecIntList l, String s) {
        if (s == "") {
            Assertions.assertEquals("Empty list", l.toString());
        } else {
            Assertions.assertEquals("List: [" + s + "]", l.toString());
        }
    }
}
